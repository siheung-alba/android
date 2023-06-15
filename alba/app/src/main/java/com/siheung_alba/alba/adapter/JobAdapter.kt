package com.siheung_alba.alba.adapter

import android.util.Log
import com.siheung_alba.alba.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.model.JobModel
import com.siheung_alba.alba.model.ResumeModel


class JobAdapter( var itemList: ArrayList<JobModel>) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    public var showButtonClickListener: OnShowButtonClickListener? = null
    public var applyButtonClickListener:OnApplyButtonClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobAdapter.JobViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_item_layout, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.title.text = itemList[position].jobTitle
        holder.add_text.text = itemList[position].jobAddtext
        holder.money.text = itemList[position].jobMoney
        holder.term.text = itemList[position].jobTerm
        holder.sex.text = itemList[position].jobSex
        holder.nation.text = itemList[position].jobNation
        holder.update.text = itemList[position].updatedAt
        holder.age.text = itemList[position].jobAge
        holder.extra_text.text = itemList[position].jobExtratext

        holder.showButton.setOnClickListener {
            showButtonClickListener?.onShowButtonClick(itemList[position])
        }
        holder.applyButton.setOnClickListener {
            val loggedInEmail = getCurrentLoggedInEmail()

            if(loggedInEmail != null){
                getOtherInfoByEmail(loggedInEmail){resumeId ->
                    if(resumeId != null){
                        applyButtonClickListener?.onApplyButtonClick(
                            resumeId,
                            loggedInEmail,
                            itemList[position].email,
                            itemList[position].job_id,
                            itemList[position]
                        )

                    }else{
                        Log.e("check", "Unable to get resume ID")
                    }
                }
            }else{
                Log.e("check", "Unable to get logged-in email")

            }
          /*  applyButtonClickListener?.onApplyButtonClick(
                loggedInEmail,
                itemList[position].email,
                itemList[position].job_id,
                itemList[position]
            )*/
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    public fun setOnShowButtonClickListener(listener: OnShowButtonClickListener) {
        showButtonClickListener = listener
    }

    public interface OnShowButtonClickListener {
        fun onShowButtonClick(item: JobModel)
    }

    public fun setOnApplyButtonClickListener(listener: OnApplyButtonClickListener) {
        applyButtonClickListener = listener
    }

    public interface OnApplyButtonClickListener {
        fun onApplyButtonClick(
            resumeId: String?,
            email: String?,
            jobEmail: String?,
            jobId: String?,
            item:JobModel
        )
    }

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.store_name)
        val add_text: TextView = itemView.findViewById(R.id.store_text)
        val money: TextView = itemView.findViewById(R.id.money)
        val term: TextView = itemView.findViewById(R.id.work_term)
        val sex: TextView = itemView.findViewById(R.id.sexCondition)
        val nation: TextView = itemView.findViewById(R.id.nationCondition)
        var update: TextView = itemView.findViewById(R.id.create_at)
        var age: TextView = itemView.findViewById(R.id.ageCondition)
        var extra_text: TextView = itemView.findViewById(R.id.extra_text)

        val showButton: Button = itemView.findViewById(R.id.showbtn)
        val applyButton: Button = itemView.findViewById(R.id.applyToBtn)
    }

    //로그인된 이메일 가져오기
    private fun getCurrentLoggedInEmail(): String?{
        val user = Firebase.auth.currentUser
        return user?.email
    }

    //resume 컬렉션에서 현재 로그인된 이메일과 같은 이메일의 resume_id가져오기
    private fun getOtherInfoByEmail(email: String, callback: (String?) -> Unit){
        val db = Firebase.firestore
        val resumeCollection = db.collection("resume")
        val query = resumeCollection.whereEqualTo("email", email)

        query.get()
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    for(document in documents) {
                        val resumeId = document.getString("resume_id")
                        callback(resumeId)
                    }
                }else{
                    callback(null)

                }
            }
            .addOnFailureListener{exception ->
                callback(null)
            }

        }

}
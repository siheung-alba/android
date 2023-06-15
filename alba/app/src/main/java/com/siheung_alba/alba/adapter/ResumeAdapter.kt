package com.siheung_alba.alba.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.model.JobModel
import com.siheung_alba.alba.model.ResumeModel

class ResumeAdapter(var itemList: ArrayList<ResumeModel>) : RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {

    public var showButtonClickListener: OnShowButtonClickListenerResume? = null
   /* public var choiceButtonClickListener: OnChoiceButtonClickListener? = null

    private val resumeList: ArrayList<String> = ArrayList() // 이력서 ID를 저장할 리스트
    private val titleList: ArrayList<String> = ArrayList() // 이력서 ID를 저장할 리스트
    private var selectedResumeId: String? = null*/

    // 리사이클러뷰에서 열리는 아이템창 연결
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_page_user_item, parent, false)
        return ResumeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val item = itemList[position]

        holder.title.text = item.title
        holder.introduce.text = item.introduce
        holder.created.text = item.updated_at
        // 아이템 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            showButtonClickListener?.onShowButtonClick(item)
        }

        /*holder.choiceButton.setOnClickListener {
            val loggedInEmail = getCurrentLoggedInEmail()

            if (loggedInEmail != null) {
                getOtherInfoByEmail(loggedInEmail) { resumeIdList ->
                    resumeIdList?.let {
                        resumeList.clear()
                        resumeList.addAll(it)
                        notifyDataSetChanged()
                    }
                }
            } else {
                Log.e("check", "Unable to get logged-in email")
            }
        }*/
    }

   /* public fun setTitleList(list:ArrayList<ResumeModel>){
        titleList.clear()
        for(item in list){
            item.title?.let { titleList.add(it) }
        }
        notifyDataSetChanged()
    }
*/
    public interface OnShowButtonClickListenerResume {
        fun onShowButtonClick(item : ResumeModel)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    /*public fun setOnChoiceButtonClickListener(listener: OnChoiceButtonClickListener) {
        choiceButtonClickListener = listener
    }

    public interface OnChoiceButtonClickListener {
        fun onChoiceButtonClick(
            resumeId: String?
        )
    }
*/
    inner class ResumeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.resume_list_title)
        var introduce: TextView = itemView.findViewById(R.id.resume_list_text)
        var created: TextView = itemView.findViewById(R.id.resume_list_create)

        var choiceButton: Button = itemView.findViewById(R.id.choicebtn)

      /*  init {
            choiceButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedResumeId = resumeList[position] // 선택된 이력서의 resume_id
                    this@ResumeAdapter.selectedResumeId = selectedResumeId
                    choiceButtonClickListener?.onChoiceButtonClick(selectedResumeId)
                    notifyDataSetChanged()
                }
            }*/
        }
    }

       /* private fun getCurrentLoggedInEmail(): String? {
            val user = Firebase.auth.currentUser
            return user?.email
        }

        private fun getOtherInfoByEmail(
            email: String,
            callback: (ArrayList<String>?) -> Unit
        ) {
            val db = Firebase.firestore
            val resumeCollection = db.collection("resume")
            val query = resumeCollection.whereEqualTo("email", email)

            query.get()
                .addOnSuccessListener { documents ->
                    val resumeIdList: ArrayList<String> = ArrayList()
                    for (document in documents) {
                        val resumeId = document.getString("resume_id")
                        resumeId?.let {
                            resumeIdList.add(it)
                            titleList.add(it)
                        }
                    }
                    callback(resumeIdList)
                }
                .addOnFailureListener { exception ->
                    callback(null)
                    Log.e("check", "Unable to get resume ID", exception)
                }

        }

        fun setSelectedResumeId(resumeId: String?) {
            selectedResumeId = resumeId
            notifyDataSetChanged()
        }*/
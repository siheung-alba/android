package com.siheung_alba.alba.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.OwnerResumeHomeActivity
import com.siheung_alba.alba.activity.OwnerUploadActivity
import com.siheung_alba.alba.model.JobModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OwnerHomeAdapter(var itemList: ArrayList<JobModel>) : RecyclerView.Adapter<OwnerHomeAdapter.OwnerHomeViewHolder>() {
    /*private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private val colResumeOwnerRef = db.collection("job")
    private val adapter = OwnerHomeAdapter(itemList)
*/

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerHomeAdapter.OwnerHomeViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_page_for_owner_item_layout, parent, false)
        return OwnerHomeViewHolder(view)



    }

    override fun onBindViewHolder(holder: OwnerHomeViewHolder, position: Int) {





        holder.title.text = itemList[position].jobTitle  // 매장
        holder.add_text.text = itemList[position].jobAddtext  // 제목
        holder.money.text = itemList[position].jobMoney // 시급
        // 나이
        holder.term.text = itemList[position].jobTerm // 근무기간
        holder.sex.text = itemList[position].jobSex // 성별
        holder.nation.text = itemList[position].jobNation // 국적
        // 추가내용

        holder.update.text = itemList[position].updatedAt // 업데이트

        holder.ownereditbtn.setOnClickListener {
            /*auth = FirebaseAuth.getInstance()
            val user = auth.currentUser*/

/*
            val userEmail = user?.email*/

            // 클릭 시
            val intent = Intent(holder.ownereditbtn.context, OwnerResumeHomeActivity::class.java)

            intent.putExtra("titleEdit",itemList[position].jobTitle)  // 매장
            intent.putExtra("jobAddtextEdit",itemList[position].jobAddtext)  // 제목
            intent.putExtra("jobTermEdit",itemList[position].jobTerm)  // 근무기간
            intent.putExtra("jobMoneyEdit",itemList[position].jobMoney) //시급
            intent.putExtra("jobAgeEdit",itemList[position].jobAge) //나이
            intent.putExtra("jobSexEdit",itemList[position].jobSex) // 성별
            intent.putExtra("jobNationEdit",itemList[position].jobNation) // 국적
            intent.putExtra("jobExtratextEdit",itemList[position].jobExtratext) // 추가 내용
            intent.putExtra("job_id",itemList[position].job_id) // 추가 내용







            /*// Firestore에서 이력서 목록 가져오기
            colResumeOwnerRef
                .whereEqualTo("email", userEmail)
                .orderBy("updated_at", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    itemList.clear()  // 이력서 목록 초기화
                    for (document in result) {
                        val item = JobModel(
                            document.data["jobTitle"] as? String?,
                            document.data["jobAddtext"] as? String?,
                            document.data["jobTerm"] as? String?,
                            document.data["jobMoney"] as? String?,
                            document.data["jobNation"] as? String?,
                            document.data["jobSex"] as? String?,
                            document.data["updatedAt"] as? String?,
                            document.data["jobAge"] as? String?,
                            document.data["jobExtratext"] as? String?,
                            document.id  // 'job_id'에 document.id 값을 전달
                        )
                        itemList.add(item)  // 이력서 목록에 추가
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.w("MyPageForUserFragment", "Error getting documents: $exception")
                }*/


            holder.ownereditbtn.context.startActivity(intent)
            }




        }



        /*
        val item = itemList[position]
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.apply {
            bind(item)
        }

         */




    override fun getItemCount(): Int {
        return itemList.size
    }








    /*
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener : OnItemClickListener



    fun setItemClickListener(itemClickListener: OnItemClickListener) {
        this.itemClickListener = itemClickListener
    }

     */

    inner class OwnerHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.store_name)
        val add_text: TextView = itemView.findViewById(R.id.store_text)
        val money: TextView = itemView.findViewById(R.id.money)
        val term: TextView = itemView.findViewById(R.id.work_term)
        val sex: TextView = itemView.findViewById(R.id.sexCondition)
        val nation: TextView = itemView.findViewById(R.id.nationCondition)
        var update: TextView = itemView.findViewById(R.id.create_at)

        var ownereditbtn: TextView = itemView.findViewById(R.id.ownerEditbtn)







        /*
        fun bind(item: JobModel, onClickListener: OnClickListener) {
            title.text = item.jobTitle
            add_text.text = item.jobAddtext
            money.text = item.jobMoney
            term.text = item.jobTerm
            sex.text = item.jobSex
            nation.text = item.jobNation
            update.text = item.updatedAt
            itemView
        }

         */

    }
}
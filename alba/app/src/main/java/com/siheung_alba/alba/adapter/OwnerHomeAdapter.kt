package com.siheung_alba.alba.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.OwnerResumePageActivity
import com.siheung_alba.alba.model.JobModel

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
            val intent = Intent(holder.ownereditbtn.context, OwnerResumePageActivity::class.java)

            intent.putExtra("titleEdit",itemList[position].jobTitle)  // 매장
            intent.putExtra("jobAddtextEdit",itemList[position].jobAddtext)  // 제목
            intent.putExtra("jobTermEdit",itemList[position].jobTerm)  // 근무기간
            intent.putExtra("jobMoneyEdit",itemList[position].jobMoney) //시급

            intent.putExtra("workDayEdit",itemList[position].workDay) //근무요일
            intent.putExtra("workTimeStart",itemList[position].workTimeStart) //근무시간
            intent.putExtra("workTimeEnd",itemList[position].workTimeEnd) //근무시간

            intent.putExtra("jobAgeEdit",itemList[position].jobAge) //나이
            intent.putExtra("jobSexEdit",itemList[position].jobSex) // 성별

            intent.putExtra("educationEdit",itemList[position].education) //학력
            intent.putExtra("preferenceEdit",itemList[position].preference) //우대조건

            intent.putExtra("jobNationEdit",itemList[position].jobNation) // 국적
            intent.putExtra("jobExtratextEdit",itemList[position].jobExtratext) // 추가 내용

            intent.putExtra("workTimeStart",itemList[position].workTimeStart)
            intent.putExtra("workTimeEnd",itemList[position].workTimeEnd)
            intent.putExtra("job_id",itemList[position].job_id) // 추가 내용



            holder.ownereditbtn.context.startActivity(intent)
            }




        }





    override fun getItemCount(): Int {
        return itemList.size
    }




    inner class OwnerHomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.store_name)
        val add_text: TextView = itemView.findViewById(R.id.store_text)
        val money: TextView = itemView.findViewById(R.id.money)
        val term: TextView = itemView.findViewById(R.id.work_term)
        val sex: TextView = itemView.findViewById(R.id.sexCondition)
        val nation: TextView = itemView.findViewById(R.id.nationCondition)
        var update: TextView = itemView.findViewById(R.id.create_at)

        var ownereditbtn: TextView = itemView.findViewById(R.id.ownerEditbtn)






    }
}
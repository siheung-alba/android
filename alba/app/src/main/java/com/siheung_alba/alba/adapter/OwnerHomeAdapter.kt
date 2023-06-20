package com.siheung_alba.alba.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.OwnerUploadActivity
import com.siheung_alba.alba.model.JobModel

class OwnerHomeAdapter(var itemList: ArrayList<JobModel>) : RecyclerView.Adapter<OwnerHomeAdapter.OwnerHomeViewHolder>() {

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
            // 클릭 시
            val intent = Intent(holder.ownereditbtn.context, OwnerUploadActivity::class.java)

            intent.putExtra("titleEdit",itemList[position].jobTitle)  // 매장
            intent.putExtra("jobAddtextEdit",itemList[position].jobAddtext)  // 제목
            intent.putExtra("jobTermEdit",itemList[position].jobTerm)  // 근무기간
            intent.putExtra("jobMoneyEdit",itemList[position].jobMoney) //시급
            intent.putExtra("jobAgeEdit",itemList[position].jobAge) //나이
            intent.putExtra("jobSexEdit",itemList[position].jobSex) // 성별
            intent.putExtra("jobNationEdit",itemList[position].jobNation) // 국적
            intent.putExtra("jobExtratextEdit",itemList[position].jobExtratext) // 추가 내용



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
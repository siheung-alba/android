package com.siheung_alba.alba.model

import com.siheung_alba.alba.activity.PopupActivity
import android.content.Intent
import android.util.Log
import com.siheung_alba.alba.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siheung_alba.alba.fragment.HomeFragment


class JobAdapter(var itemList: ArrayList<JobModel>) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    public var showButtonClickListener: OnShowButtonClickListener? = null

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
    }
}
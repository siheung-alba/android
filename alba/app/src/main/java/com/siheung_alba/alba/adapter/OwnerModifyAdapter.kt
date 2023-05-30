
package com.siheung_alba.alba.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siheung_alba.alba.R
import com.siheung_alba.alba.model.JobModel

class OwnerModifyAdapter(var itemList: ArrayList<JobModel>) : RecyclerView.Adapter<OwnerModifyAdapter.OwnerModifyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OwnerModifyAdapter.OwnerModifyViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_page_for_owner_item_layout, parent, false)
        return OwnerModifyViewHolder(view)
    }

    override fun onBindViewHolder(holder: OwnerModifyViewHolder, position: Int) {

        holder.title.text = itemList[position].jobTitle
        holder.add_text.text = itemList[position].jobAddtext
        holder.money.text = itemList[position].jobMoney
        holder.term.text = itemList[position].jobTerm
        holder.sex.text = itemList[position].jobSex
        holder.nation.text = itemList[position].jobNation
        holder.update.text = itemList[position].updatedAt

        val item = itemList[position]
        val adapter = this
        holder.apply {
            bind(item,View.OnClickListener {
                adapter.notifyDataSetChanged()
            })
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class OwnerModifyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.store_name)
        val add_text: TextView = itemView.findViewById(R.id.store_text)
        val money: TextView = itemView.findViewById(R.id.money)
        val term: TextView = itemView.findViewById(R.id.work_term)
        val sex: TextView = itemView.findViewById(R.id.sexCondition)
        val nation: TextView = itemView.findViewById(R.id.nationCondition)
        var update: TextView = itemView.findViewById(R.id.create_at)

        fun bind(item : JobModel, onClickListener: View.OnClickListener) {
            title.text = item.jobTitle
            add_text.text = item.jobAddtext
            money.text = item.jobMoney
            term.text = item.jobTerm
            sex.text = item.jobSex
            nation.text = item.jobNation
            update.text = item.updatedAt
        }

    }


}


package com.siheung_alba.alba.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.siheung_alba.alba.R
import com.siheung_alba.alba.model.ResumeModel

class ResumeAdapter(var itemList: ArrayList<ResumeModel>) : RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {

    public var showButtonClickListener: OnShowButtonClickListenerResume? = null

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
    }


    public interface OnShowButtonClickListenerResume {
        fun onShowButtonClick(item : ResumeModel)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ResumeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var title: TextView = itemView.findViewById(R.id.resume_list_title)
        var introduce : TextView = itemView.findViewById(R.id.resume_list_text)
        var created: TextView = itemView.findViewById(R.id.resume_list_create)
    }


}
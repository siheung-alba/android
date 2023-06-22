package com.siheung_alba.alba.adapter

import android.content.Context
import android.content.Intent
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
import com.siheung_alba.alba.activity.OwnerResumeShowActivity
import com.siheung_alba.alba.model.Applicant
import com.siheung_alba.alba.model.ApplyModel
import com.siheung_alba.alba.model.JobModel
import org.w3c.dom.Text


// 알림창 리스트
class ApplicantAdapter(private val context: Context, private val itemList :ArrayList<Applicant>):
RecyclerView.Adapter<ApplicantAdapter.ApplicantViewHolder>() {

    class ApplicantViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val applicantEmail: TextView = itemView.findViewById(R.id.applicantEmail)
        val showResumeBtn: Button = itemView.findViewById(R.id.showResume)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantViewHolder {

        val view: View = LayoutInflater.from(context).inflate(R.layout.applicant_layout, parent, false)

        return ApplicantViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplicantViewHolder, position: Int) {
        holder.applicantEmail.text = itemList[position].applicant + "님이 지원하였습니다."

        holder.showResumeBtn.setOnClickListener {
            val intent = Intent(context, OwnerResumeShowActivity::class.java)
            intent.putExtra("title", itemList[position].title)
            intent.putExtra("name", itemList[position].applicantName)
            intent.putExtra("phone", itemList[position].applicantPhone)
            intent.putExtra("email", itemList[position].applicant)
            intent.putExtra("age", itemList[position].age)
            intent.putExtra("sex", itemList[position].sex)
            intent.putExtra("nation", itemList[position].nation)
            intent.putExtra("career", itemList[position].career)
            intent.putExtra("introduce", itemList[position].introduce)

            intent.run { context.startActivity(this) }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}
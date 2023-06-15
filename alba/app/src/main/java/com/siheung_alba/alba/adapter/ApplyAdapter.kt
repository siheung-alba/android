package com.siheung_alba.alba.adapter

import android.content.Intent
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
import com.siheung_alba.alba.activity.OwnerResumeShowActivity
import com.siheung_alba.alba.model.ApplyModel
import com.siheung_alba.alba.model.JobModel
import org.w3c.dom.Text
class ApplyAdapter(private var itemList: ArrayList<ApplyModel>) : RecyclerView.Adapter<ApplyAdapter.ApplyViewHolder>() {

    public var DetailResumeClickListener : OnDetailResumeClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.owner_resume_item_layout, parent, false)
        return ApplyViewHolder(view)
    }

    override fun onBindViewHolder(holder: ApplyViewHolder, position: Int) {
        val applyModel = itemList[position]
        holder.applicant.text = applyModel.applicant
        holder.owner.text = applyModel.owner
        holder.job_id.text = applyModel.job_id
        holder.resume_id.text = applyModel.resume_id

        val loggedInEmail = getCurrentLoggedInEmail()

        val db = Firebase.firestore
        val applyCollection = db.collection("apply")
        val query = applyCollection.whereEqualTo("owner", loggedInEmail)

        query.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        if (applyModel.owner == loggedInEmail) {
                            getResumeTitle(applyModel.resume_id) { Resumetitle ->
                                holder.resume_title.text = Resumetitle
                                Log.e("check", "resume_title:${Resumetitle}")

                            }
                            getJobTitle(applyModel.job_id) { Jobtitle ->
                                holder.job_title.text = Jobtitle
                                Log.e("check", "job_title:${Jobtitle}")

                            }
                            Log.e("check", "resume_id:${applyModel.resume_id}")
                            Log.e("check", "job_id:${applyModel.job_id}")
                        }
                        else{
                            holder.itemView.layoutParams.height = 0
                            holder.itemView.visibility = View.GONE
                        }
                    }
                } else {
                    holder.itemView.layoutParams.height = 0
                    holder.itemView.visibility = View.GONE

                }
            }
            .addOnFailureListener { exception ->
                holder.itemView.layoutParams.height = 0
                holder.itemView.visibility = View.GONE
            }

        holder.detail_resume.setOnClickListener{
            DetailResumeClickListener?.onDetailResumeClick(itemList[position])
        }
    }

    public fun setOnDetailResumeClickListener(listener: OnDetailResumeClickListener) {
       DetailResumeClickListener = listener
    }

    public interface OnDetailResumeClickListener {
        fun onDetailResumeClick(item: ApplyModel)
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ApplyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val applicant: TextView = itemView.findViewById(R.id.applicant)
        val owner: TextView = itemView.findViewById(R.id.owner)
        val job_id: TextView = itemView.findViewById(R.id.job_id)
        val resume_id: TextView = itemView.findViewById(R.id.resume_id)
        val resume_title: TextView = itemView.findViewById(R.id.resume_title)
        val job_title: TextView = itemView.findViewById(R.id.store_name)

        val detail_resume: Button = itemView.findViewById(R.id.detail_resume)
    }

    private fun getCurrentLoggedInEmail(): String? {
        val user = Firebase.auth.currentUser
        return user?.email
    }

    private fun getResumeTitle(resume_id: String?, callback: (String?) -> Unit) {
        if (resume_id != null) {
            val db = Firebase.firestore
            val resumeCollection = db.collection("resume")
            val query = resumeCollection.whereEqualTo("resume_id", resume_id)

            query.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        val Resumetitle = document.getString("title")

                        callback(Resumetitle)
                    } else {
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    callback(null)
                }
        } else {
            callback(null)
        }
    }

    private fun getJobTitle(job_id: String?, callback: (String?) -> Unit) {
        if (job_id != null) {
            val db = Firebase.firestore
            val jobCollection = db.collection("job")
            val query = jobCollection.whereEqualTo("job_id", job_id)

            query.get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val document = documents.documents[0]
                        val Jobtitle = document.getString("title")
                        callback(Jobtitle)
                    } else {
                        callback(null)
                    }
                }
                .addOnFailureListener { exception ->
                    callback(null)
                }
        } else {
            callback(null)
        }
    }
}

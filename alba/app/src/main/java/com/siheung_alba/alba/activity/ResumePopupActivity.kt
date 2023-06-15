package com.siheung_alba.alba.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.adapter.ResumeAdapter
import com.siheung_alba.alba.model.ResumeModel

class ResumePopupActivity : AppCompatActivity() {
    private val db = Firebase.firestore

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ResumeAdapter

    private var title: String? = null
    private var email: String? = null
    private var jobId: String? = null
    private var jobEmail: String? = null
    private var resumeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_popup)

        val window = window
        window?.setGravity(Gravity.CENTER)


        /* val intent = intent
        val title = intent.getStringExtra("title")
        val email = intent.getStringExtra("email")
        val jobId = intent.getStringExtra("jobId")
        val jobEmail = intent.getStringExtra("jobEmail")

        val resumeId = intent.getStringExtra("resumeId")

        recyclerView = findViewById(R.id.resume_list)
        adapter = ResumeAdapter((ArrayList()))
        recyclerView.adapter = adapter

        showResumeList(adapter.setTitleList())

        saveApplyData( email, jobEmail, jobId, resumeId)

        fun onChoiceButtonClick(view: View) {
            if (resumeId != null) {
                val intent = Intent(this, PopupActivity::class.java)
                intent.putExtra("title", title)
                intent.putExtra("email", email)
                intent.putExtra("jobId", jobId)
                intent.putExtra("jobEmail", jobEmail)
                intent.putExtra("resumeId", resumeId)
                startActivity(intent)
            }
        }
    }
    private fun showResumeList(list: ArrayList<ResumeModel>){
        adapter.setTitleList(list)
    }

    fun mOnClose(view: View) {
        finish()
    }

    private fun saveApplyData(
        resumeId: String?,
        email: String?,
        jobEmail: String?,
        jobId: String?
    ) {
        val applyDate = hashMapOf(
            "resume_id" to resumeId,
            "applicant" to email,
            "job_id" to jobId,
            "owner" to jobEmail
        )
        val applyCollection = db.collection("apply")
        applyCollection.add(applyDate)
            .addOnSuccessListener { documentReference ->
                Log.e("apply", "Apply data saved with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("apply", "Error saving apply data", e)
            }

    }*/
    }
}

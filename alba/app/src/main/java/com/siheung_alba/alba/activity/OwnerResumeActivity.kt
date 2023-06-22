package com.siheung_alba.alba.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.adapter.ApplyAdapter
import com.siheung_alba.alba.adapter.ApplicantAdapter
import com.siheung_alba.alba.databinding.ActivityOwnerResumeBinding
import com.siheung_alba.alba.model.Applicant
import com.siheung_alba.alba.model.ApplyModel

class OwnerResumeActivity : AppCompatActivity() {


    private val binding by lazy { ActivityOwnerResumeBinding.inflate(layoutInflater) }

    private val itemList = arrayListOf<Applicant>()
    private lateinit var adapter: ApplicantAdapter

    private var mAuth = Firebase.auth
    private var db = Firebase.firestore

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val toolbar: Toolbar = binding.ownerResume
        toolbar.title = "지원된 이력서"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        adapter = ApplicantAdapter(this, itemList)

        binding.applicantList.layoutManager = LinearLayoutManager(this)
        binding.applicantList.adapter = adapter

        val currentUser = mAuth.currentUser?.email

        db.collection("apply")
            .whereEqualTo("owner", currentUser)
            .get()
            .addOnSuccessListener { applyDocuments ->
                itemList.clear()
                for (applyDocument in applyDocuments) {

                    db.collection("resume")
                        .whereEqualTo("resume_id", applyDocument.data["resume_id"].toString())
                        .get()
                        .addOnSuccessListener { resumeDocuments ->
                            for (resumeDocument in resumeDocuments) {

                                db.collection("user")
                                    .whereEqualTo("email", resumeDocument.data["email"].toString())
                                    .get()
                                    .addOnSuccessListener { userDocuments ->
                                        for (userDocument in userDocuments) {

                                            val item = Applicant(
                                                resumeDocument.data["introduce"].toString(),
                                                resumeDocument.data["career"].toString(),
                                                resumeDocument.data["title"].toString(),
                                                resumeDocument.data["updated_at"].toString(),
                                                userDocument.data["birthday"].toString(),
                                                userDocument.data["sex"].toString(),
                                                userDocument.data["nation"].toString(),
                                                userDocument.data["email"].toString(),
                                                resumeDocument.data["resume_id"].toString(),
                                                applyDocument.data["job_id"].toString(),
                                                applyDocument.data["owner"].toString(),
                                                userDocument.data["name"].toString(),
                                                userDocument.data["phone"].toString()
                                            )
                                            itemList.add(item)
                                        }
                                        adapter.notifyDataSetChanged()
                                    }
                            }

                        }

                }
            }
            .addOnFailureListener {
                Log.w("error : ", "document cannot be accessed!")
            }

    }
}

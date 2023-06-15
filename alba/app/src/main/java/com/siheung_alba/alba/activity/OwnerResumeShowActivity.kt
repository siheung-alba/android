package com.siheung_alba.alba.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.databinding.ActivityOwnerResumeShowBinding

class OwnerResumeShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOwnerResumeShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOwnerResumeShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resume_id = intent.getStringExtra("resume_id")
        val email = intent.getStringExtra("applicant")

        Log.e("check", "00000->resume_id:${resume_id}")
        Log.e("check", "1111->email:${email}")

        val db = Firebase.firestore
        val resumeCollection = db.collection("resume")
        val query = resumeCollection.whereEqualTo("resume_id", resume_id)

        query.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val title = document.getString("title")
                    val career = document.getString("career")
                    val introduce = document.getString("introduce")

                    binding.title.text = title
                    binding.career.text = career
                    binding.introduce.text = introduce

                    Log.e("check", "resume_id:${resume_id}, title:${title}, career:${career}, introduce:${introduce}")
                    }

                    else {
                        Log.e("check", "1->resume_id:${resume_id}")

                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("check", "2->resume_id:${resume_id}")
                }

        val userCollection = db.collection("user")
        val user_query = userCollection.whereEqualTo("email", email)

        user_query.get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val document = documents.documents[0]
                    val name = document.getString("name")
                    val age = document.getString("age")
                    val nation = document.getString("nation")
                    val sex = document.getString("sex")
                    val phone = document.getString("phone")

                    binding.name.text = name
                    binding.age.text = age
                    binding.nation.text = nation
                    binding.sex.text = sex
                    binding.phone.text = phone
                    binding.email.text = email

                    Log.e("check", "email:${email}, name:${name}, nation:${nation}, sex:${sex}")
                }

                else {
                    Log.e("check", "3->email:${email}")

                }
            }
            .addOnFailureListener { exception ->
                Log.e("check","4->email:${email}")
            }

        binding.check.setOnClickListener {
            val intent = Intent(this, OwnerResumeActivity::class.java)
            startActivity(intent)
        }
    }
}
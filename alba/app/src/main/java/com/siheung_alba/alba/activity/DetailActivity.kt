package com.siheung_alba.alba.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.adapter.JobAdapter
import com.siheung_alba.alba.databinding.ActivityDetailBinding
import com.siheung_alba.alba.fragment.HomeFragment
import com.siheung_alba.alba.model.JobModel

class DetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }

    private val db = Firebase.firestore

    private lateinit var jobId: String
    private lateinit var ownerEmail: String
    private lateinit var applicantEmail: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val intent = intent

        jobId = intent.getStringExtra("jobId").toString()

        binding.Title.text = intent.getStringExtra("title")
        binding.Addtext.text = intent.getStringExtra("addtext")
        binding.Money.text = intent.getStringExtra("money")
        binding.Term.text = intent.getStringExtra("term")
        binding.Sex.text = intent.getStringExtra("sex")
        binding.Nation.text = intent.getStringExtra("nation")
        binding.Age.text = intent.getStringExtra("age")
        binding.Extratext.text = intent.getStringExtra("extratext")
        binding.WorkDay.text = intent.getStringExtra("workday")
        binding.WorkTime.text = intent.getStringExtra("worktime")
        binding.Preference.text = intent.getStringExtra("preference")
        binding.Education.text = intent.getStringExtra("education")
        binding.OwnerName.text = intent.getStringExtra("ownername")
        binding.OwnerPhone.text = intent.getStringExtra("ownerphone")

        // 사장님 이메일
        ownerEmail = intent.getStringExtra("ownerEmail").toString()

        // 현재 이용자 이메일
        val userEmail = Firebase.auth.currentUser
        applicantEmail = userEmail?.email.toString()


        // 지원 하기 -> 채용자에게 알림 + db에 데이터 값 넣기
        binding.applyBtn.setOnClickListener {

            getResumeId(applicantEmail){resumeId ->

                if (resumeId != null) {
                    val applyData = hashMapOf(
                        "resume_id" to resumeId,
                        "applicant" to applicantEmail,
                        "job_id" to jobId,
                        "owner" to ownerEmail
                    )

                    db.collection("apply")
                        .add(applyData)
                        .addOnSuccessListener {result ->
                            val intent = Intent(this, PopupActivity::class.java)
                            intent.putExtra("text", "지원이 완료되었습니다.")
                            startActivity(intent)
                            Log.e("success : ", "success to access documents : $result")
                        }
                        .addOnFailureListener {
                            Log.e("error : ", "fail to access documents")
                        }
                }else {
                    val intent = Intent(this, PopupActivity::class.java)
                    intent.putExtra("text", "작성된 이력서가 없습니다.")
                    startActivity(intent)
                }


            }
        }

        binding.closeBtn.setOnClickListener {
            finish()
        }
    }

    private fun getResumeId(currentUser: String, callback: (String?) -> Unit){
        val db = Firebase.firestore
        val resumeCollection = db.collection("resume")
        val query = resumeCollection.whereEqualTo("email", currentUser)

        query.get()
            .addOnSuccessListener { documents ->
                if(!documents.isEmpty){
                    for(document in documents) {
                        val resumeId = document.getString("resume_id")
                        callback(resumeId)
                    }
                }else{
                    callback(null)

                }
            }
            .addOnFailureListener{
                callback(null)
            }

    }

    fun mOnClose(view: View) {
        finish()
    }
}
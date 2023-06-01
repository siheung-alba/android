package com.siheung_alba.alba.user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityResumeUploadBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

// 이력서 작성하기 페이지
class ResumeUploadActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_upload)

        val toolbar: Toolbar = findViewById(R.id.resumeUploadToolbar)
        toolbar.title = "이력서 작성하기"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

//        val userImg : ImgButton = findViewById(R.id.resume_img)  // 유저 이미지 업로드 버튼
        val title : EditText = findViewById(R.id.resume_title)  // 이력서 제목
        val career : EditText = findViewById(R.id.resume_career) // 경력
        val intro : EditText = findViewById(R.id.resume_introduce) // 자기소개서
        val done_btn: Button = findViewById(R.id.resume_done_btn)


        done_btn.setOnClickListener {

            // 현재 로그인한 사용자의 정보 가져오기
            val user = auth.currentUser

            if (user != null) {
                val email = user.email

                val data = hashMapOf(
                    "title" to title.text.toString(),
                    "career" to career.text.toString(),
                    "introduce" to intro.text.toString(),
                    "email" to email,
                    "created_at" to formatted,
                    "updated_at" to formatted
                )


                db.collection("resume")
                    .add(data)
                    .addOnSuccessListener {
                        Toast.makeText(this, "이력서 작성이 완료되었습니다", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        // 실패할 경우
                        android.util.Log.w(
                            "ResumeUploadActivity",
                            "Error getting documents: $exception"
                        )

                    }
            }
        }


    }

}
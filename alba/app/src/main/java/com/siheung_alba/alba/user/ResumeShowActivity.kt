package com.siheung_alba.alba.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// 이력서 보여주기 페이지 (수정, 삭제)
class ResumeShowActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_show)

        val toolbar: Toolbar = findViewById(R.id.resumeShowToolbar)
        toolbar.title = "상세보기"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        // 이력서 정보 가져와서 화면에 표시하는 로직 구현
        val resumeId = intent.getStringExtra("resumeId")

        // TODO: 이력서 정보를 사용하여 화면에 표시하는 로직 작성

        // 수정 버튼 클릭 시 이력서 수정 페이지로 이동
        // TODO: 수정 버튼 클릭 시 이력서 수정 페이지로 이동하는 코드 작성

        // 삭제 버튼 클릭 시 이력서 삭제 로직 구현
        // TODO: 삭제 버튼 클릭 시 이력서 삭제 로직 작성
    }
}

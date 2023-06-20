package com.siheung_alba.alba.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OwnerUploadActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_upload)

        val toolbar: Toolbar = findViewById(R.id.resumeUploadToolbar)
        toolbar.title = "채용공고 올리기"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val user = Firebase.auth.currentUser

        val userEmail = user?.email
        val storeTitle : EditText = findViewById(R.id.edt_store)  // 매장
        val term : EditText = findViewById(R.id.edtTerm)  // 근무기간
        val money : EditText = findViewById(R.id.edtMoney) // 시급
        val age : EditText = findViewById(R.id.edtAge) // 나이
        val sex : EditText = findViewById(R.id.edtSex) // 성별
        val nation : EditText = findViewById(R.id.edtNation) // 국적
        val addText : EditText = findViewById(R.id.edt_add_text) // 제목
        val detail : EditText = findViewById(R.id.edt_detail) // 추가내용
        val btnPostJob: Button = findViewById(R.id.btnPostJob)


        // 수정 받아오기

        val  titleEdit = intent.getStringExtra("titleEdit") // 매장
        val  jobAddtextEdit = intent.getStringExtra("jobAddtextEdit") // 제목
        val  jobTermEdit = intent.getStringExtra("jobTermEdit") // 근무기간
        val  jobMoneyEdit = intent.getStringExtra("jobMoneyEdit") // 시급
        val  jobAgeEdit = intent.getStringExtra("jobAgeEdit")  // 나이
        val  jobSexEdit = intent.getStringExtra("jobSexEdit") // 성별
        val  jobNationEdit = intent.getStringExtra("jobNationEdit") // 국적
        val  jobExtratextEdit = intent.getStringExtra("jobExtratextEdit") //추가 내용

        storeTitle.setText(titleEdit)
        addText.setText(jobAddtextEdit)
        term.setText(jobTermEdit)
        money.setText(jobMoneyEdit)
        age.setText(jobAgeEdit)
        sex.setText(jobSexEdit)
        nation.setText(jobNationEdit)
        detail.setText(jobExtratextEdit)


        btnPostJob.setOnClickListener {
            val data = hashMapOf(
                "title" to storeTitle.text.toString(),
                "term" to term.text.toString(),
                "money" to money.text.toString(),
                "age" to age.text.toString(),
                "sex" to sex.text.toString(),
                "nation" to nation.text.toString(),
                "add_text" to addText.text.toString(),
                "extra_text" to detail.text.toString(),
                "email" to userEmail,
                "created_at" to formatted,
                "updated_at" to formatted

            )


            db.collection("job")
                .add(data)
                .addOnSuccessListener {

                    Toast.makeText(this, "데이터가 추가되었습니다", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    // 실패할 경우
                    android.util.Log.w("OwnerUploadActivity", "Error getting documents: $exception")
                }

            finish()
        }

    }
}
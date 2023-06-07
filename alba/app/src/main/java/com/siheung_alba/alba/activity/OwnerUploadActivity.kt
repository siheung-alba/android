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
        val storeTitle : EditText = findViewById(R.id.edt_store)
        val term : EditText = findViewById(R.id.edtTerm)
        val money : EditText = findViewById(R.id.edtMoney)
        val age : EditText = findViewById(R.id.edtAge)
        val sex : EditText = findViewById(R.id.edtSex)
        val nation : EditText = findViewById(R.id.edtNation)
        val addText : EditText = findViewById(R.id.edt_add_text)
        val detail : EditText = findViewById(R.id.edt_detail)
        val btnPostJob: Button = findViewById(R.id.btnPostJob)

        btnPostJob.setOnClickListener {
            val data = hashMapOf(
                "title" to storeTitle.text.toString(),
                "term" to term.text.toString(),
                "money" to money.text.toString(),
                "age" to age.text.toString(),
                "sex" to sex.text.toString(),
                "nation" to nation.text.toString(),
                "add_text" to addText.text.toString(),
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
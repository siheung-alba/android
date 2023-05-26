package com.siheung_alba.alba.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.fragment.MyPageForOwnerFragment
import com.siheung_alba.alba.model.JobModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

class OwnerUploadActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val user_id = "apple" // 유저 아이디
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_upload)

        val toolbar: Toolbar = findViewById(R.id.resumeUploadToolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val title : EditText = findViewById(R.id.edtTitle)
        val term : EditText = findViewById(R.id.edtTerm)
        val money : EditText = findViewById(R.id.edtMoney)
        val age : EditText = findViewById(R.id.edtAge)
        val sex : EditText = findViewById(R.id.edtSex)
        val nation : EditText = findViewById(R.id.edtNation)
        val addText : EditText = findViewById(R.id.edt_add_text)
        val btnPostJob: Button = findViewById(R.id.btnPostJob)

        btnPostJob.setOnClickListener {
            val data = hashMapOf(
                "title" to title.text.toString(),
                "term" to term.text.toString(),
                "money" to money.text.toString(),
                "age" to age.text.toString(),
                "sex" to sex.text.toString(),
                "nation" to nation.text.toString(),
                "add_text" to addText.text.toString(),
                "user_id" to user_id,
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

        }

    }
}
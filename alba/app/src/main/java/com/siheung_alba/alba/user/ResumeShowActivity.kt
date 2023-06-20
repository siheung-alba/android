package com.siheung_alba.alba.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    private val colResumeRef = db.collection("resume")


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
        val user = auth.currentUser
        val intent = intent

        val changeBtn: Button = findViewById(R.id.change_resume)
        val deleteBtn : Button = findViewById(R.id.delete_resume)

        // 이력서 정보를 받아오기 위해 인텐트에서 데이터 추출
        val resumeId = intent.getStringExtra("resume_id")
        Log.d("ResumeShowActivity", "resume_id: $resumeId")

        val resumeTitle = intent.getStringExtra("title")
        Log.d("ResumeShowActivity", "title: $resumeTitle")

        val resumeCareer = intent.getStringExtra("career")
        Log.d("ResumeShowActivity", "career: $resumeCareer")

        val resumeIntroduce = intent.getStringExtra("introduce")
        Log.d("ResumeShowActivity", "introduce: $resumeIntroduce")

        val receivedUserName = intent.getStringExtra("userName")
        Log.d("ResumeShowActivity", "userName: $receivedUserName")

        val receivedUserSex = intent.getStringExtra("userSex")
        val receivedUserAge = intent.getStringExtra("userAge")
        val receivedUserNation = intent.getStringExtra("userNation")

        // 이력서 정보를 화면에 표시
        val titleEditText: EditText = findViewById(R.id.resume_show_title)
        val careerEditText: EditText = findViewById(R.id.resume_show_career)
        val introEditText: EditText = findViewById(R.id.resume_show_introduce)
        val userNameTextView : TextView = findViewById(R.id.resume_show_text1) // 이름
        val userSexTextView : TextView = findViewById(R.id.resume_show_text2) // 성별
        val userAgeTextView : TextView = findViewById(R.id.resume_show_text3) // 나이
        val userNationTextView : TextView = findViewById(R.id.resume_show_text4) // 국적

        titleEditText.setText(resumeTitle)
        careerEditText.setText(resumeCareer)
        introEditText.setText(resumeIntroduce)
        userNameTextView.text = receivedUserName
        userSexTextView.text = receivedUserSex
        userAgeTextView.text = receivedUserAge
        userNationTextView.text = receivedUserNation


        // TODO: 이력서 정보를 사용하여 화면에 표시하는 로직 작성

        changeBtn.setOnClickListener {
            // 수정된 내용을 저장
            val editedTitle = titleEditText.text.toString()
            val editedCareer = careerEditText.text.toString()
            val editedIntroduce = introEditText.text.toString()


            val resumeData = hashMapOf<String, Any>(
                "title" to editedTitle,
                "career" to editedCareer,
                "introduce" to editedIntroduce,
                "updated_at" to formatted
            )

//            val resumeId = resume.resumeId

            db.collection("resume")
                .document(resumeId!!)
                .update(resumeData)
                .addOnSuccessListener {
                    Toast.makeText(this, "이력서가 성공적으로 업데이트되었습니다", Toast.LENGTH_SHORT).show()
//                    val resultIntent = Intent()
//                    resultIntent.putExtra("editedTitle", editedTitle)
//                    resultIntent.putExtra("editedCareer", editedCareer)
//                    resultIntent.putExtra("editedIntroduce", editedIntroduce)
//                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("ResumeShowActivity", "Error updating resume: $exception")
                    Toast.makeText(this, "이력서 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show()
                }
        }


        // TODO: 삭제 버튼 클릭 시 이력서 삭제 로직 작성
        deleteBtn.setOnClickListener {

            // Firestore에서 해당 이력서 ID에 해당하는 문서 삭제
            db.collection("resume")
                .document(resumeId!!)
                .delete()
                .addOnSuccessListener {
                    // 삭제 성공
                    Toast.makeText(this, "이력서가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    finish() // Activity 종료
                }
                .addOnFailureListener { e ->
                    // 삭제 실패
                    Toast.makeText(this, "이력서 삭제에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    Log.e("ResumeShowActivity", "Failed to delete resume: $e")
                }
        }




    }
}

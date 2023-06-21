package com.siheung_alba.alba.user

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.nl.translate.Translator
import com.siheung_alba.alba.R
//import com.siheung_alba.alba.databinding.ActivityResumeUploadBinding
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

    private lateinit var translator: Translator

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
        val trans_btn : Button = findViewById(R.id.resume_trans)

        val userNameTextView : TextView = findViewById(R.id.resume_upload_text1) // 이름
        val userName : String = userNameTextView.text.toString()
        val userSexTextView : TextView = findViewById(R.id.resume_upload_text2) // 성별
        val userSex : String = userSexTextView.text.toString()
        val userAgeTextView : TextView = findViewById(R.id.resume_upload_text3) // 나이
        val userAge : String = userAgeTextView.text.toString()
        val userNationTextView : TextView = findViewById(R.id.resume_upload_text4) // 국적
        val userNation : String = userNationTextView.text.toString()

        val intent = intent
        val receivedUserName = intent.getStringExtra("userName")
        val receivedUserSex = intent.getStringExtra("userSex")
        val receivedUserAge = intent.getStringExtra("userAge")
        val receivedUserNation = intent.getStringExtra("userNation")

        userNameTextView.text = receivedUserName
        userSexTextView.text = receivedUserSex
        userAgeTextView.text = receivedUserAge
        userNationTextView.text = receivedUserNation


        // 이력서 작성 완료하기 버튼
        done_btn.setOnClickListener {

            // 현재 로그인한 사용자의 정보 가져오기
            val user = auth.currentUser

            if (user != null) {
                val email = user.email
                // 7자리의 랜덤 숫자 생성
                val resumeId = generateRandomNumber()

                val data = hashMapOf(
                    "resume_id" to resumeId,
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


        // 국적을 언어 코드로 변환
        val userLanguageCode = if (receivedUserNation != null) {
            getLanguageCodeFromNation(receivedUserNation)
        } else {
            "ko" // 기본값으로 한국어 설정
        }

        // 번역여부 활성화 여부
        trans_btn.isEnabled = userLanguageCode != "ko"

        // 번역하기 { 사용자의 국적 -> 한국어 }
        trans_btn.setOnClickListener {
            val sourceLanguageCode = "ko"
            val targetLanguageCode = getLanguageCodeFromNation(userLanguageCode)

            translateText(title.text.toString(), sourceLanguageCode, targetLanguageCode) { translatedText ->
                title.setText(translatedText)
            }

            translateText(career.text.toString(), sourceLanguageCode, targetLanguageCode) { translatedText ->
                career.setText(translatedText)
            }

            translateText(intro.text.toString(), sourceLanguageCode, targetLanguageCode) { translatedText ->
                intro.setText(translatedText)
            }
        }


        // 다운로드 조건 설정
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        val targetLanguageCode = userLanguageCode






    }



    // 이력서id 랜덤 생성
    private fun generateRandomNumber(): String? {
        val random = Random()
        val randomNumber = StringBuilder(10)
        for (i in 0 until 10) {
            val digit = random.nextInt(10)
            randomNumber.append(digit)
        }
        return randomNumber.toString()
    }


    // 번역 함수
    private fun translateText(
        text: String,
        sourceLanguageCode: String,
        targetLanguageCode: String,
        completion: (String) -> Unit
    ) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguageCode)
            .setTargetLanguage(targetLanguageCode)
            .build()
        val translator = Translation.getClient(options)

        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                // 번역된 텍스트 반환
                completion(translatedText)
            }
            .addOnFailureListener { exception ->
                // 번역 실패 처리
                Log.e("ResumeUploadActivity", "Translation failed: $exception")
            }
    }




    // 국적을 언어 코드로 변환하는 함수
//    private fun getLanguageCodeFromNation(nation: String): String {
//        return when (nation) {
//            "대한민국" -> "ko"
//            "미국" -> "en"
//            "중국" -> "zh"
//            "베트남" -> "vi"
//            "인도네시아" -> "id"
//            "필리핀" -> "fil"
//            "캄보디아" -> "km"
//            "네팔" -> "ne"
//            "우즈베키스탄" -> "uz"
//            else -> "ko" // 기본값으로 한국어 설정
//        }
//    }

    // 국적을 언어코드로 변환하는 함수
    private fun getLanguageCodeFromNation(nation: String): String {
        return when (nation) {
            "대한민국" -> "ko"
            "미국" -> "en"
            else -> "ko" // 기본값으로 한국어 설정
        }
    }


}
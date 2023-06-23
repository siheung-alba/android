package com.siheung_alba.alba.activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityJoinBinding
import com.siheung_alba.alba.databinding.ActivityOwnerUploadBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OwnerUploadActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    private val binding by lazy { ActivityOwnerUploadBinding.inflate(layoutInflater) }

    private lateinit var term : String // 근무 기간
    private lateinit var workTime : String // 근무 시간
    private lateinit var workDay : String // 근무 요일
    private lateinit var title : String // 매장
    private lateinit var money : String // 시급
    private lateinit var addText : String // 제목
    private lateinit var extraText : String // 상세설명
    private lateinit var age : String
    private lateinit var sex : String // 성별
    private lateinit var education : String // 학력
    private lateinit var preference : String // 우대조건
    private lateinit var nation : String

    private lateinit var ownerName : String // 채용자 이름
    private lateinit var ownerPhone : String // 채용자 전화번호



    private lateinit var latitude : String
    private lateinit var longitude : String

    var noBlank = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.resumeUploadToolbar)
        toolbar.title = "채용공고 올리기"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val user = Firebase.auth.currentUser

        val userEmail = user?.email

        // 근무 기간 스피너
        var workTermData = resources.getStringArray(R.array.work_term_array)
        var workTermAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workTermData)
        binding.workTermSpinner.adapter = workTermAdapter


        // 근무 시간 스피너
        var startTimeData = resources.getStringArray(R.array.start_time_array)
        var startTimeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, startTimeData)
        binding.startTimeSpinner.adapter = startTimeAdapter

        var endTimeData = resources.getStringArray(R.array.end_time_array)
        var endTimeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, endTimeData)
        binding.endTimeSpinner.adapter = endTimeAdapter

        // 성별 스피너
        var genderData = resources.getStringArray(R.array.gender_array)
        var genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderData)
        binding.sexSpinner.adapter = genderAdapter

        // 연령 스피너
        var minAgeData = resources.getStringArray(R.array.min_age_array)
        var minAgeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, minAgeData)
        binding.minAgeSpinner.adapter = minAgeAdapter

        // 학력 스피너
        var educationData = resources.getStringArray(R.array.edu_array)
        var educationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, educationData)
        binding.educationSpinner.adapter = educationAdapter

        // 우대 조건 스피너
        var preferenceData = resources.getStringArray(R.array.prefer_array)
        var preferenceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, preferenceData)
        binding.preferenceSpinner.adapter = preferenceAdapter

        // 국적 스피너
        var nationData = resources.getStringArray(R.array.nation_array)
        var nationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nationData)
        binding.nationSpinner.adapter = nationAdapter

        // 근무 횟수, 근무 요일 스피너
        var workDayData = resources.getStringArray(R.array.work_day_array)
        var workDayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workDayData)
        binding.workDaySpinner.adapter = workDayAdapter


        db.collection("owner")
            .get()
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    if (userEmail == document.data["ownerEmail"]) {
                        ownerName = document.data["ownerName"].toString()
                        ownerPhone = document.data["ownerPhone"].toString()
                    }
                }

            }


        binding.btnPostJob.setOnClickListener {

            term = binding.workTermSpinner.selectedItem.toString()
            workTime = binding.startTimeSpinner.selectedItem.toString() + " ~ " + binding.endTimeSpinner.selectedItem.toString()
            workDay = binding.workDaySpinner.selectedItem.toString()
            sex = binding.sexSpinner.selectedItem.toString()
            age = binding.minAgeSpinner.selectedItem.toString() + "세 이상"
            education = binding.educationSpinner.selectedItem.toString()
            preference = binding.preferenceSpinner.selectedItem.toString()
            nation = binding.nationSpinner.selectedItem.toString()

            title = binding.edtStore.text.toString()
            money = binding.edtMoney.text.toString()
            addText = binding.edtAddText.text.toString()
            extraText = binding.edtDetail.text.toString()


            isChecked()

            if(noBlank) {
                val data = hashMapOf(
                    "title" to title,
                    "term" to term,
                    "work_time" to workTime,
                    "work_day" to workDay,
                    "money" to money,
                    "age" to age,
                    "sex" to sex,
                    "education" to education,
                    "preference" to preference,
                    "nation" to nation,
                    "add_text" to addText,
                    "extra_text" to extraText,
                    "email" to userEmail,
                    "job_id" to Math.random()*100,
                    "owner_name" to ownerName,
                    "owner_phone" to ownerPhone,
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
                        Log.w("OwnerUploadActivity", "Error getting documents: $exception")
                    }

                // 위치 데이터 삽입
                db.collection("owner")
                    .get()
                    .addOnSuccessListener { documents ->
                        for(document in documents) {
                            if(userEmail == document.data["ownerEmail"].toString()) {
                                latitude = document.data["latitude"].toString()
                                longitude = document.data["longitude"].toString()

                                val positionData = hashMapOf(
                                    "email" to userEmail,
                                    "latitude" to latitude,
                                    "longitude" to longitude,
                                    "name" to title,
                                )

                                db.collection("store")
                                    .add(positionData)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "지도에 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.w("OwnerUploadActivity", "Error getting documents: $exception")
                                    }
                            }
                        }
                    }

                finish()
            }
        }

    }
    private fun isChecked() {
        if(title.isEmpty()){
            Toast.makeText(this, "매장을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(term.isEmpty()){
            Toast.makeText(this, "근무기간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(workTime == "선택해주세요. ~ 선택해주세요."){
            Toast.makeText(this, "근무시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(workDay == "선택해주세요."){
            Toast.makeText(this, "근무요일을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(money.isEmpty()){
            Toast.makeText(this, "시급을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(age == "연령선택세 이상"){
            Toast.makeText(this, "나이를 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(sex == "선택해주세요."){
            Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(education == "선택해주세요."){
            Toast.makeText(this, "학력을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(preference == "선택해주세요."){
            Toast.makeText(this, "우대조건을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(nation == "선택해주세요."){
            Toast.makeText(this, "국적을 선택해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(addText.isEmpty()){
            Toast.makeText(this, "제목을 작성해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }
        if(extraText.isEmpty()){
            Toast.makeText(this, "상세설명을 작성해주세요.", Toast.LENGTH_SHORT).show()
            noBlank = false
        }

    }
}
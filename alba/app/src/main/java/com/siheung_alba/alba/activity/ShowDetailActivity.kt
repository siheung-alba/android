package com.siheung_alba.alba.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.siheung_alba.alba.R

class ShowDetailActivity : AppCompatActivity() {

    private lateinit var uploadTimeTextView: TextView
    private lateinit var titleTextView: TextView
    private lateinit var moneyButton: Button
    private lateinit var nationButton: Button
    private lateinit var termTextView: TextView
    private lateinit var workDayTextView: TextView
    private lateinit var workTimeTextView: TextView
    private lateinit var sexTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var educationTextView: TextView
    private lateinit var preferenceTextView: TextView
    private lateinit var detailTextView: TextView
    private lateinit var ownerNameTextView: TextView
    private lateinit var ownerPhoneTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_detail)

        uploadTimeTextView = findViewById(R.id.update_at) // 업데이트 시간
        titleTextView = findViewById(R.id.title) // 제목
        moneyButton = findViewById(R.id.money_btn) // 시급
        nationButton = findViewById(R.id.nation_btn) // 국적
        termTextView = findViewById(R.id.term) // 근무 기간
        workDayTextView = findViewById(R.id.work_day) // 근무 요일
        workTimeTextView = findViewById(R.id.work_time) // 근무 시간
        sexTextView = findViewById(R.id.sex) // 성별
        ageTextView = findViewById(R.id.age) // 나이
        educationTextView = findViewById(R.id.education) // 학력
        preferenceTextView = findViewById(R.id.preference) // 우대 조건
        detailTextView = findViewById(R.id.detail) // 상세 설명
        ownerNameTextView = findViewById(R.id.owner_name) // 사장님 이름
        ownerPhoneTextView = findViewById(R.id.owner_phone) // 사장님 전화번호

        val intent = intent
        val uploadTime = intent.getStringExtra("uploadTime")
        val title = intent.getStringExtra("title")
        val nation = intent.getStringExtra("nation")
        val term = intent.getStringExtra("term")
        val workDay = intent.getStringExtra("workDay")
        val workTime = intent.getStringExtra("workTime")
        val sex = intent.getStringExtra("sex")
        val age = intent.getStringExtra("age")
        val education = intent.getStringExtra("education")
        val preference = intent.getStringExtra("preference")
        val detail = intent.getStringExtra("detail")
        val ownerName = intent.getStringExtra("ownerName")
        val ownerPhone = intent.getStringExtra("ownerPhone")

        uploadTimeTextView.text = uploadTime
        titleTextView.text = title
        nationButton.text = nation
        termTextView.text = term
        workDayTextView.text = workDay
        workTimeTextView.text = workTime
        sexTextView.text = sex
        ageTextView.text = age
        educationTextView.text = education
        preferenceTextView.text = preference
        detailTextView.text = detail
        ownerNameTextView.text = ownerName
        ownerPhoneTextView.text = ownerPhone

    }
}
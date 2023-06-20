package com.siheung_alba.alba.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.siheung_alba.alba.R

class OwnerResumeHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_resume_home)

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
    }
}
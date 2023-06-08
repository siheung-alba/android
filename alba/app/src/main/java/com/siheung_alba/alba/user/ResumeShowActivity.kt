package com.siheung_alba.alba.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.siheung_alba.alba.R

class ResumeShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_show)

        // 이력서 정보 가져와서 화면에 표시하는 로직 구현
        val resumeId = intent.getStringExtra("resumeId")

        // TODO: 이력서 정보를 사용하여 화면에 표시하는 로직 작성

        // 수정 버튼 클릭 시 이력서 수정 페이지로 이동
        // TODO: 수정 버튼 클릭 시 이력서 수정 페이지로 이동하는 코드 작성

        // 삭제 버튼 클릭 시 이력서 삭제 로직 구현
        // TODO: 삭제 버튼 클릭 시 이력서 삭제 로직 작성
    }
}

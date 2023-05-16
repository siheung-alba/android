package com.siheung_alba.alba

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.content.Intent
import android.widget.EditText
import android.widget.Button
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import java.util.*



class JoinActivity : AppCompatActivity() {
    var dateString = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val startbutton = findViewById<Button>(R.id.startButton)
        val editdate = findViewById<EditText>(R.id.editDate)

        startbutton.setOnClickListener ({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        })

        editdate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
//                result.text = "날짜/시간 : "+dateString + " / " + timeString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
}
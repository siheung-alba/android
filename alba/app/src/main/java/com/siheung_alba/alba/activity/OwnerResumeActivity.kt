package com.siheung_alba.alba.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.siheung_alba.alba.R

class OwnerResumeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_resume)

        val resume1 = findViewById<TextView>(R.id.resume_1)
        val resume2 = findViewById<TextView>(R.id.resume_2)
        val resume3 = findViewById<TextView>(R.id.resume_3)


    }
}

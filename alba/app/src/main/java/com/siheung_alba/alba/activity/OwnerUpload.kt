package com.siheung_alba.alba.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.siheung_alba.alba.R

class OwnerUpload : AppCompatActivity() {
    private lateinit var uploadTitle: EditText
    private lateinit var period: EditText
    private lateinit var salary: EditText
    private lateinit var age: EditText
    private lateinit var gender: EditText
    private lateinit var country: EditText
    private lateinit var detail: EditText
    private lateinit var btnPostJob: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.owner_upload)

        uploadTitle = findViewById(R.id.upload_title)
        period = findViewById(R.id.period)
        salary = findViewById(R.id.salary)
        age = findViewById(R.id.age)
        gender = findViewById(R.id.gender)
        country = findViewById(R.id.country)
        detail = findViewById(R.id.detail)
        btnPostJob = findViewById(R.id.btnPostJob)

        btnPostJob.setOnClickListener {
            val title = uploadTitle.text.toString()
            val duration = period.text.toString()
            val hourlyRate = salary.text.toString()
            val jobAge = age.text.toString()
            val jobGender = gender.text.toString()
            val jobCountry = country.text.toString()
            val additionalDetails = detail.text.toString()

        }
    }
}
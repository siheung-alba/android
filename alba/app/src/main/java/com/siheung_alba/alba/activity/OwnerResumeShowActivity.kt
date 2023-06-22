package com.siheung_alba.alba.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.databinding.ActivityOwnerResumeShowBinding
import com.siheung_alba.alba.model.Applicant

class OwnerResumeShowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOwnerResumeShowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOwnerResumeShowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        binding.title.text = intent.getStringExtra("title")
        binding.career.text = intent.getStringExtra("career")
        binding.introduce.text = intent.getStringExtra("introduce")
        binding.name.text = intent.getStringExtra("name")
        binding.phone.text = intent.getStringExtra("phone")
        binding.email.text = intent.getStringExtra("email")
        binding.age.text = intent.getStringExtra("age")
        binding.sex.text = intent.getStringExtra("sex")
        binding.nation.text = intent.getStringExtra("nation")

        binding.check.setOnClickListener {
            finish()
        }

    }

}
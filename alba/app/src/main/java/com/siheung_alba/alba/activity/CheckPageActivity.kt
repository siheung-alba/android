package com.siheung_alba.alba.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.siheung_alba.alba.R

class CheckPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_page)


        val checkoutbutton : Button = findViewById(R.id.checkOutButton)

        checkoutbutton .setOnClickListener{
            finish()


        }

    }
}
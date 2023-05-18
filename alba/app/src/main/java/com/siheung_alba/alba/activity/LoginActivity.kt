package com.siheung_alba.alba.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.siheung_alba.alba.R
import com.siheung_alba.alba.fragment.HomeFragment


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val checkbox = findViewById<CheckBox>(R.id.checkbox_shop_login)
        val textview = findViewById<TextView>(R.id.login_name)
        val startbutton = findViewById<Button>(R.id.startButton)
        val joinbutton = findViewById<TextView>(R.id.join_button)
        val shopjoinbutton = findViewById<TextView>(R.id.shop_join_button)


        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                textview.text = "시흥알바 사장님 로그인"
            } else {
                textview.text = "시흥알바 로그인"
            }
        }

        startbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        joinbutton.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        shopjoinbutton.setOnClickListener {
            val intent = Intent(this, ShopJoinActivity::class.java)
            startActivity(intent)
        }




    }
}
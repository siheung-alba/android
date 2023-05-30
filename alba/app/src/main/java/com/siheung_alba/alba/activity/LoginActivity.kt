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
        val startButton = findViewById<Button>(R.id.startButton)
        val joinButton = findViewById<TextView>(R.id.join_button)
        val shopJoinButton = findViewById<TextView>(R.id.shop_join_button)


        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                textview.text = "시흥알바 사장님 로그인"
                startButton.setOnClickListener {
                    val intent = Intent(this, MainForOwnerActivity::class.java)
                    startActivity(intent)
                }

            } else {
                textview.text = "시흥알바 로그인"
            }
        }

        startButton.setOnClickListener {
            val intent = Intent(this, MainForUserActivity::class.java)
            startActivity(intent)
        }

        joinButton.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        shopJoinButton.setOnClickListener {
            val intent = Intent(this, ShopJoinActivity::class.java)
            startActivity(intent)
        }




    }
}
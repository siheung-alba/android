package com.siheung_alba.alba.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import com.siheung_alba.alba.R
import com.siheung_alba.alba.fragment.HomeFragment


class ShopJoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_join)

        val startbutton = findViewById<Button>(R.id.startButton)

        startbutton.setOnClickListener {
            val intent = Intent(this, HomeFragment::class.java)
            startActivity(intent)
        }

    }
}
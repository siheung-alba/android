package com.siheung_alba.alba

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button


class Shop_JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_join)

        val startbutton = findViewById<Button>(R.id.startButton)

        startbutton.setOnClickListener ({
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        })

    }
}
package com.siheung_alba.alba.activity

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.siheung_alba.alba.R
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import org.w3c.dom.Text

class PopupActivity : Activity() {
    private lateinit var titleTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)


        Log.e("check", "확인44444444444444444444444444444444444444444444444")

        val window = window
        window?.setGravity(Gravity.CENTER)

        titleTextView = findViewById(R.id.Title)

        val intent = intent
        val title = intent.getStringExtra("title")

        // 정보를 해당 TextView에 설정
        titleTextView.text = title
    }

    fun mOnClose(view: View) {
        finish()
    }
}

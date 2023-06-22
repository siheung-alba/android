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
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popup)

        val window = window
        window?.setGravity(Gravity.CENTER)

        textView = findViewById(R.id.text)

        val intent = intent
        val text = intent.getStringExtra("text")

        // 정보를 해당 TextView에 설정
        textView.text = text
    }

    fun mOnClose(view: View) {
        finish()
    }
}

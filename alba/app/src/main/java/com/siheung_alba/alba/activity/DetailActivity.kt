package com.siheung_alba.alba.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.siheung_alba.alba.R

class DetailActivity : AppCompatActivity() {
    private lateinit var checkButton: Button
    private lateinit var titleTextView: TextView
    private lateinit var addtextTextView: TextView
    private lateinit var moneyTextView: TextView
    private lateinit var termTextView: TextView
    private lateinit var sexTextView: TextView
    private lateinit var nationTextView: TextView
    private lateinit var ageTextView: TextView
    private lateinit var extratextTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        titleTextView = findViewById(R.id.Title)
        addtextTextView = findViewById(R.id.Addtext)
        moneyTextView = findViewById(R.id.Money)
        termTextView = findViewById(R.id.Term)
        sexTextView = findViewById(R.id.Sex)
        nationTextView = findViewById(R.id.Nation)
        ageTextView = findViewById(R.id.Age)
        extratextTextView = findViewById(R.id.Extratext)

        val intent = intent
        val title = intent.getStringExtra("title")
        val addtext = intent.getStringExtra("addtext")
        val money = intent.getStringExtra("money")
        val term = intent.getStringExtra("term")
        val sex = intent.getStringExtra("sex")
        val nation = intent.getStringExtra("nation")
        val age = intent.getStringExtra("age")
        val extratext = intent.getStringExtra("extratext")

        // 정보를 해당 TextView에 설정
        titleTextView.text = title
        addtextTextView.text = addtext
        moneyTextView.text = money
        termTextView.text = term
        sexTextView.text = sex
        nationTextView.text = nation
        ageTextView.text = age
        extratextTextView.text = extratext
    }

    fun mOnClose(view: View) {
        finish()
    }
}
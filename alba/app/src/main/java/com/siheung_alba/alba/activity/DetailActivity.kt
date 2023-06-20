package com.siheung_alba.alba.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
    private lateinit var workdayTextView: TextView
    private lateinit var worktimeTextView: TextView
    private lateinit var preferenceTextView: TextView
    private lateinit var educationTextView: TextView
    private lateinit var ownernameTextView: TextView
    private lateinit var ownerphoneTextView: TextView

    @SuppressLint("MissingInflatedId")
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
        workdayTextView = findViewById(R.id.WorkDay)
        worktimeTextView = findViewById(R.id.WorkTime)
        preferenceTextView = findViewById(R.id.Preference)
        educationTextView = findViewById(R.id.Education)
        ownernameTextView = findViewById(R.id.OwnerName)
        ownerphoneTextView = findViewById(R.id.OwnerPhone)

        val intent = intent
        val title = intent.getStringExtra("title")
        val addtext = intent.getStringExtra("addtext")
        val money = intent.getStringExtra("money")
        val term = intent.getStringExtra("term")
        val sex = intent.getStringExtra("sex")
        val nation = intent.getStringExtra("nation")
        val age = intent.getStringExtra("age")
        val extratext = intent.getStringExtra("extratext")
        val workday = intent.getStringExtra("workday")
        val worktime = intent.getStringExtra("worktime")
        val preference = intent.getStringExtra("preference")
        val education = intent.getStringExtra("education")
        val ownername = intent.getStringExtra("ownername")
        val ownerphone = intent.getStringExtra("ownerphone")

        // 정보를 해당 TextView에 설정
        titleTextView.text = title
        addtextTextView.text = addtext
        moneyTextView.text = money
        termTextView.text = term
        sexTextView.text = sex
        nationTextView.text = nation
        ageTextView.text = age
        extratextTextView.text = extratext
        workdayTextView.text = workday
        worktimeTextView.text = worktime
        preferenceTextView.text = preference
        educationTextView.text = education
        ownernameTextView.text = ownername
        ownerphoneTextView.text = ownerphone

/*
        apply.setOnClickListener { onApplyButtonClicked(it) }
*/
    }
  /*  fun onApplyButtonClicked(view: View) {
        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }*/

    fun mOnClose(view: View) {
        finish()
    }
}
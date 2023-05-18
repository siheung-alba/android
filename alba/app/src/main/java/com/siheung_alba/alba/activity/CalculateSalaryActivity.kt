package com.siheung_alba.alba.activity

import com.siheung_alba.alba.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import com.siheung_alba.alba.databinding.ActivityCalculateSalaryBinding
import java.util.Calendar

class CalculateSalaryActivity : AppCompatActivity() {

    private val binding: ActivityCalculateSalaryBinding by lazy { ActivityCalculateSalaryBinding.inflate(layoutInflater) }
    private val cal = Calendar.getInstance()
    private val weeksInMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_salary)

        val toolbar: Toolbar = findViewById(R.id.calculateSalaryToolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "급여계산기"
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val workTimeSpinner = findViewById<Spinner>(R.id.workTimeSpinner)
        val extendedWorkTimeSpinner = findViewById<Spinner>(R.id.extendedWorkTimeSpinner)
        val daySpinner = findViewById<Spinner>(R.id.daySpinner)


        var workTimeData = resources.getStringArray(R.array.work_time_array)
        var extendedWorkTimeData = resources.getStringArray(R.array.extended_work_time_array)
        var dayData = resources.getStringArray(R.array.day_array)

        var adapterForWorkTime = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, workTimeData)
        var adapterForExtendedWorkTime = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, extendedWorkTimeData)
        var adapterForDay = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dayData)

        workTimeSpinner.adapter = adapterForWorkTime
        extendedWorkTimeSpinner.adapter = adapterForExtendedWorkTime
        daySpinner.adapter = adapterForDay

        binding.calculateBtn.setOnClickListener {
            val workHours = workTimeSpinner.selectedItemPosition
            val totalWorkDays = daySpinner.selectedItemPosition * weeksInMonth
            val basePay = Integer.parseInt(binding.setBasePayText.text.toString())
            val totalPay = basePay * totalWorkDays * workHours

            binding.calculateResult.setText("예상 금액 : ${totalPay}").toString()
        }

    }

    private fun calculateSalary() {

    }

    private fun convertTimeToMinutes(time: String): Int {
        val split = time.split(" ")
        var minutes = 0
        for (i in split.indices step 2) {
            when (split[i + 1]) {
                "분" -> minutes += split[i].toInt()
                "시간" -> minutes += split[i].toInt() * 60
            }
        }
        return minutes
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
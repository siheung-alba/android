package com.siheung_alba.alba.user

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityCalculateSalaryBinding
import java.text.DecimalFormat

class CalculateSalaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculateSalaryBinding
//    private val cal = Calendar.getInstance()
//    private val weeksInMonth = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculateSalaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.calculateSalaryToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "급여계산기"
            setDisplayShowTitleEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        // 스피너 적용
        val workTimeSpinner = findViewById<Spinner>(R.id.workTimeSpinner)  // 일일 근무 시간
        val daySpinner = findViewById<Spinner>(R.id.daySpinner)  // 일주 근무 시간
        val extendedWorkTimeSpinner = findViewById<Spinner>(R.id.extendedWorkTimeSpinner)  // 연장 근무 시간
        val taxSpinner = findViewById<Spinner>(R.id.taxSpinner)  // 세금 적용

        // 배열 불러오기
        val workTimeData = resources.getStringArray(R.array.work_time_array)  // 일일 근무 시간 배열
        val dayData = resources.getStringArray(R.array.day_array)   // 일주일에 근무 시간 배열
        val extendedWorkTimeData = resources.getStringArray(R.array.extended_work_time_array)  // 연장 근무 시간 배열
        val taxData = resources.getStringArray(R.array.tax_array)  // 세금 배열

        // 배열과 스피너 적용
        val adapterForWorkTime = ArrayAdapter(this, android.R.layout.simple_spinner_item, workTimeData)
        val adapterForExtendedWorkTime = ArrayAdapter(this, android.R.layout.simple_spinner_item, extendedWorkTimeData)
        val adapterForDay = ArrayAdapter(this, android.R.layout.simple_spinner_item, dayData)
        val adapterForTax = ArrayAdapter(this, android.R.layout.simple_spinner_item, taxData)

        workTimeSpinner.adapter = adapterForWorkTime  // 일일 근무 시간
        daySpinner.adapter = adapterForDay   // 일주 근무 시간
        extendedWorkTimeSpinner.adapter = adapterForExtendedWorkTime  // 월 연장 근무 시간
        taxSpinner.adapter = adapterForTax   // 세금

        binding.calculateBtn.setOnClickListener {
            val basePay = binding.setBasePayText.text.toString().toFloat() // 시급 입력 받음

            val workHours = convertTimeToFloat(workTimeSpinner.selectedItem.toString())  // 일일 근무 시간
            val workDays = (daySpinner.selectedItemPosition) // 일주일 근무 일수
            val extendedWork = convertTimeToFloat(extendedWorkTimeSpinner.selectedItem.toString())  // 초과 근무 시간
            val weekWork =  workHours * workDays  // 일주일 총 근무 시간



            val totalPay = basePay * weekWork * 4 +
                    calculateHolidayAllowance(weekWork, basePay) * 4 +
                    calculateOvertimePay(extendedWork, basePay)

            val taxRate = getTaxRate(taxSpinner.selectedItemPosition)
            val taxedPay = totalPay - (totalPay * taxRate)
            val formattedTaxedPay = DecimalFormat("###,###.##").format(taxedPay)


//            Log.d("CalculateSalaryActivity", "workHours: $workHours")
//            Log.d("CalculateSalaryActivity", "WorkDays: $workDays")
//            Log.d("CalculateSalaryActivity", "taxRate: $taxRate")
//            Log.d("CalculateSalaryActivity", "taxedPay: $taxedPay")

            binding.calculateResult.text = "예상 금액: $formattedTaxedPay 원"
        }


        // 초기화 버튼

        // 초기화 버튼 클릭 이벤트 처리
        binding.resetBtn.setOnClickListener {
            // 시급 입력란 초기화
            binding.setBasePayText.text = null

            // 스피너 초기화
            val defaultPosition = 0
            binding.workTimeSpinner.setSelection(defaultPosition)
            binding.daySpinner.setSelection(defaultPosition)
            binding.extendedWorkTimeSpinner.setSelection(defaultPosition)
            binding.taxSpinner.setSelection(defaultPosition)
        }








    }


        // 초기화 버튼

        // 초기화 버튼 클릭 이벤트 처리
        binding.resetBtn.setOnClickListener {
            // 시급 입력란 초기화
            binding.setBasePayText.text = null

            // 스피너 초기화
            val defaultPosition = 0
            binding.workTimeSpinner.setSelection(defaultPosition)
            binding.daySpinner.setSelection(defaultPosition)
            binding.extendedWorkTimeSpinner.setSelection(defaultPosition)
            binding.taxSpinner.setSelection(defaultPosition)
        }




    }

    // 주휴수당 계산기
    private fun calculateHolidayAllowance(weekWork: Float, basePay: Float): Float {
        val weeklyWorkHours = 40.0f
        val holidayAllowance = if (weekWork < weeklyWorkHours && weekWork >= 15.0f) {
            (weekWork / weeklyWorkHours * 8 * basePay)
        } else if (weekWork < 15.0f) {
            0.0f
        } else {
            8 * basePay
        }
        Log.d("HolidayAllowance", "주휴수당: $holidayAllowance")
        return holidayAllowance
    }



    // 연장 근무 수당 계산
    private fun calculateOvertimePay(extendedWork: Float, basePay: Float): Int {
        val overtimeMultiplier = 1.5f
        val overtimePay = (extendedWork * basePay * overtimeMultiplier).toInt()

        Log.d("OvertimePay", "초과근무수당: $overtimePay")
        return overtimePay
    }


    private fun getTaxRate(taxSpinnerPosition: Int): Double {
        return when (taxSpinnerPosition) {
            0 -> 0.0
            1 -> 0.033
            2 -> 0.0932
            else -> 0.0
        }
    }


    private fun convertTimeToFloat(time: String): Float {
        val parts: List<String> = time.split("시간")

        if (parts.size == 2) {
            val hours = parts[0].toIntOrNull()
            val minutes = parts[1].replace(" ", "").replace("분", "").toIntOrNull()

            if (hours != null && minutes != null) {
                return hours.toFloat() + (minutes.toFloat() / 60.0f)
            } else {
                if (hours != null) {
                    return hours.toFloat()
                }
            }
        } else if (parts.size == 1 && parts[0].isNotEmpty()) {
            val hours = parts[0].replace(" ", "").replace("시간", "").toIntOrNull()

            if (hours != null) {
//                Log.d("시간 분배 시간만 있을때  ", "시간: $hours")
                return hours.toFloat()
            }
        }
//        Log.d("시간 분배 ", "시간: $time")
        return 0.0f
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}


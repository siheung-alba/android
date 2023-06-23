package com.siheung_alba.alba.activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityJoinBinding
import com.siheung_alba.alba.databinding.ActivityOwnerResumeHomeBinding
import com.siheung_alba.alba.databinding.ActivityOwnerUploadBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class OwnerResumePageActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    private val binding by lazy { ActivityOwnerResumeHomeBinding.inflate(layoutInflater) }

    private lateinit var term : String // 근무 기간
    private lateinit var workTime : String // 근무 시간
    private lateinit var workDay : String // 근무 요일
    private lateinit var title : String // 매장
    private lateinit var money : String // 시급
    private lateinit var addText : String // 제목
    private lateinit var extraText : String // 상세설명
    private lateinit var age : String
    private lateinit var sex : String // 성별
    private lateinit var education : String // 학력
    private lateinit var preference : String // 우대조건
    private lateinit var nation : String

    private lateinit var ownerName : String // 채용자 이름
    private lateinit var ownerPhone : String // 채용자 전화번호

    private lateinit var latitude : String
    private lateinit var longitude : String

    var noBlank = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.resumeUploadToolbar)
        toolbar.title = "채용공고 수정하기"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val user = Firebase.auth.currentUser

        val userEmail = user?.email

        // 근무 기간 스피너
        var workTermData = resources.getStringArray(R.array.work_term_array)
        var workTermAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workTermData)
        binding.workTermSpinner.adapter = workTermAdapter

        // 근무 시간 스피너
        var startTimeData = resources.getStringArray(R.array.start_time_array)
        var startTimeAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, startTimeData)
        binding.startTimeSpinner.adapter = startTimeAdapter

        var endTimeData = resources.getStringArray(R.array.end_time_array)
        var endTimeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, endTimeData)
        binding.endTimeSpinner.adapter = endTimeAdapter

        // 성별 스피너
        var genderData = resources.getStringArray(R.array.gender_array)
        var genderAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, genderData)
        binding.sexSpinner.adapter = genderAdapter

        // 연령 스피너
        var minAgeData = resources.getStringArray(R.array.min_age_array)
        var minAgeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, minAgeData)
        binding.minAgeSpinner.adapter = minAgeAdapter

        // 학력 스피너
        var educationData = resources.getStringArray(R.array.edu_array)
        var educationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, educationData)
        binding.educationSpinner.adapter = educationAdapter

        // 우대 조건 스피너
        var preferenceData = resources.getStringArray(R.array.prefer_array)
        var preferenceAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, preferenceData)
        binding.preferenceSpinner.adapter = preferenceAdapter

        // 국적 스피너
        var nationData = resources.getStringArray(R.array.nation_array)
        var nationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, nationData)
        binding.nationSpinner.adapter = nationAdapter

        // 근무 횟수, 근무 요일 스피너
        var workDayData = resources.getStringArray(R.array.work_day_array)
        var workDayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, workDayData)
        binding.workDaySpinner.adapter = workDayAdapter


        // 값 받아오기
        val titleEdit = intent.getStringExtra("titleEdit") // 매장
        val jobAddtextEdit = intent.getStringExtra("jobAddtextEdit") // 제목
        val jobMoneyEdit = intent.getStringExtra("jobMoneyEdit") // 시급
        val jobTermEdit = intent.getStringExtra("jobTermEdit") // 근무기간
        val workDayEdit = intent.getStringExtra("workDayEdit") // 근무요일
        val jobSexEdit = intent.getStringExtra("jobSexEdit") // 성별
        val jobAgeEdit = intent.getStringExtra("jobAgeEdit")  // 나이
        val educationEdit = intent.getStringExtra("educationEdit")//학력
        val preferenceEdit = intent.getStringExtra("preferenceEdit")//우대조건
        val jobNationEdit = intent.getStringExtra("jobNationEdit") // 국적
        val jobExtratextEdit = intent.getStringExtra("jobExtratextEdit") //추가 내용
        val jobId = intent.getStringExtra("job_id")





        // 값 출력
        binding.edtStore.setText(titleEdit) // 매장
        binding.edtAddText.setText(jobAddtextEdit) //제목
        binding.edtMoney.setText(jobMoneyEdit) //시급

        //근무기간
        val workTermSpinnerPosition = workTermAdapter.getPosition(jobTermEdit)
        binding.workTermSpinner.setSelection(workTermSpinnerPosition)

        //근무요일
        val workDayAdapterPosition = workDayAdapter.getPosition(workDayEdit)
        binding.workDaySpinner.setSelection(workDayAdapterPosition)


        //성별
        val genderAdapterPosition = genderAdapter.getPosition(jobSexEdit)
        binding.sexSpinner.setSelection(genderAdapterPosition)

        //나이
        val minAgeAdapterPosition = minAgeAdapter.getPosition(jobAgeEdit)
        binding.minAgeSpinner.setSelection(minAgeAdapterPosition)

        //학력
        val educationAdapterPosition = educationAdapter.getPosition(educationEdit)
        binding.educationSpinner.setSelection(educationAdapterPosition)

        //우대조건
        val preferenceAdapterPosition = preferenceAdapter.getPosition(preferenceEdit)
        binding.preferenceSpinner.setSelection(preferenceAdapterPosition)

        // 국적
        val nationAdapterPosition = nationAdapter.getPosition(jobNationEdit)
        binding.nationSpinner.setSelection(nationAdapterPosition)

        binding.edtDetail.setText(jobExtratextEdit) // 추가내용


        // 수정버튼 클릭시 이력서 업데이트
        val checkJobBtn: Button = findViewById(R.id.checkJobBtn)
        checkJobBtn.setOnClickListener {
            // 수정된 내용을 저장
            val editedStoreTitle = binding.edtStore.text.toString()
            val editedAddText = binding.edtAddText.text.toString()
            val editedMoney = binding.edtMoney.text.toString()
            val editedworkTermSpinner = binding.workTermSpinner.selectedItem.toString()
            val editedworkDaySpinner = binding.workDaySpinner.selectedItem.toString()
            val editedTimeSpinner =
                binding.startTimeSpinner.selectedItem.toString() + " ~ " + binding.endTimeSpinner.selectedItem.toString()
            val editedsexSpinner = binding.sexSpinner.selectedItem.toString()
            val editedminAgeSpinner = binding.minAgeSpinner.selectedItem.toString()
            val editededucationSpinner = binding.educationSpinner.selectedItem.toString()
            val editedpreferenceSpinner = binding.preferenceSpinner.selectedItem.toString()
            val editednationSpinner = binding.nationSpinner.selectedItem.toString()
            val editededtDetail = binding.edtDetail.text.toString()


            val OwnerData = hashMapOf<String, Any>(
                "title" to editedStoreTitle,
                "term" to editedworkTermSpinner,
                "work_time" to editedTimeSpinner,
                "work_day" to editedworkDaySpinner,
                "money" to editedMoney,
                "age" to editedminAgeSpinner,
                "sex" to editedsexSpinner,
                "education" to editededucationSpinner,
                "preference" to editedpreferenceSpinner,
                "nation" to editednationSpinner,
                "add_text" to editedAddText,
                "extra_text" to editededtDetail,
                "created_at" to formatted,
                "updated_at" to formatted



            )

            if (editedStoreTitle.isEmpty()) {
                Toast.makeText(this, "매장을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }

            if (editedworkTermSpinner.isEmpty()) {
                Toast.makeText(this, "근무기간을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editedTimeSpinner == "선택해주세요. ~ 선택해주세요.") {
                Toast.makeText(this, "근무시간을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editedworkDaySpinner == "선택해주세요.") {
                Toast.makeText(this, "근무요일을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editedMoney.isEmpty()) {
                Toast.makeText(this, "시급을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editedminAgeSpinner == "연령선택세 이상") {
                Toast.makeText(this, "나이를 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editedsexSpinner == "선택해주세요.") {
                Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editededucationSpinner == "선택해주세요.") {
                Toast.makeText(this, "학력을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editedpreferenceSpinner == "선택해주세요.") {
                Toast.makeText(this, "우대조건을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editednationSpinner == "선택해주세요.") {
                Toast.makeText(this, "국적을 선택해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editedAddText.isEmpty()) {
                Toast.makeText(this, "제목을 작성해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }
            if (editededtDetail.isEmpty()) {
                Toast.makeText(this, "상세설명을 작성해주세요.", Toast.LENGTH_SHORT).show()
                noBlank = false
                return@setOnClickListener
            }

            val collectionRef = db.collection("job")
            collectionRef.document(jobId!!)
                .update(OwnerData)
                .addOnSuccessListener {
                    Toast.makeText(this, "이력서가 성공적으로 업데이트되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("OwnerResumeActivity", "Error updating resume: $exception")
                    Toast.makeText(this, "이력서 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show()
                }


        }

        // 삭제버튼 클릭시
        val deleteJobBtn: Button = findViewById(R.id.deleteJobBtn)
        deleteJobBtn.setOnClickListener {

            val collectionRef = db.collection("job")

            collectionRef.document(jobId!!)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "이력서가 성공적으로 삭제되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("OwnerResumeActivity", "Error deleting resume: $exception")
                    Toast.makeText(this, "이력서 삭제에 실패했습니다", Toast.LENGTH_SHORT).show()
                }

        }



    }


}
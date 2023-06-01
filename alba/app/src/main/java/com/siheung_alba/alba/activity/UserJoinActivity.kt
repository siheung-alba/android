package com.siheung_alba.alba.activity

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.siheung_alba.alba.R
import com.siheung_alba.alba.user.MainForUserActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*



class UserJoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // 파이어베이스
    private var db = Firebase.firestore
    private var userCol = db.collection("user")
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    var dateString = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth // 파이어베이스

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var isGoToJoin = true

        var editname = findViewById<EditText>(R.id.editName)
        var editEmail = findViewById<EditText>(R.id.editEmail)
        var editTell = findViewById<EditText>(R.id.editTell)
        var editPassword = findViewById<EditText>(R.id.editPassword)
        var editCheckPassword = findViewById<EditText>(R.id.editPassword2)


        // 날짜, 국적
        var editDate = findViewById<Button>(R.id.editDate)
        var dateResult = findViewById<TextView>(R.id.editDateResult)
        var nationSpinner = findViewById<Spinner>(R.id.nationSpinner)

        editDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}/${month+1}/${dayOfMonth}"
                dateResult.text = "날짜 :" + dateString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        var nationData = resources.getStringArray(R.array.nationality_array)
        var adapterNation = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nationData)

        nationSpinner.adapter = adapterNation

        // 여성 남성 값 저장
        val genderRadioGroup = findViewById<RadioGroup>(R.id.genderRadioGroup) // 성별 라디오 그룹
        var male = findViewById<RadioButton>(R.id.male) // 남성
        var female = findViewById<RadioButton>(R.id.female) // 여성
        var selectedGender = ""
        genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> selectedGender = male.text.toString()
                R.id.female -> selectedGender = female.text.toString()
            }
        }
        val startJoinButton = findViewById<Button>(R.id.startJoinButton) // 가입 버튼

        startJoinButton.setOnClickListener {

            var name = editname.text.toString() // 이름
            var email = editEmail.text.toString() // 이메일
            var phone = editTell.text.toString() // 전화번호
            var password = editPassword.text.toString() // 비밀번호
            var checkPassword = editCheckPassword.text.toString() // 비밀번호 확인
            var nation = nationSpinner.selectedItem.toString();

            // 값이 비어있는지 확인

            if(name.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(phone.isEmpty()) {
                Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(password.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(checkPassword.isEmpty()) {
                Toast.makeText(this, "비밀번호 재입력을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(password != checkPassword) {
                Toast.makeText(this, "비밀번호가 같지 않습니다. 다시 입력해주세요. ", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(dateString.isEmpty()) {
                Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(nation == "선택하세요.") {
                Toast.makeText(this, "국적을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(!male.isSelected || !female.isSelected) {
                Toast.makeText(this, "성별을 입력해주세요", Toast.LENGTH_LONG).show()
            }

            if(isGoToJoin) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "생성되었습니다.", Toast.LENGTH_LONG).show()

                            val data = hashMapOf(
                                "name" to name,
                                "email" to email,
                                "phone" to phone,
                                "password" to password,
                                "nation" to nation,
                                "birthday" to dateString,
                                "sex" to selectedGender,
                                "created_at" to formatted,
                                "updated_at" to formatted
                            )

                            userCol
                                .add(data)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, MainForUserActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener { exception ->
                                    Log.w("fail", "Error getting documents: $exception")
                                }

                        } else {
                            Toast.makeText(this, "실패하였습니다.", Toast.LENGTH_LONG).show()
                            Log.e(TAG, "createUserWithEmail:failure", task.exception)
                        }
                    }
            }
        }
    }
}

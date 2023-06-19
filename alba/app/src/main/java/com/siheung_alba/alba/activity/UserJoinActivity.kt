package com.siheung_alba.alba.activity

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.siheung_alba.alba.R
import com.siheung_alba.alba.databinding.ActivityJoinBinding
import com.siheung_alba.alba.user.MainForUserActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.HashMap
import kotlin.properties.Delegates

class UserJoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private var userCol = db.collection("user")
    private var ownerCol = db.collection("owner")
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)
    private var dateString = ""

    private val binding by lazy { ActivityJoinBinding.inflate(layoutInflater) }

    private lateinit var name : String
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var phone : String
    private lateinit var checkPassword : String
    private lateinit var nation : String
    var selectedGender = ""
    var noBlank = true
    var push = false
    var checkBtnNUM = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.editDate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}/${month+1}/${dayOfMonth}"
                binding.editDateResult.text = "날짜 :" + dateString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        var nationData = resources.getStringArray(R.array.nationality_array)
        var adapterNation = ArrayAdapter(this, android.R.layout.simple_spinner_item, nationData)

        binding.nationSpinner.adapter = adapterNation

        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.male -> selectedGender = "남성"
                R.id.female -> selectedGender = "여성"
            }
        }

        //이용약관 버튼
        binding.check.setOnClickListener{
            val intent = Intent(this, CheckPageActivity::class.java)
            startActivity(intent)


        }






        // 이메일 중복확인
        binding.checkEmailBtn.setOnClickListener {
            isChecked()
        }

        binding.startJoinButton.setOnClickListener {

            email = binding.editEmail.text.toString()
            name = binding.editName.text.toString() // 이름
            phone = binding.editTell.text.toString() // 전화번호
            password = binding.editPassword.text.toString() // 비밀번호
            checkPassword = binding.editPassword2.text.toString() // 비밀번호 확인
            nation = binding.nationSpinner.selectedItem.toString(); // 국적


            // 값이 비어있는지 확인
            checkValue()

            if(noBlank) {
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
                makeAccount(data)
            }
        }
    }

    private fun checkValue() {
        if(name.isEmpty()) {
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }
        if(email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
        }
        if(phone.isEmpty()) {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }
        if(password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        } else if (password.length <= 5) {
            Toast.makeText(this, "비밀번호 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
        }
        if(checkPassword.isEmpty()) {
            Toast.makeText(this, "비밀번호 재입력을 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }
        if(password != checkPassword) {
            Toast.makeText(this, "비밀번호가 같지 않습니다. 다시 입력해주세요. ", Toast.LENGTH_LONG).show()
            noBlank = false
        }
        if(dateString.isEmpty()) {
            Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }
        if(nation == "선택하세요.") {
            Toast.makeText(this, "국적을 입력해주세요", Toast.LENGTH_LONG).show()
            noBlank = false
        }
        if(selectedGender.isEmpty()) {
            Toast.makeText(this, "성별을 입력해주세요", Toast.LENGTH_LONG).show()
        }

        if(!push) {
            Toast.makeText(this, "중복 확인을 해주세요", Toast.LENGTH_LONG).show()
            noBlank = false
        }

        if(!binding.checkBtn.isChecked) {
            Toast.makeText(this, "이용약관 승인이 안되었습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun isChecked() {

        email = binding.editEmail.text.toString()

        if(email.isNotEmpty()) {
            userCol
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener {userResult ->
                    if(userResult.size() == 0) {
                        ownerCol
                            .whereEqualTo("ownerEmail", email)
                            .get()
                            .addOnSuccessListener {ownerResult ->
                                if(ownerResult.size() == 0) {
                                    binding.checkText.text = "${binding.editEmail.text}은 사용 가능한 이메일 입니다."
                                    push = true
                                    Log.i("msg", "everything fail")
                                }else {
                                    binding.checkText.text = "${binding.editEmail.text}은 사용할 수 없는 이메일 입니다."
                                    noBlank = false
                                    Log.i("msg", "for ownerCol Success")
                                }
                            }
                    } else {
                        binding.checkText.text = "${binding.editEmail.text}은 사용할 수 없는 이메일 입니다."
                        noBlank = false
                        Log.i("msg", "for userCol Success")
                    }
                }
                .addOnFailureListener {
                    Log.i("msg", "error occured")
                }
        }
        else {
            binding.checkText.text = "이메일을 입력해주세요."
        }
    }

    private fun makeAccount(data: HashMap<String, String>) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "생성되었습니다.", Toast.LENGTH_LONG).show()

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

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
}

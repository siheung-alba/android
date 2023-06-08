package com.siheung_alba.alba.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.databinding.ActivityShopJoinBinding
import com.siheung_alba.alba.kakao_map_api.AddressApiActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ShopJoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private var userCol = db.collection("user")
    private var ownerCol = db.collection("owner")
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)
    private val binding by lazy { ActivityShopJoinBinding.inflate(layoutInflater) }

    private lateinit var shopName : String
    private lateinit var shopEmail : String
    private lateinit var shopNumber : String
    private lateinit var shopPhone : String
    private lateinit var shopPwd : String
    private lateinit var shopPwdRe : String
    private lateinit var shopAddress : String

    var noBlank = true
    var push = false

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 이메일 중복확인
        binding.checkEmailBtn.setOnClickListener {
            isChecked()
        }

        //이용약관 버튼
        binding.check.setOnClickListener{
            val intent = Intent(this, CheckPageActivity::class.java)
            startActivity(intent)
        binding.editshopAddress.setOnClickListener {
            startActivity(Intent(this, AddressApiActivity::class.java))
        }
    }


        }


        binding.startButton.setOnClickListener {

            // 가입
            shopName = binding.editshopName.text.toString()
            shopEmail = binding.editshopEmail.text.toString()
            shopNumber = binding.editshopNum.text.toString()
            shopPhone = binding.editshopTell.text.toString()
            shopPwd = binding.editshopPassword.text.toString()
            shopPwdRe = binding.editshopPasswordRe.text.toString()
            shopAddress = binding.editshopAddress.text.toString()

            // 값이 비어있는지 확인
            checkValue()

            if(noBlank) {
                val data = hashMapOf(
                    "owmerName" to shopName, // 매장 이름
                    "ownerEmail" to shopEmail, // 사장님 이메일
                    "ownerPhone" to shopPhone, // 사장님 전화번호
                    "ownerPwd" to shopPwd, // 비밀번호
                    "ownerAddress" to shopAddress, // 매장 주소
                    "ownerNumber" to shopNumber, // 사업자 번호
                    "created_at" to formatted,
                    "updated_at" to formatted
                )
                makeAccount(data)
            }
        }



    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }
    private fun checkValue() {
        if(shopName.isEmpty()) {
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }

        if(shopEmail.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        } else if (!isValidEmail(shopEmail)) {
            Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        if(shopNumber.isEmpty()) {
            Toast.makeText(this, "사업자 번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }

        if(shopPhone.isEmpty()) {
            Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }

        if(shopPwd.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        } else if (shopPwd.length <= 5) {
            Toast.makeText(this, "비밀번호 6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
        }

        if(shopPwdRe.isEmpty()) {
            Toast.makeText(this, "비밀번호 재입력을 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }

        if(shopPwd != shopPwdRe) {
            Toast.makeText(this, "비밀번호가 같지 않습니다. 다시 입력해주세요. ", Toast.LENGTH_LONG).show()
            noBlank = false
        }

        if(shopAddress.isEmpty()) {
            Toast.makeText(this, "매장주소를 입력해주세요.", Toast.LENGTH_LONG).show()
            noBlank = false
        }

        if(!binding.checkBtn.isChecked) {
            Toast.makeText(this, "이용약관 승인이 안되었습니다.", Toast.LENGTH_LONG).show()
        }
    }
    private fun isChecked() {

        shopEmail = binding.editshopEmail.text.toString()

        if(shopEmail.isNotEmpty()) {
            userCol
                .whereEqualTo("email", shopEmail)
                .get()
                .addOnSuccessListener {userResult ->
                    if(userResult.size() == 0) {
                        ownerCol
                            .whereEqualTo("ownerEmail", shopEmail)
                            .get()
                            .addOnSuccessListener {ownerResult ->
                                if(ownerResult.size() == 0) {
                                    binding.checkText.text = "${binding.editshopEmail.text}은 사용 가능한 이메일 입니다."
                                    push = true
                                    Log.i("msg", "everything fail")
                                }else {
                                    binding.checkText.text = "${binding.editshopEmail.text}은 사용할 수 없는 이메일 입니다."
                                    noBlank = false
                                    Log.i("msg", "for ownerCol Success")
                                }
                            }
                    } else {
                        binding.checkText.text = "${binding.editshopEmail.text}은 사용할 수 없는 이메일 입니다."
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
        auth.createUserWithEmailAndPassword(shopEmail, shopPwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "생성되었습니다.", Toast.LENGTH_LONG).show()

                    ownerCol
                        .add(data)
                        .addOnSuccessListener {
                            Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainForOwnerActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                        .addOnFailureListener {exception ->
                            Log.w("fail", "Error getting documents: $exception")
                        }
                } else {
                    Toast.makeText(this, "실패하였습니다.", Toast.LENGTH_LONG).show()
                    Log.e(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }

}
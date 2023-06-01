package com.siheung_alba.alba.activity

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.fragment.HomeFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ShopJoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // 파이어베이스
    private var db = Firebase.firestore
    private var ownerCol = db.collection("owner")
    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)

    private fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth // 파이어베이스

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_join)

        var isGoToJoin = true

        // 가입
        var editshopname = findViewById<EditText>(R.id.editshopName)
        var editshopemail = findViewById<EditText>(R.id.editshopEmail)
        var editshopnum = findViewById<EditText>(R.id.editshopNum)
        var editshoptell = findViewById<EditText>(R.id.editshopTell)
        var editshoppassword = findViewById<EditText>(R.id.editshopPassword)
        var editshoppasswordre = findViewById<EditText>(R.id.editshopPasswordRe)
        var editshopaddress = findViewById<EditText>(R.id.editshopAddress)


        // 가입버튼
        val startbutton = findViewById<Button>(R.id.startButton)

        startbutton.setOnClickListener {

            // 가입
            var editshopname = editshopname.text.toString()
            var editshopemail = editshopemail.text.toString()
            var editshopnum = editshopnum.text.toString()
            var editshoptell = editshoptell.text.toString()
            var editshoppassword = editshoppassword.text.toString()
            var editshoppasswordre = editshoppasswordre.text.toString()
            var editshopaddress = editshopaddress.text.toString()

            if(editshopname.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(editshopemail.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(editshopnum.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else if (!isValidEmail(editshopnum)) {
                Toast.makeText(this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }

            if(editshoptell.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(editshoppassword.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            } else if (editshoppassword.length <= 5) {
                Toast.makeText(this, "6자리 이상 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            if(editshoppasswordre.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(!editshoppassword.equals(editshoppasswordre)) {
                Toast.makeText(this, "비밀번호가 같지 않습니다. 다시 입력해주세요. ", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(editshopaddress.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(isGoToJoin) {

                auth.createUserWithEmailAndPassword(editshopemail, editshoppassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "생성되었습니다.", Toast.LENGTH_LONG).show()

                            val data = hashMapOf(
                                "owmerName" to editshopname,
                                "ownerEmail" to editshopemail,
                                "ownerPhone" to editshoptell,
                                "ownerPwd" to editshoppassword,
                                "ownerAddress" to editshopaddress,
                                "ownerNumber" to editshopnum,
                                "created_at" to formatted,
                                "updated_at" to formatted
                            )

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
    }
}
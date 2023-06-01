package com.siheung_alba.alba.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.user.MainForUserActivity


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // 파이어베이스
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth // 파이어베이스


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // 가입
        var email = findViewById<EditText>(R.id.loginEmail)
        var pwd = findViewById<EditText>(R.id.loginPassword)




        val checkbox = findViewById<CheckBox>(R.id.checkbox_shop_login)
        val textview = findViewById<TextView>(R.id.login_name)
        val startButton = findViewById<Button>(R.id.startButton)
        val joinButton = findViewById<Button>(R.id.join_button)
        val shopJoinButton = findViewById<Button>(R.id.shop_join_button)


        checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                textview.text = "시흥알바 사장님 로그인"
//                startButton.setOnClickListener {
//                    val intent = Intent(this, MainForOwnerActivity::class.java)
//                    startActivity(intent)
//                }

            } else {
                textview.text = "시흥알바 로그인"
            }
        }

        startButton.setOnClickListener {

            var email = email.text.toString()
            var pwd = pwd.text.toString()

            auth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val intent = Intent(this, MainForUserActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "이메일 또는 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        joinButton.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }

        shopJoinButton.setOnClickListener {
            val intent = Intent(this, ShopJoinActivity::class.java)
            startActivity(intent)
        }




    }
}
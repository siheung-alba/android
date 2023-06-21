package com.siheung_alba.alba.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.databinding.ActivityLoginBinding
import com.siheung_alba.alba.user.MainForUserActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private var flag = 0
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        auth = Firebase.auth

        binding.checkboxShopLogin.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.loginName.text = "시흥알바 사장님 로그인"
                flag = 1
            }
            else {
                binding.loginName.text = "시흥알바 로그인"
            }
        }

        binding.startButton.setOnClickListener {
            emailLogin()
        }

        binding.joinButton.setOnClickListener {
            val intent = Intent(this, UserJoinActivity::class.java)
            startActivity(intent)
        }

        binding.shopJoinButton.setOnClickListener {
            val intent = Intent(this, ShopJoinActivity::class.java)
            startActivity(intent)
        }
    }

    private fun emailLogin() {
        if(binding.loginEmail.text.toString().isNullOrEmpty() || binding.loginPassword.text.toString().isNullOrEmpty()) {
            Toast.makeText(this, "이메일 혹은 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
        }else {
            signinEmail()
        }
    }
    private fun signinEmail() {

        auth.signInWithEmailAndPassword(binding.loginEmail.text.toString(), binding.loginPassword.text.toString())
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    when(flag) {
                        1 -> moveMainPageForOwner(task.result?.user)
                        0 -> moveMainPageForUser(task.result?.user)
                        else -> Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "잘못된 정보를 입력했거나, 계정이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun moveMainPageForUser(user: FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainForUserActivity::class.java))
            finish()
        }
    }

    private fun moveMainPageForOwner(user: FirebaseUser?) {
        if(user != null) {
            startActivity(Intent(this, MainForOwnerActivity::class.java))
            finish()
        }
    }

    // 로그인 유지하는 기능이라는데....
    override fun onStart() {
        super.onStart()
        when(flag) {
            1 -> moveMainPageForOwner(auth?.currentUser)
            0 -> moveMainPageForUser(auth?.currentUser)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
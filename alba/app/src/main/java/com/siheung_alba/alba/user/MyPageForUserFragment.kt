package com.siheung_alba.alba.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.user.CalculateSalaryActivity
import com.siheung_alba.alba.R
import com.siheung_alba.alba.model.UserModel
import com.siheung_alba.alba.user.ResumeUploadActivity

class MyPageForUserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_my_page_for_user, container, false)
        val uploadBtn : Button = view.findViewById(R.id.resume_upload_btn)
        val toolbar: Toolbar = view.findViewById(R.id.myPageToolbar)

        toolbar.title = "마이페이지"
        toolbar.inflateMenu(R.menu.my_page_menu)

        // 급여계산기 페이지로 이동
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.my_page_menu -> {
                    val intent = Intent(activity, CalculateSalaryActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        setHasOptionsMenu(true)

        // 이력서 업로드 페이지로 이동
        uploadBtn.setOnClickListener {
            val intent = Intent(activity, ResumeUploadActivity::class.java)
            startActivity(intent)
        }

        Log.d("MyPageForUserFragment", "Fetching user information...")

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser
//        val email = user?.email

        if (user != null ) {
            val email = user.email

            Log.d("MyPageForUserFragment", "Fetching user information for email: $email")

            // 유저 정보를 가져와서 텍스트뷰에 설정
            val userInfoTextView1: TextView = view.findViewById(R.id.mypage_info_text1)
            val userInfoTextView2: TextView = view.findViewById(R.id.mypage_info_text2)
            val userInfoTextView3: TextView = view.findViewById(R.id.mypage_info_text3)
            val userInfoTextView4: TextView = view.findViewById(R.id.mypage_info_text4)

            if (email != null) {
                db.collection("user")
                    .document(email)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        val user = documentSnapshot.toObject(UserModel::class.java)
                        user?.let {
                            userInfoTextView1.text = "테스트"
//                            userInfoTextView1.text = "이름: ${it.name}"
                            userInfoTextView2.text = "성별: ${it.sex}"
                            val age = calculateAge(it.birthday)
                            userInfoTextView3.text = "나이: 만 $age 세"
                            userInfoTextView4.text = "국적: ${it.nation}"
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e(
                            "MyPageForUserFragment",
                            "Failed to retrieve user information: $exception"
                        )

                        // 실패 처리
                        // 예: 로그 출력 등
                    }
            }

        }
            return view


    }

    private fun calculateAge(birthday: String): Int {
        val birthYear = birthday.split("/")[0].toInt()
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        return currentYear - birthYear + 1
    }

}

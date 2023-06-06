package com.siheung_alba.alba.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.Query
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.user.CalculateSalaryActivity
import com.siheung_alba.alba.R
import com.siheung_alba.alba.adapter.ResumeAdapter
import com.siheung_alba.alba.model.ResumeModel
import com.siheung_alba.alba.model.UserModel
import com.siheung_alba.alba.user.ResumeUploadActivity
import java.util.*

class MyPageForUserFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    val user = Firebase.auth.currentUser
    val userEmail = user?.email
    private val colResumeRef = db.collection("resume")
    private val itemList = arrayListOf<ResumeModel>()
    private val adapter = ResumeAdapter(itemList)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_page_for_user, container, false)
        val uploadBtn: Button = view.findViewById(R.id.resume_upload_btn)
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

        val userInfoTextView1: TextView = view.findViewById(R.id.mypage_info_text1)
        val userInfoTextView2: TextView = view.findViewById(R.id.mypage_info_text2)
        val userInfoTextView3: TextView = view.findViewById(R.id.mypage_info_text3)
        val userInfoTextView4: TextView = view.findViewById(R.id.mypage_info_text4)


        // 이력서 업로드 페이지로 이동
        uploadBtn.setOnClickListener {
            val intent = Intent(activity, ResumeUploadActivity::class.java)
            intent.putExtra("userName", userInfoTextView1.text.toString())
            intent.putExtra("userSex", userInfoTextView2.text.toString())
            intent.putExtra("userAge", userInfoTextView3.text.toString())
            intent.putExtra("userNation", userInfoTextView4.text.toString())
            startActivity(intent)
        }

        // 나이 계산 함수
        fun calculateAge(birthday: String): Int {
            val birthYear = birthday.split("/")[0].toInt()
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            return currentYear - birthYear + 1
        }

        Log.d("MyPageForUserFragment", "Fetching user information...")

//        Log.d("MyPageForUserFragment", "Fetching user information...")

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            val email = user.email

//            Log.d("MyPageForUserFragment", "Fetching user information for email: $email")

            // 유저 정보를 가져와서 텍스트뷰에 설정
            var userInfoTextView1: TextView = view.findViewById(R.id.mypage_info_text1) // 이름
            var userInfoTextView2: TextView = view.findViewById(R.id.mypage_info_text2) // 성별
            var userInfoTextView3: TextView = view.findViewById(R.id.mypage_info_text3) // 나이
            var userInfoTextView4: TextView = view.findViewById(R.id.mypage_info_text4) // 국적

            if (email != null) {
                db.collection("user")
                    .whereEqualTo("email", email) // 이메일이 일치하는 문서 가져오기
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            userInfoTextView1.text =
                                "이름 : " + document.data["name"].toString() // 이름
                            userInfoTextView2.text = "성별 : " + document.data["sex"].toString() // 성별
                            userInfoTextView3.text =
                                "나이 : " + calculateAge(document.data["birthday"].toString()) // 나이
                            userInfoTextView4.text =
                                "국적 : " + document.data["nation"].toString() // 국적
                        }

                    }
                    .addOnFailureListener { exception ->
                        Log.e(
                            "MyPageForUserFragment",
                            "Failed to retrieve user information: $exception"
                        )
                    }

            }
        }
            return view

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resumeList = view.findViewById<RecyclerView>(R.id.resume_list)
        resumeList.layoutManager = LinearLayoutManager(requireContext())
        resumeList.adapter = adapter

        colResumeRef
            .whereEqualTo("email", userEmail)
            .orderBy("updated_at", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (document in result) {
                    val item = ResumeModel(
                        document.data["email"] as? String?,
                        document.data["title"] as? String?,
                        document.data["career"] as? String?,
                        document.data["introduce"] as? String?,
                        document.data["updated_at"] as? String?
                    )
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("MyPageForUserFragment", "Error getting documents: $exception")
            }
    }





}

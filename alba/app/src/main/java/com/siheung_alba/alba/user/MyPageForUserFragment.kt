package com.siheung_alba.alba.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            val email = user.email

//            Log.d("MyPageForUserFragment", "Fetching user information for email: $email")

            if (email != null) {
                db.collection("user")
                    .whereEqualTo("email", email) // 이메일이 일치하는 문서 가져오기
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            userInfoTextView1.text =
                                "이름 : " + document.data["name"].toString() // 이름
                            userInfoTextView2.text =
                                "성별 : " + document.data["sex"].toString() // 성별
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        val resumeList = view.findViewById<RecyclerView>(R.id.resume_list)
        resumeList.layoutManager = LinearLayoutManager(requireContext())
        resumeList.adapter = adapter

        // 사용자 정보 초기화
        val userInfoTextView1: TextView = view.findViewById(R.id.mypage_info_text1) // 이름
        val userInfoTextView2: TextView = view.findViewById(R.id.mypage_info_text2) // 성별
        val userInfoTextView3: TextView = view.findViewById(R.id.mypage_info_text3) // 나이
        val userInfoTextView4: TextView = view.findViewById(R.id.mypage_info_text4) // 국적

        // Firestore에서 이력서 목록 가져오기
        colResumeRef
            .whereEqualTo("email", userEmail)
            .orderBy("updated_at", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()  // 이력서 목록 초기화
                for (document in result) {
                    val item = ResumeModel(
                        document.data["email"] as? String?,
                        document.data["title"] as? String?,
                        document.data["career"] as? String?,
                        document.data["introduce"] as? String?,
//                        document.data["resume_id"] as? String?,
                        document.id,
                        document.data["created_at"] as? String?,
                        document.data["updated_at"] as? String?
                    )
                    itemList.add(item)  // 이력서 목록에 추가

//                    val resumeId = document.data["resume_id"] as? String
//                    Log.d("MyPageForUserFragment", "resume_id: $resumeId") // resume_id 값 로그로 출력
                }
                adapter.notifyDataSetChanged()

                // 이력서 개수에 따라 버튼 상태 설정
                val hasResume = itemList.isNotEmpty()
                val uploadBtn: Button = view.findViewById(R.id.resume_upload_btn)
                uploadBtn.isEnabled = !hasResume
                if (hasResume) {
                    uploadBtn.setOnClickListener {
                        Toast.makeText(activity, "이미 이력서가 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    uploadBtn.setOnClickListener {
                        val intent = Intent(activity, ResumeUploadActivity::class.java)
                        intent.putExtra("userName", userInfoTextView1.text.toString())
                        intent.putExtra("userSex", userInfoTextView2.text.toString())
                        intent.putExtra("userAge", userInfoTextView3.text.toString())
                        intent.putExtra("userNation", userInfoTextView4.text.toString())
                        startActivity(intent)
                    }
                }


            }

            .addOnFailureListener { exception ->
                Log.w("MyPageForUserFragment", "Error getting documents: $exception")
            }

        adapter.setOnItemClickListener { position: Int ->
            val selectedResume = itemList[position]

            // 이력서 보여주는 이동하는 인텐트 생성
            val intent = Intent(requireContext(), ResumeShowActivity::class.java)
            intent.putExtra("title", selectedResume.title) // 이력서 제목
            intent.putExtra("career", selectedResume.career) // 경력
            intent.putExtra("introduce", selectedResume.introduce) // 자기소개서
            intent.putExtra("resume_id", selectedResume.resume_id) // 이력서 ID
            intent.putExtra("userName", userInfoTextView1.text.toString()) // 이름
            intent.putExtra("userSex", userInfoTextView2.text.toString()) // 성별
            intent.putExtra("userAge", userInfoTextView3.text.toString()) // 나이
            intent.putExtra("userNation", userInfoTextView4.text.toString()) // 국적

            startActivity(intent)
        }
    }





}

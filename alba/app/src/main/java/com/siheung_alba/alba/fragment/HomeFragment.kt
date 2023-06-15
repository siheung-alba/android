package com.siheung_alba.alba.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.DetailActivity
import com.siheung_alba.alba.activity.LoginActivity
import com.siheung_alba.alba.activity.PopupActivity
import com.siheung_alba.alba.activity.ResumePopupActivity
import com.siheung_alba.alba.adapter.JobAdapter
import com.siheung_alba.alba.adapter.ResumeAdapter
import com.siheung_alba.alba.model.JobModel
import com.siheung_alba.alba.model.ResumeModel


class HomeFragment : Fragment() {

    private val db = Firebase.firestore
    private val colJobRef = db.collection("job")
    private val itemList = arrayListOf<JobModel>()
    private val jobadapter = JobAdapter(itemList)

    private val resumecollection = db.collection("resume")
    private val reitemList = arrayListOf<ResumeModel>()
    private val resumeadapter = ResumeAdapter(reitemList)

    private lateinit var auth: FirebaseAuth // 파이어베이스

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.homeToolbar)
        toolbar.title = "채용공고"
        toolbar.inflateMenu(R.menu.home_menu)

        val jobList = view?.findViewById<RecyclerView>(R.id.job_list)
        jobList?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        jobList?.adapter = jobadapter

        colJobRef
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (document in result) {
                    val item = JobModel(
                        document.data["title"].toString(),
                        document.data["add_text"].toString(),
                        document.data["term"].toString(),
                        document.data["money"].toString(),
                        document.data["nation"].toString(),
                        document.data["sex"].toString(),
                        document.data["updated_at"].toString(),
                        document.data["age"].toString(),
                        document.data["extra_text"].toString(),
                        document.data["email"].toString(),
                        document.data["job_id"].toString()
                    )
                    itemList.add(item)

                }
                jobadapter.notifyDataSetChanged() // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                android.util.Log.w("MainActivity", "Error getting documents: $exception")
            }

        jobadapter.setOnShowButtonClickListener(object : JobAdapter.OnShowButtonClickListener {
            override fun onShowButtonClick(item: JobModel) {
                val context = requireContext()
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("title", item.jobTitle)
                intent.putExtra("addtext", item.jobAddtext)
                intent.putExtra("money", item.jobMoney)
                intent.putExtra("term", item.jobTerm)
                intent.putExtra("sex", item.jobSex)
                intent.putExtra("nation", item.jobNation)
                intent.putExtra("update", item.updatedAt)
                intent.putExtra("age", item.jobAge)
                intent.putExtra("extratext", item.jobExtratext)
                context.startActivity(intent)
            }
        })

        jobadapter.setOnApplyButtonClickListener(object : JobAdapter.OnApplyButtonClickListener {
            override fun onApplyButtonClick(
                resumeId: String?,
                email: String?,
                jobEmail: String?,
                jobId: String?,
                item: JobModel
            ) {

                saveApplyData(resumeId, email, jobEmail, jobId)

                val content = requireContext()
                val intent = Intent(context, PopupActivity::class.java)
                intent.putExtra("title", item.jobTitle)

                /*val intent = Intent(context, ResumePopupActivity::class.java)
                intent.putExtra("title", item.jobTitle)
                intent.putExtra("email", email)
                intent.putExtra("jobId", jobId)
                intent.putExtra("jobEmail", jobEmail)*/

                content.startActivity(intent)
            }
        })
/*

        resumeadapter.setOnChoiceButtonClickListener(object : ResumeAdapter.OnChoiceButtonClickListener{
            override fun onChoiceButtonClick(
                resumeId: String?
            ) {

                val intent = intent
                val title = intent.getStringExtra("title")
                val email = intent.getStringExtra("email")
                val jobId = intent.getStringExtra("jobId")
                val jobEmail = intent.getStringExtra("jobEmail")

                saveApplyData(resumeId, email, jobEmail, jobId)

                val content = requireContext()
                val intent = Intent(context, PopupActivity::class.java)
                intent.putExtra("title", item.jobTitle)
                intent.putExtra("email", email)
                intent.putExtra("jobId", jobId)
                intent.putExtra("jobEmail", jobEmail)

                content.startActivity(intent)
            }
        })
*/

        // 로그아웃 처리
        auth = Firebase.auth

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    Firebase.auth.signOut()
                    startActivity(Intent(activity, LoginActivity::class.java)) // 로그인 화면으로 빠지기
                    true
                }

                else -> false
            }
        }
        setHasOptionsMenu(true)
    }

    private fun saveApplyData(
        resumeId: String?,
        email: String?,
        jobEmail: String?,
        jobId: String?
    ) {
        val applyDate = hashMapOf(
            "resume_id" to resumeId,
            "applicant" to email,

            //이거 두 개 왜인지 모르겟는데 반대로 데이터 들어가서 그냥 값 바꿔놈,,이유 못찾는중,,
            "owner" to jobId,
            "job_id" to jobEmail
        )
        val applyCollection = db.collection("apply")
        applyCollection.add(applyDate)
            .addOnSuccessListener { documentReference ->
                Log.e("apply", "Apply data saved with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.e("apply", "Error saving apply data", e)
            }

    }
}
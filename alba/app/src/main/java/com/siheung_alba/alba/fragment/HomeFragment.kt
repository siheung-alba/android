package com.siheung_alba.alba.fragment

import android.app.Activity
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
                            document.data["job_id"].toString(),
                            document.data["email"].toString(),
                            document.data["work_day"].toString(),
                            document.data["work_time"].toString(),
                            document.data["preference"].toString(),
                            document.data["education"].toString(),
                            document.data["owner_name"].toString(),
                            document.data["owner_phone"].toString()
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
                    intent.putExtra("workday", item.workDay)
                    intent.putExtra("worktime", item.workTime)
                    intent.putExtra("preference", item.preference)
                    intent.putExtra("education", item.education)
                    intent.putExtra("ownername", item.ownerName)
                    intent.putExtra("ownerphone", item.ownerPhone)
                    context.startActivity(intent)
                }
            })

            jobadapter.setOnApplyButtonClickListener(object :
                JobAdapter.OnApplyButtonClickListener {
                override fun onApplyButtonClick(
                    resumeId: String?,
                    email: String?,
                    jobEmail: String?,
                    jobId: String?,
                    item: JobModel
                ) {

                    Log.e("check", "확인111111111111111111111111111111111111111111111")

                    saveApplyData(resumeId, email, jobEmail, jobId)

                    val content = requireContext()
                    val intent = Intent(context, PopupActivity::class.java)
                    intent.putExtra("title", item.jobTitle)


                    Log.e("check", "확인2222222222222222222222222222222222222222222222222222")

                    content.startActivity(intent)


                    Log.e("check", "확인33333333333333333333333333333333333333333333")
                }
            })

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
        jobId: String?,
        jobEmail: String?
    ) {
        val applyDate = hashMapOf(
            "resume_id" to resumeId,
            "applicant" to email,
            "job_id" to jobId,
            "owner" to jobEmail
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
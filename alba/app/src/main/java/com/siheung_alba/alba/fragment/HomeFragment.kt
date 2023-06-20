package com.siheung_alba.alba.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.LoginActivity
import com.siheung_alba.alba.activity.PopupActivity
import com.siheung_alba.alba.adapter.JobAdapter
import com.siheung_alba.alba.model.JobModel


class HomeFragment : Fragment() {

    private val db = Firebase.firestore
    private val colJobRef = db.collection("job")
    private val itemList = arrayListOf<JobModel>()
    private val adapter = JobAdapter(itemList)

    private lateinit var auth: FirebaseAuth // 파이어베이스

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.homeToolbar)
        toolbar.title = "채용공고"
        toolbar.inflateMenu(R.menu.home_menu)

        val jobList = view?.findViewById<RecyclerView>(R.id.job_list)
        jobList?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        jobList?.adapter = adapter

        colJobRef
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for(document in result) {
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
                        document.data["job_id"].toString()
                    )
                    itemList.add(item)

                }
                adapter.notifyDataSetChanged() // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                android.util.Log.w("MainActivity", "Error getting documents: $exception")
            }

        adapter.setOnShowButtonClickListener(object : JobAdapter.OnShowButtonClickListener {
            override fun onShowButtonClick(item: JobModel) {
                val context = requireContext()
                val intent = Intent(context, PopupActivity::class.java)
                intent.putExtra("title", item.jobTitle)
                intent.putExtra("addtext", item.jobAddtext)
                intent.putExtra("money", item.jobMoney)
                intent.putExtra("term", item.jobTerm)
                intent.putExtra("sex", item.jobSex)
                intent.putExtra("nation", item.jobNation)
                intent.putExtra("update", item.updatedAt)
                intent.putExtra("age", item.jobAge)
                intent.putExtra("extratext", item.jobExtratext)
                intent.putExtra("job_id", item.job_id)
                context.startActivity(intent)
            }
        })

        // 로그아웃 처리
        auth = Firebase.auth

        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId) {
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
}
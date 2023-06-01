package com.siheung_alba.alba.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.PopupActivity
import com.siheung_alba.alba.model.JobAdapter
import com.siheung_alba.alba.model.JobModel


class HomeFragment : Fragment() {

    private val db = Firebase.firestore
    private val colJobRef = db.collection("job")
    private val itemList = arrayListOf<JobModel>()
    private val adapter = JobAdapter(itemList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.homeToolbar)
        toolbar.title = "채용공고"

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
                        document.data["extra_text"].toString()
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
                context.startActivity(intent)
            }
        })
    }
}
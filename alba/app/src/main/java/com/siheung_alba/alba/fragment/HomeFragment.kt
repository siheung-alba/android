package com.siheung_alba.alba.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.adapter.JobAdapter
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
                        document.data["updated_at"].toString()
                    )
                    itemList.add(item)
                }
                adapter.notifyDataSetChanged() // 리사이클러 뷰 갱신
            }
            .addOnFailureListener { exception ->
                android.util.Log.w("MainActivity", "Error getting documents: $exception")
            }

    }

}
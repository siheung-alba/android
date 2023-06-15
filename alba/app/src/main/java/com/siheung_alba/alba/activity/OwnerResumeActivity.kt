package com.siheung_alba.alba.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import com.siheung_alba.alba.adapter.ApplyAdapter
import com.siheung_alba.alba.adapter.JobAdapter
import com.siheung_alba.alba.databinding.ActivityOwnerResumeBinding
import com.siheung_alba.alba.databinding.ActivityShopJoinBinding
import com.siheung_alba.alba.model.ApplyModel
import com.siheung_alba.alba.model.JobModel
class OwnerResumeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOwnerResumeBinding
    private lateinit var recyclerView: RecyclerView
    private val itemList = arrayListOf<ApplyModel>()
    private lateinit var applyAdapter: ApplyAdapter
    private val applyadapter = ApplyAdapter(itemList)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOwnerResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = binding.ownerResume
        toolbar.title = "지원된 이력서"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        applyAdapter = ApplyAdapter(itemList)

        recyclerView = binding.applyList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = applyAdapter

        val colJobRef = Firebase.firestore.collection("apply")

        colJobRef
            .get()
            .addOnSuccessListener { result ->
                itemList.clear()
                for (document in result) {
                    val item = ApplyModel(
                        document.data["applicant"].toString(),
                        document.data["job_id"].toString(),
                        document.data["owner"].toString(),
                        document.data["resume_id"].toString(),
                    )
                    itemList.add(item)
                }
                applyAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("error", "Error getting documents: $exception")
            }

        applyAdapter.setOnDetailResumeClickListener(object : ApplyAdapter.OnDetailResumeClickListener {
            override fun onDetailResumeClick(item: ApplyModel) {
                val intent = Intent(this@OwnerResumeActivity, OwnerResumeShowActivity::class.java)
                intent.putExtra("resume_id", item.resume_id)
                intent.putExtra("applicant", item.applicant)

                Log.e("check", "11111111111111111111111111111111111111111111111111111")
                Log.e("check", "resume_id:${item.resume_id}")

                startActivity(intent)
            }
        })
    }
}

package com.siheung_alba.alba.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.OwnerResumeActivity
import com.siheung_alba.alba.activity.OwnerUploadActivity
import com.siheung_alba.alba.activity.ResumeUploadActivity

class MyPageForOwnerFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_page_for_owner, container, false)
        val uploadBtn : Button = view.findViewById(R.id.btn_upload)
        val toolbar : Toolbar = view.findViewById(R.id.ownerPageToolbar)

        toolbar.title = "마이페이지"
        toolbar.inflateMenu(R.menu.owner_page_menu)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.owner_page_menu -> {
                    val intent = Intent(activity, OwnerResumeActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
        setHasOptionsMenu(true)

        uploadBtn.setOnClickListener {
            val intent = Intent(activity, OwnerUploadActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}
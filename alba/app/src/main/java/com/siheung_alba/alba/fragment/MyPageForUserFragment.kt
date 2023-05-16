package com.siheung_alba.alba.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import com.siheung_alba.alba.activity.CalculateSalaryActivity
import com.siheung_alba.alba.R
import com.siheung_alba.alba.activity.ResumeUploadActivity

class MyPageForUserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_my_page_for_user, container, false)
        val uploadBtn : Button = view.findViewById(R.id.resume_upload_btn)
        val toolbar: Toolbar = view.findViewById(R.id.myPageToolbar)

        toolbar.title = "마이페이지"
        toolbar.inflateMenu(R.menu.my_page_menu)
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

        uploadBtn.setOnClickListener {
            val intent = Intent(activity, ResumeUploadActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}
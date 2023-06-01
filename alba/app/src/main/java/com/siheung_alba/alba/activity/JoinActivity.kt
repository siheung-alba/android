package com.siheung_alba.alba.activity

import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.siheung_alba.alba.R
import com.siheung_alba.alba.fragment.HomeFragment
import java.util.*



class JoinActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth // 파이어베이스

    var dateString = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        auth = Firebase.auth // 파이어베이스

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        var isGoToJoin = true

        // 가입
        var editname = findViewById<EditText>(R.id.editName)
        var email = findViewById<EditText>(R.id.editEmail)
        var edittell = findViewById<EditText>(R.id.editTell)
        var pwd = findViewById<EditText>(R.id.editPassword)
        var pwd2 = findViewById<EditText>(R.id.editPassword2)
        var male = findViewById<RadioButton>(R.id.male)
        var female = findViewById<RadioButton>(R.id.female)



        // 가입버튼
        val startjoinbutton = findViewById<Button>(R.id.startJoinButton)

        // 날짜, 국적
        var editdate = findViewById<Button>(R.id.editDate)
        var editdateresult = findViewById<TextView>(R.id.editDateResult)
        var nationspinner = findViewById<Spinner>(R.id.nationSpinner)

        startjoinbutton.setOnClickListener {
            // 가입
            var editname = editname.text.toString()
            var email = email.text.toString()
            var edittell = edittell.text.toString()
            var pwd = pwd.text.toString()
            var pwd2 = pwd2.text.toString()
            var editdateresultjoin = editdateresult.text.toString()
            var nationspinnerjoin = nationspinner.getSelectedItem().toString();
            /*var male = male.text.toString();
            var female = female.text.toString();*/


            // 값이 비어있는지 확인

            if(editname.isEmpty()) {
                Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(email.isEmpty()) {
                Toast.makeText(this, "이메일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(edittell.isEmpty()) {
                Toast.makeText(this, "전화번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(pwd.isEmpty()) {
                Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(pwd2.isEmpty()) {
                Toast.makeText(this, "비밀번호 재입력을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(!pwd.equals(pwd2)) {
                Toast.makeText(this, "비밀번호가 같지 않습니다. 다시 입력해주세요. ", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(editdateresultjoin.isEmpty()) {
                Toast.makeText(this, "생년월일을 입력해주세요.", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            if(nationspinnerjoin == "선택하세요.") {
                Toast.makeText(this, "국적을 입력해주세요", Toast.LENGTH_LONG).show()
                isGoToJoin = false
            }

            /* if(male.isEmpty() && female.isEmpty()) {
                Toast.makeText(this, "성별을 입력해주세요", Toast.LENGTH_LONG).show()
            }*/

            if(isGoToJoin) {

                auth.createUserWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "생성되었습니다.", Toast.LENGTH_LONG).show()

                            val intent = Intent(this, MainForUserActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                            val database = Firebase.database
                            val myRef = database.getReference("message")

                            myRef.setValue(email)

                        } else {
                            Toast.makeText(this, "실패하였습니다.", Toast.LENGTH_LONG).show()
                            Log.e(TAG, "createUserWithEmail:failure", task.exception)
                        }
                    }

            }







        }


        // 날짜, 국적
        editdate.setOnClickListener {
            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
                editdateresult.text = "날짜 :" + dateString
            }
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        var NationData = resources.getStringArray(R.array.nationality_array)
        var adapterNation = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, NationData)

        nationspinner.adapter = adapterNation
    }
}

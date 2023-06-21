package com.siheung_alba.alba.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.siheung_alba.alba.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// 공고 수정, 삭제하기
class OwnerResumePageActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore

    private val current = LocalDateTime.now()
    private val formatter = DateTimeFormatter.ofPattern("M/d")
    private val formatted = current.format(formatter)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_resume_home)


        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        val userEmail = user?.email
        val storeTitle : EditText = findViewById(R.id.edt_store)  // 매장
        val term : EditText = findViewById(R.id.edtTerm)  // 근무기간
        val money : EditText = findViewById(R.id.edtMoney) // 시급
        val age : EditText = findViewById(R.id.edtAge) // 나이
        val sex : EditText = findViewById(R.id.edtSex) // 성별
        val nation : EditText = findViewById(R.id.edtNation) // 국적
        val addText : EditText = findViewById(R.id.edt_add_text) // 제목
        val detail : EditText = findViewById(R.id.edt_detail) // 추가내용



        // 수정 받아오고 출력
        val  titleEdit = intent.getStringExtra("titleEdit") // 매장
        val  jobAddtextEdit = intent.getStringExtra("jobAddtextEdit") // 제목
        val  jobTermEdit = intent.getStringExtra("jobTermEdit") // 근무기간
        val  jobMoneyEdit = intent.getStringExtra("jobMoneyEdit") // 시급
        val  jobAgeEdit = intent.getStringExtra("jobAgeEdit")  // 나이
        val  jobSexEdit = intent.getStringExtra("jobSexEdit") // 성별
        val  jobNationEdit = intent.getStringExtra("jobNationEdit") // 국적
        val  jobExtratextEdit = intent.getStringExtra("jobExtratextEdit") //추가 내용
        val jobId = intent.getStringExtra("job_id")


        storeTitle.setText(titleEdit)
        addText.setText(jobAddtextEdit)
        term.setText(jobTermEdit)
        money.setText(jobMoneyEdit)
        age.setText(jobAgeEdit)
        sex.setText(jobSexEdit)
        nation.setText(jobNationEdit)
        detail.setText(jobExtratextEdit)



        // 수정버튼 클릭시 이력서 업데이트
        val checkJobBtn: Button = findViewById(R.id.checkJobBtn)
        checkJobBtn.setOnClickListener {
            // 수정된 내용을 저장
            val editedStoreTitle = storeTitle.text.toString()
            val editedTerm = term.text.toString()
            val editedMoney = money.text.toString()
            val editedAge = age.text.toString()
            val editedSex = sex.text.toString()
            val editedNation = nation.text.toString()
            val editedAddtext = addText.text.toString()
            val editedDetail = detail.text.toString()

            val OwnerData = hashMapOf<String, Any>(
                "title" to editedStoreTitle,
                "term" to editedTerm,
                "money" to editedMoney,
                "age" to editedAge,
                "sex" to editedSex,
                "nation" to editedNation,
                "add_text" to editedAddtext,
                "extra_text" to editedDetail,
                "created_at" to formatted,
                "updated_at" to formatted
            )

            val collectionRef = db.collection("job")
            val query = collectionRef.whereEqualTo("job_id", jobId)



            /*query.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        val documentId = document.id
                        // 문서 ID를 이용하여 필요한 작업 수행
                        // ...

                        db.collection("job")
                            .document(documentId!!)
                            .update(OwnerData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "이력서가 성공적으로 업데이트되었습니다", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("OwnerResumeActivity", "Error updating resume: $exception")
                    Toast.makeText(this, "이력서 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show()
                }*/

            collectionRef.document(jobId!!)
                .update(OwnerData)
                .addOnSuccessListener {
                    Toast.makeText(this, "이력서가 성공적으로 업데이트되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("OwnerResumeActivity", "Error updating resume: $exception")
                    Toast.makeText(this, "이력서 업데이트에 실패했습니다", Toast.LENGTH_SHORT).show()
                }







        }

        // 삭제버튼 클릭시
        val deleteJobBtn: Button = findViewById(R.id.deleteJobBtn)
        deleteJobBtn.setOnClickListener {

            val collectionRef = db.collection("job")


            collectionRef.document(jobId!!)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "이력서가 성공적으로 삭제되었습니다", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { exception ->
                    Log.e("OwnerResumeActivity", "Error deleting resume: $exception")
                    Toast.makeText(this, "이력서 삭제에 실패했습니다", Toast.LENGTH_SHORT).show()
                }




        }



    }
}
package com.example.termproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.remote.Datastore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val Name = findViewById<EditText>(R.id.Name_Profile).text.toString()
        val Phone = findViewById<EditText>(R.id.Phone_Profile).text.toString()
        val Birth = findViewById<EditText>(R.id.Birthday_Profile).text.toString()
        val Email = findViewById<EditText>(R.id.Email_Profile).text.toString()


        findViewById<Button>(R.id.Save_Profile).setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java) //intent 생성 this에서 homeActivity로 이동
            val user = Firebase.auth.currentUser
            val db: FirebaseFirestore = Firebase.firestore

            println("######회원 정보 등록 시도")
            println("${Name}, ${Phone.length}, ${Birth.length}, ${Email.length}, ")
//            if(Name.length > 0 && Phone.length > 0 && Birth.length > 0 && Email.length > 0) {
                val memberinfo = MemberInfo(Name, Phone, Birth, Email)
                println("######회원 정보 등록 시도1")
                if (user != null) {
                    println("######회원 정보 등록 시도2")
                    db.collection("users").document(user.uid).set(memberinfo)
                        .addOnSuccessListener {
                            println("######회원 정보 등록 성공")
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            println("######회원 정보 등록 실패")
                            startActivity(intent)
                        }
                }
//            }
        }



    }
}
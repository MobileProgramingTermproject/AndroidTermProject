package com.example.termproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.remote.Datastore
import com.google.firebase.ktx.Firebase

class ProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        var member = MemberInfo()
        if(user == null){
            println("######User 없음")
        }else {
            val usersdb = db.collection("users").document(user.uid)
            println("######User ${user.uid}")
            usersdb.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        member.Name = document.data?.get("name").toString()
                        member.Phone = document.data?.get("phone").toString()
                        member.Birth = document.data?.get("birth").toString()
                        member.Email = document.data?.get("email").toString()
                        println("######DB 읽음")
                            findViewById<EditText>(R.id.Name_Profile).setText(member.Name)
                            findViewById<EditText>(R.id.Phone_Profile).setText(member.Phone)
                            findViewById<EditText>(R.id.Birthday_Profile).setText(member.Birth)
                            findViewById<EditText>(R.id.Email_Profile).setText(member.Email)
                    } else {
                        println("######DB 없음")
                    }
                }
                .addOnFailureListener { exception ->
                }
        }


        findViewById<Button>(R.id.Save_Profile).setOnClickListener {
            val Name = findViewById<EditText>(R.id.Name_Profile).text.toString()
            val Phone = findViewById<EditText>(R.id.Phone_Profile).text.toString()
            val Birth = findViewById<EditText>(R.id.Birthday_Profile).text.toString()
            val Email = findViewById<EditText>(R.id.Email_Profile).text.toString()
            val intent = Intent(this, HomeActivity::class.java) //intent 생성 this에서 homeActivity로 이동


            println("######회원 정보 등록 시도")
            println("${Name.length}, ${Phone.length}, ${Birth.length}, ${Email.length}, ")
            if(Name.length > 0 && Phone.length > 0 && Birth.length > 0 && Email.length > 0) {
                val memberinfo = MemberInfo(user?.uid, Name, Phone, Birth, Email)
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
            }
        }

    }
}
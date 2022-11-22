package com.example.termproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MembershipActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.membership_layout)  //회원가입 레이아웃으로

        val Id = findViewById<EditText>(R.id.ID).text
        val Password = findViewById<EditText>(R.id.Password).text

        findViewById<Button>(R.id.Check).setOnClickListener {   //비밀번호 확인
            if (Password.toString()
                    .equals(findViewById<EditText>(R.id.PasswordCheck).text.toString())
            ) {
                println("######Password Checked");
                findViewById<TextView>(R.id.checktextView).setText("Checked")   //일치하면 표시
            } else{
                findViewById<EditText>(R.id.PasswordCheck).setText("")          //불일치 시 비밀번호 확인 EditTextView 비우기
                findViewById<TextView>(R.id.checktextView).setText("Check Failed")   //일치하면 표시
           }
        }

        findViewById<Button>(R.id.Membership_btn).setOnClickListener {
            if(findViewById<TextView>(R.id.checktextView).text.toString().equals("Checked")) { //비밀번호 확인이 됬으면 Firebase Signup 시도
                Firebase.auth.createUserWithEmailAndPassword(Id.toString()+"@29.com", Password.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            println("######Sign-up Success")
                            Firebase.auth.signInWithEmailAndPassword(Id.toString()+"@29.com", Password.toString())

                            val user = Firebase.auth.currentUser
                            val db = Firebase.firestore
                            println("######User ${user?.uid}")

                            val Name = findViewById<EditText>(R.id.Name).text.toString()
                            val Phone = findViewById<EditText>(R.id.PhoneNum).text.toString()
                            val Birth = findViewById<EditText>(R.id.Birth).text.toString()
                            val Email = findViewById<EditText>(R.id.Email).text.toString()

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
                                            val intent = Intent(this, HomeActivity::class.java) //intent 생성 this에서 homeActivity로 이동
                                            startActivity(intent)
                                        }
                                        .addOnFailureListener {
                                            println("######회원 정보 등록 실패")
                                        }
                                }
                            }
                        } else {
                            println("######Sign-up Login Failed ${it.exception?.message}")
                            findViewById<TextView>(R.id.checktextView).setText("Signup failed ${it.exception?.message}")
                        }
                    }
            }else{
                findViewById<TextView>(R.id.checktextView).setText("Checked Please")

            }
        }
    }
}
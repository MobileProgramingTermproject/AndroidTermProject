package com.example.termproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() { //로그인
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout) //login_layout 열기

        val ID = findViewById<EditText>(R.id.editTextTextPersonName).text
        val PassWord = findViewById<EditText>(R.id.editTextTextPassword).text

        findViewById<Button>(R.id.button_login).setOnClickListener{ //로그인 버튼 클릭
            Firebase.auth.signInWithEmailAndPassword(ID.toString(), PassWord.toString()) //파이어베이스 로그인 시도
                .addOnCompleteListener {
                    if(it.isSuccessful) {//성공 시 표시
                        println("######Login Success ${ID} ${PassWord}")
                        findViewById<TextView>(R.id.CheckLogin).setText("Login Success ${ID} ${PassWord}") //화면에 표시
                        setContentView(R.layout.main_layout)
                        val intent2 = Intent(this, HomeActivity::class.java) //intent 생성 this에서 homeActivity로 이동
                        startActivity(intent2)
                    }else {//실패 시 표시
                        println("######Login Failed ${it.exception?.message} ${ID} ${PassWord}")
                        findViewById<TextView>(R.id.CheckLogin).setText("Login Failed ${it.exception?.message} ${ID} ${PassWord}") //화면에 표시
                    }
                }
        }
        findViewById<TextView>(R.id.membershiptxt).setOnClickListener { //회원가입 클릭 시 membership액티비티로
            val intent = Intent(this, MembershipActivity::class.java) //intent 생성 this에서 MembershipActivity로 이동
            startActivity(intent)
        }
        



    }
}
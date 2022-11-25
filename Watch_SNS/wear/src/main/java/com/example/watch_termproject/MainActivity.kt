package com.example.watch_termproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var actionbar : ActionBar
        actionbar = supportActionBar!!
        actionbar.hide()

        val id = findViewById<EditText>(R.id.Id_edittext).text
        val pw = findViewById<EditText>(R.id.Pw_edittext).text

        findViewById<Button>(R.id.button_login).setOnClickListener {
            Firebase.auth.signInWithEmailAndPassword(id.toString()+"@29.com", pw.toString()) //파이어베이스 로그인 시도
                .addOnCompleteListener {
                    if(it.isSuccessful) {//성공 시 표시
                        val intent = Intent(this, HomeActivity::class.java) //intent 생성 this에서 homeActivity로 이동
                        startActivity(intent)
                    }else {//실패 시 표시
                    }
                }
        }

    }
}
package com.example.termproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout) //login_layout 열기

        findViewById<Button>(R.id.Button_Posting).setOnClickListener {
            val intent2 = Intent(this, AddPostActivity::class.java) //intent 생성 this에서 MembershipActivity로 이동
            startActivity(intent2)
        }
    }
}
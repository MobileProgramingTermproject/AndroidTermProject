package com.example.termproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity:AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout) //login_layout 열기

        findViewById<Button>(R.id.Button_Profile).setOnClickListener {
            val intent1 = Intent(this, ProfileActivity::class.java) //intent 생성 this에서 ProfileActivity로 이동
            startActivity(intent1)
        }
        findViewById<Button>(R.id.Button_Posting).setOnClickListener {
            val intent2 = Intent(this, AddPostActivity::class.java) //intent 생성 this에서 PostingActivity로 이동
            startActivity(intent2)
        }

        findViewById<Button>(R.id.Button_Logout).setOnClickListener {
            val intent3 = Intent(this, MainActivity::class.java) //intent 생성 this에서 MainActivity로 이동
            startActivity(intent3)
        }

        findViewById<Button>(R.id.Button_Friends).setOnClickListener {
            val intent4 = Intent(this, FriendsActivity::class.java) //intent 생성 this에서 FriendsActivity로 이동
            startActivity(intent4)
        }
    }
}
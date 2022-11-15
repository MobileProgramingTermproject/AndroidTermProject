package com.example.termproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class HomeActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout) //login_layout 열기
    }
}
package com.example.termproject

import android.app.Activity
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class AddPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addpost_layout)
        //throw RuntimeException("################")

        val rootRef = Firebase.storage.reference

        val ref = rootRef.child("sangsangbugi.png")
        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
            if (it.isSuccessful) {
                val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result!!.size)
                val imgView = findViewById<ImageView>(R.id.imageView)
                imgView.setImageBitmap(bmp)
                finish();
            }
        }
    }
}

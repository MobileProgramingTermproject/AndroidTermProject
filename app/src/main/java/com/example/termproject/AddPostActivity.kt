package com.example.termproject

import android.content.ContentUris
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class AddPostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addpost_layout)
        
        //throw RuntimeException("######")
        
        val rootRef = Firebase.storage.reference
        
        val ref = rootRef.child("sangsangbugi.png")
        ref.getBytes(Long.MAX_VALUE).addOnCompleteListener{
            if (it.isSuccessful){
                BitmpaFactory.decodeByteArray(it.result,0,it.result.size)
            }
        }
    }
        
}
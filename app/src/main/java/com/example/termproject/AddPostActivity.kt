package com.example.termproject

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.termproject.databinding.AddpostLayoutBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage


class AddPostActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    lateinit var binding: AddpostLayoutBinding

    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "sangsangbugi.png"
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        binding = AddpostLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Firebase.auth.currentUser ?: finish() //if not authenticated, finish this activity

        storage = Firebase.storage
        val storageRef = storage.reference // reference to root
        val imageRef1 = storageRef.child("upload_images/background.png")
        //imageRef1 indicate same object.

    }


}
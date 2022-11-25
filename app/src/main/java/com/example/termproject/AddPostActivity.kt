package com.example.termproject

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.sns_project.PostInfo
import com.example.termproject.databinding.AddpostLayoutBinding

import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class AddPostActivity : AppCompatActivity() {
    lateinit var title: EditText
    lateinit var imageIv: ImageView
    lateinit var textEt: EditText
    lateinit var uploadBtn: Button
    lateinit var buttonImage: Button
    lateinit var name: String
    lateinit var uid: String

    var IMAGE_PICK = 1111
    var selectImage: Uri? = null

    lateinit var storage: FirebaseStorage
    lateinit var firestore: FirebaseFirestore
    lateinit var binding: AddpostLayoutBinding

    companion object {
        const val REQUEST_CODE = 1
        const val UPLOAD_FOLDER = "upload_images/"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addpost_layout)

        val user = Firebase.auth.currentUser
        val db = Firebase.firestore

        db.collection("users").document(user!!.uid).get()
            .addOnSuccessListener {
                name = it.data?.get("name").toString()
                uid = it.data?.get("uid").toString()
            }


        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        title = findViewById(R.id.Title)
        buttonImage = findViewById(R.id.Button_Image)
        imageIv = findViewById(R.id.image_iv)
        textEt = findViewById(R.id.text_et)
        uploadBtn = findViewById(R.id.upload_btn)
        var imageUrl = String()

        buttonImage.setOnClickListener {
            var intent = Intent(Intent.ACTION_PICK) //선택하면 무엇인가 띄운다.
            intent.type = "image/*"
            startActivityForResult(intent, IMAGE_PICK)
        }
        uploadBtn.setOnClickListener {
            if (selectImage != null) {
                var fileName =
                    SimpleDateFormat("yyyyMMddHHmmss").format(Date()) //파일명 겹치면 안 되므로 년월일초 지정
                storage.getReference().child("image").child(fileName)
                    .putFile(selectImage!!) //어디에 업로드할지 지정
                    .addOnSuccessListener { taskSnapshot -> // 업로드 정보를 담는다
                        taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { it ->
                            if(it != null) {
                                imageUrl = it.toString()
                            }
                            var post = PostInfo(title.text.toString(),textEt.text.toString(), LocalDateTime.now().toString(), uid, imageUrl, name)
                            firestore.collection("post")
                                .document().set(post)
                                .addOnSuccessListener {
                                    finish()
                                }
                        }
                    }
            }else {
                var post = PostInfo(title.text.toString(),textEt.text.toString(), LocalDateTime.now().toString(), uid, imageUrl, name)
                firestore.collection("post")
                    .document().set(post)
                    .addOnSuccessListener {
                        finish()
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            selectImage = data?.data
            imageIv.setImageURI(selectImage)

        }
    }
}

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
                var post = PostInfo(title.text.toString(),textEt.text.toString(), Date().toString(), uid, imageUrl, name)
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



//    lateinit var storage: FirebaseStorage
//    lateinit var binding: AddpostLayoutBinding
//    companion object {
//        const val REQUEST_CODE = 1
//        const val UPLOAD_FOLDER = "upload_images/"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        //throw RuntimeException("################")
//
//
//
//
//        binding = AddpostLayoutBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        Firebase.auth.currentUser ?: finish()  // if not authenticated, finish this activity
//
//        storage = Firebase.storage
//        val storageRef = storage.reference // reference to root
//        val imageRef1 = storageRef.child("/upload_images/no_image.png")
//        // imageRef1 and imageRef2 indicate same object.
//        displayImageRef(imageRef1, binding.imageView)
//
//        binding.ButtonUpload.setOnClickListener {
//            uploadDialog()
//        }
//
//        binding.ButtonImage.setOnClickListener {
//            listPhotosDialog()
//            val rootRef = Firebase.storage.reference
//
//            val ref = rootRef.child("/upload_images/kotlin.png")
//            ref.getBytes(Long.MAX_VALUE).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    val bmp = BitmapFactory.decodeByteArray(it.result,0,it.result!!.size)
//                    val imgView = findViewById<ImageView>(R.id.image_iv)
//                    imgView.setImageBitmap(bmp)
//                }
//            }
//        }
//    }
//    private fun uploadDialog() {
//        println("111111111111111")
//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//            == PackageManager.PERMISSION_GRANTED) {
//            println("22222222222222")
//            val cursor = contentResolver.query(
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, null, null, null)
//
//            AlertDialog.Builder(this)
//                .setTitle("Choose Photo")
//                .setCursor(cursor, { _, i ->
//                    cursor?.run {
//                        moveToPosition(i)
//                        val idIdx = getColumnIndex(MediaStore.Images.ImageColumns._ID)
//                        val nameIdx = getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME)
//                        uploadFile(getLong(idIdx), getString(nameIdx))
//                    }
//                }, MediaStore.Images.ImageColumns.DISPLAY_NAME).create().show()
//        } else {
//            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                AddPostActivity.REQUEST_CODE
//            )
//        }
//    }
//
//    private fun listPhotosDialog() {
//        storage.reference.child(AddPostActivity.UPLOAD_FOLDER).listAll()
//            .addOnSuccessListener {
//                val itemsString = mutableListOf<String>()
//                for (i in it.items) {
//                    itemsString.add(i.name)
//                }
//                AlertDialog.Builder(this)
//                    .setTitle("Uploaded Photos")
//                    .setItems(itemsString.toTypedArray(), {_, i -> }).show()
//            }.addOnFailureListener {
//
//            }
//    }
//
//    private fun uploadFile(file_id: Long?, fileName: String?) {
//        file_id ?: return
//        val imageRef = storage.reference.child("${AddPostActivity.UPLOAD_FOLDER}${fileName}")
//        val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, file_id)
//        imageRef.putFile(contentUri).addOnCompleteListener {
//            if (it.isSuccessful) {
//                // upload success
//                Snackbar.make(binding.root, "Upload completed.", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int,
//                                            permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == AddPostActivity.REQUEST_CODE) {
//            if ((grantResults.isNotEmpty() &&
//                        grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                uploadDialog()
//            }
//        }
//    }
//
//    private fun displayImageRef(imageRef: StorageReference?, view: ImageView) {
//        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
//            val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
//            view.setImageBitmap(bmp)
//        }?.addOnFailureListener {
//            // Failed to download the image
//        }
//    }


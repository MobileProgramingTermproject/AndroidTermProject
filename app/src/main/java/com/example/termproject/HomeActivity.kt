package com.example.termproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeActivity:AppCompatActivity(),PhotoAdapter.OnItemClickListener {

    lateinit var email: TextView
    lateinit var auth:FirebaseAuth

    lateinit var addPhotoBtn: Button
    lateinit var listRv: RecyclerView

    lateinit var photoAdapter:PhotoAdapter
    lateinit var photoList:ArrayList<Photo>

    lateinit var firestore: FirebaseFirestore

//    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

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

        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.email_tv)
        email.text = auth.currentUser?.email

        firestore = FirebaseFirestore.getInstance()

        addPhotoBtn=findViewById(R.id.add_photo_btn)
        listRv=findViewById(R.id.list_rv)

        photoList= ArrayList()
        photoAdapter=PhotoAdapter(this,photoList)

        listRv.layoutManager= GridLayoutManager(this, 3)
        listRv.adapter=photoAdapter

        photoAdapter.onItemClickListener=this

        firestore.collection("photo").addSnapshotListener {
                querySnapshot, FirebaseFIrestoreException ->
            if(querySnapshot!=null){
                for(dc in querySnapshot.documentChanges){
                    if(dc.type== DocumentChange.Type.ADDED){
                        var photo=dc.document.toObject(Photo::class.java)
                        photo.id=dc.document.id
                        photoList.add(photo)
                    }
                }
                photoAdapter.notifyDataSetChanged()
            }
        }

        addPhotoBtn.setOnClickListener {
            var intent= Intent(this,AddPostActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onItemClick(photo: Photo) {
        var intent=Intent(this,PhotoActivity::class.java)
        intent.putExtra("id",photo.id)
        startActivity(intent)
    }






}
package com.example.termproject

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_project.PostInfo
import com.example.termproject.databinding.ActivityFriendsListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity:AppCompatActivity() {
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore

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

        findViewById<Button>(R.id.Button_friendslist).setOnClickListener {
            val intent = Intent(this, FriendsListActivity::class.java) //intent 생성 this에서 FriendsActivity로 이동
            startActivity(intent)
        }

        setRecyclerView()

    }


    private fun setRecyclerView() {


        val postArray: ArrayList<PostInfo> = arrayListOf<PostInfo>()

        val docRef = db.collection("post").orderBy("createdAt").get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    postArray.add(
                        PostInfo(
                            document.data.get("title") as String,
                            document.data.get("text") as String,
                            document.data.get("date") as String,
                            document.data.get("uid") as String,
                            document.data.get("image") as String,
                            document.data.get("name") as String
                        )
                    )
                    val recyclerView = findViewById<RecyclerView>(R.id.postView)
                    val mainAdapter = MainAdapter(this, postArray)
                    recyclerView.adapter = mainAdapter

                    val layout = LinearLayoutManager(this)
                    recyclerView.layoutManager = layout
                    recyclerView.setHasFixedSize(true)
                }

            }
            .addOnFailureListener { exception ->
                println(exception)
            }

    }
}


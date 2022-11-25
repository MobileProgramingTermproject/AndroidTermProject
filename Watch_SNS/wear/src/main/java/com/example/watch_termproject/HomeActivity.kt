package com.example.watch_termproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    val user = Firebase.auth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        var actionbar : ActionBar
        actionbar = supportActionBar!!
        actionbar.hide()

        Firebase.firestore.collection("users").document(user!!.uid).get()
            .addOnSuccessListener {
                findViewById<TextView>(R.id.MyName).text = it.data?.get("name").toString()
            }

        setRecyclerView()

        findViewById<Button>(R.id.Sync).setOnClickListener {
            setRecyclerView()
        }

    }


    private fun setRecyclerView() {

        val postArray: ArrayList<PostInfo> = arrayListOf<PostInfo>()
        var friends: ArrayList<String> = arrayListOf<String>()

        Firebase.firestore.collection("users").document(user!!.uid).get()
            .addOnSuccessListener{ document ->
                if(document.data?.get("friends") == null){
                    friends.add(user!!.uid)
                }else {
                    friends = document.data?.get("friends") as ArrayList<String>
                    friends.add(user!!.uid)
                }
                Firebase.firestore.collection("post").orderBy("date", Query.Direction.DESCENDING).get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if (friends.contains(document.data.get("uid"))) {
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
                            }
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
}


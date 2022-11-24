package com.example.termproject


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.termproject.databinding.ActivityFriendsListBinding
import com.example.termproject.databinding.ItemFriendBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class FriendsListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFriendsListBinding
    private var data:ArrayList<Friendlist> = ArrayList()
    val user = Firebase.auth.currentUser
    val db = Firebase.firestore
    var frienduid = ArrayList<String> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFriendsListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initialize()

    }

    inner class FriendAdapter : RecyclerView.Adapter<Holder>(){

        var listData = ArrayList<Friendlist>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return Holder(binding)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val friend = listData[position]
            holder.setData(friend)
        }

        override fun getItemCount(): Int {
            return listData.size
        }
    }

    inner class Holder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root){
        fun setData(friend: Friendlist){
            binding.friendName.text = friend.name
            binding.friendPhonenum.text = friend.phone
            binding.friendBirthday.text = friend.birth
            binding.friendEmail.text = friend.email
        }
    }

    private fun initialize() {
        with(data){
            db.collection("users").document(user!!.uid).get()
                .addOnSuccessListener {
                    if (it.data != null) {
                        if (it.data!!.get("friends") != null) {
                            frienduid = it.data!!.get("friends") as ArrayList<String>
                            println("친구 있음")
                        }else {
                            frienduid = ArrayList<String>()
                            println("친구 없음")
                        }
                    }
                    db.collection("users")
                        .whereEqualTo("type", "user")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if(frienduid.contains(document.data.get("uid").toString())){
                                    data.add(Friendlist(document.data?.get("name").toString(), document.data?.get("phone").toString(), document.data?.get("birth").toString(), document.data?.get("email").toString()))
                                    println("친구 추가 중 ${data.size}")
                                }
                            }
                            setadapter()
                        }
                }
        }

    }

    private fun setadapter(){
        println("setAdapter 실행 중")
        val adapter = FriendAdapter()
        adapter.listData = data
        binding.Friendlist.adapter = adapter
        binding.Friendlist.layoutManager = LinearLayoutManager(this)
    }
}
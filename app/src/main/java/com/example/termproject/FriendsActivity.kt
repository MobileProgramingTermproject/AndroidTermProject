package com.example.termproject

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FriendsActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var searchRecyclerViewAdapter: SearchRecyclerViewAdapter
    lateinit var users: ArrayList<Friend>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        recyclerView = findViewById(R.id.searchlist)

        findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(searchViewTextListener)

        users = tempUsers()
        println("setAdapter 실행 전")
        setAdapter()

    }

    var searchViewTextListener: SearchView.OnQueryTextListener =
        object : SearchView.OnQueryTextListener {
            //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
            override fun onQueryTextSubmit(s: String): Boolean {
                searchRecyclerViewAdapter.getFilter().filter(s)
                return false
            }

            //텍스트 입력/수정시에 호출
            override fun onQueryTextChange(s: String): Boolean {
                searchRecyclerViewAdapter.getFilter().filter(s)
                return false
            }
        }

    inner class SearchRecyclerViewAdapter(var users: ArrayList<Friend>, var con: Context) :
        RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {
        var filteredUsers = ArrayList<Friend>()
        var itemFilter = ItemFilter()

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            lateinit var user_img_list: ImageView
            lateinit var user_uid_list: TextView
            lateinit var user_name_list: TextView
            lateinit var btn: Button

            init {
                user_img_list = itemView.findViewById(R.id.user_img_list)
                user_uid_list = itemView.findViewById(R.id.friend_name)
                user_name_list = itemView.findViewById(R.id.friend_phonenum)
                btn = itemView.findViewById<Button>(R.id.friend_add)
                val user = Firebase.auth.currentUser
                val db = Firebase.firestore

//                db.collection("users").document(user!!.uid)
//                    .get()
//                    .addOnSuccessListener {
//                        val friendlist: ArrayList<String> = it.data!!.get("friends") as ArrayList<String>
//                        if(friendlist.contains(user_uid_list.text.toString())){
//                            itemView.findViewById<Button>(R.id.friend_add).text = "친구 삭제"
//                        }else
//                            itemView.findViewById<Button>(R.id.friend_add).text = "친구 추가"
//                    }
//                    .addOnFailureListener {
//                    }

                println("ViewHolder 내용 ${user_name_list}")

//                if(itemView.findViewById<Button>(R.id.friend_add).text.equals("수락 대기")) {
//                    itemView.findViewById<Button>(R.id.friend_add).isClickable = false
//                }

                itemView.findViewById<Button>(R.id.friend_add).setOnClickListener {
                    //친구 추가
                    if (itemView.findViewById<Button>(R.id.friend_add).text.equals("친구 추가")) {
                        db.collection("users").document(user!!.uid)
                            .update(
                                "requestFriends",
                                FieldValue.arrayUnion(user_uid_list.text.toString())
                            )
                            .addOnSuccessListener {
                                println("${user_name_list.text.toString()} 친구 요청")
                                itemView.findViewById<Button>(R.id.friend_add).text = "수락 대기"
                                itemView.findViewById<Button>(R.id.friend_add).isClickable = false
                                users = tempUsers()
                            }
                            .addOnFailureListener {
                                println("${user_name_list.text.toString()} 친구 추가 실패")
                            }
                    } else if (itemView.findViewById<Button>(R.id.friend_add).text.equals("친구 요청")) {
                        db.collection("users").document(user!!.uid)
                            .update("friends", FieldValue.arrayUnion(user_uid_list.text.toString()))
                            .addOnSuccessListener {
                                println("${user_name_list.text.toString()} 친구 수락")
                                itemView.findViewById<Button>(R.id.friend_add).text = "친구 삭제"
                                users = tempUsers()
                            }
                            .addOnFailureListener {
                                println("${user_name_list.text.toString()} 친구 수락 실패")
                            }

                        db.collection("users").document(user!!.uid)
                            .update(
                                "requestfriends",
                                FieldValue.arrayRemove(user_uid_list.text.toString())
                            )
                            .addOnSuccessListener {
                                println("${user_name_list.text.toString()} 친구 수락")
                            }
                            .addOnFailureListener {
                                println("${user_name_list.text.toString()} 친구 수락 실패")
                            }

                        db.collection("users").document(user_uid_list.text.toString())
                            .update("friends", FieldValue.arrayUnion(user.uid.toString()))
                            .addOnSuccessListener {
                                println("${user_name_list.text.toString()} 친구 수락")
                            }
                            .addOnFailureListener {
                                println("${user_name_list.text.toString()} 친구 수락")
                            }

                    } else if (itemView.findViewById<Button>(R.id.friend_add).text.equals("수락 대기")) {
                        itemView.findViewById<Button>(R.id.friend_add).isClickable = false
                    } else {
                        db.collection("users").document(user!!.uid)
                            .update(
                                "friends",
                                FieldValue.arrayRemove(user_uid_list.text.toString())
                            )
                            .addOnSuccessListener {
                                println("${user_name_list.text.toString()} 친구 삭제")
                                itemView.findViewById<Button>(R.id.friend_add).text = "친구 추가"
                                users = tempUsers()
                            }
                            .addOnFailureListener {
                                println("${user_name_list.text.toString()} 친구 삭제 실패")
                            }
                    }
                }
            }
        }

        init {
            filteredUsers.addAll(users)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val con = parent.context
            val inflater = con.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.item_user, parent, false)

            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user: Friend = filteredUsers[position]

            holder.user_uid_list.text = user.uid
            holder.user_name_list.text = user.name
            holder.btn.text = user.button
            if (holder.btn.text.equals("수락 대기"))
                holder.btn.isClickable = false
        }

        override fun getItemCount(): Int {
            return filteredUsers.size
        }

        //-- filter
        fun getFilter(): Filter {
            return itemFilter
        }

        inner class ItemFilter : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filterString = charSequence.toString()
                val results = FilterResults()

                //검색이 필요없을 경우를 위해 원본 배열을 복제
                val filteredList: ArrayList<Friend> = ArrayList<Friend>()
                //공백제외 아무런 값이 없을 경우 -> 원본 배열
                if (filterString.trim { it <= ' ' }.isEmpty()) {
                    results.values = users
                    results.count = users.size

                    return results
                    //공백제외 2글자 이인 경우 -> 이름으로만 검색
                } else if (filterString.trim { it <= ' ' }.length <= 2) {
                    for (user in users) {
                        if (user.name.contains(filterString)) {
                            filteredList.add(user)
                        }
                    }
                    //그 외의 경우(공백제외 2글자 초과) -> 이름/전화번호로 검색
                } else {
                    for (user in users) {
                        if (user.name.contains(filterString) || user.uid.contains(
                                filterString
                            )
                        ) {
                            filteredList.add(user)
                        }
                    }
                }
                results.values = filteredList
                results.count = filteredList.size

                return results
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                filteredUsers.clear()
                filteredUsers.addAll(filterResults.values as ArrayList<Friend>)
                notifyDataSetChanged()
            }
        }
    }

    fun setAdapter() {
        //리사이클러뷰에 리사이클러뷰 어댑터 부착
        println("setAdapter 실행 중")
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerViewAdapter = SearchRecyclerViewAdapter(users, this)
        recyclerView.adapter = searchRecyclerViewAdapter
    }

    fun tempUsers(): ArrayList<Friend> {
        println("tempUser 실행 중")
        val user = Firebase.auth.currentUser
        val db = Firebase.firestore
        var member = Friend("", "", "")
        val temp_user = ArrayList<Friend>()

        lateinit var friendlist: ArrayList<String>
        lateinit var friendrequestlist: ArrayList<String>
        lateinit var requestlist: ArrayList<String>

        db.collection("users").document(user!!.uid).get()
            .addOnSuccessListener {
                if (it.data != null) {
                    if (it.data!!.get("friends") != null)
                        friendlist = it.data!!.get("friends") as ArrayList<String>
                    else
                        friendlist = ArrayList<String>()
                }
                if (it.data != null) {
                    if (it.data!!.get("requestFriends") != null)
                        friendrequestlist = it.data!!.get("requestFriends") as ArrayList<String>
                    else
                        friendrequestlist = ArrayList<String>()
                }
//                friendlist = it.data.get("friends") as ArrayList<String>
//                friendrequestlist = it.data.get("requestFriends") as ArrayList<String>


                db.collection("users")
                    .whereEqualTo("type", "user")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (document.get("requestFriends") != null) {
                                requestlist = document.get("requestFriends") as ArrayList<String>
                                println("${document} 친구 요청 체크리스트")
                            } else
                                requestlist = ArrayList<String>()

                            member.name = document.data.get("name").toString()
                            member.uid = document.data.get("uid").toString()
                            if (requestlist.contains(user.uid.toString())) {
                                member.button = "친구 요청"
                                println("배열 비교중 4")
                            } else if (friendrequestlist.contains(member.uid)) {
                                member.button = "수락 대기"
                                println("배열 비교중 3")
                            } else if (friendlist.contains(member.uid)) {
                                member.button = "친구 삭제"
                                println("배열 비교중 2")
                            } else {
                                member.button = "친구 추가"
                                println("배열 비교중 1")
                            }
                            if (friendlist.contains(member.uid)) {
                                member.button = "친구 삭제"
                                println("배열 비교중 2")
                            }

                            println(member.button)
                            if (!member.uid.equals(user?.uid.toString())) {
                                temp_user.add(Friend(member.uid, member.name, member.button))
                            }
                        }
                    }
                    .addOnFailureListener { exception ->
                    }

            }
            .addOnFailureListener {

            }


        println("tempUser 종료")
        return temp_user
    }
}
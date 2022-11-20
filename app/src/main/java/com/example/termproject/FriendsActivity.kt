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

            init {
                user_img_list = itemView.findViewById(R.id.user_img_list)
                user_uid_list = itemView.findViewById(R.id.user_uid_list)
                user_name_list = itemView.findViewById(R.id.user_name_list)

                println("ViewHolder 내용 ${user_name_list}")
                itemView.findViewById<Button>(R.id.friend_add).setOnClickListener {
                    //친구 추가 메세지 보내기
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

            holder.user_uid_list.text = user.uid//uid로 변경 예정
            holder.user_name_list.text = user.name
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

    fun setAdapter(){
        //리사이클러뷰에 리사이클러뷰 어댑터 부착
        println("setAdapter 실행 중")
        recyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerViewAdapter = SearchRecyclerViewAdapter(users, this)
        recyclerView.adapter = searchRecyclerViewAdapter
    }

    fun tempUsers(): ArrayList<Friend> {
        println("tempUser 실행 중")
        val db = Firebase.firestore
        var member = Friend("", "")
        val temp_user = ArrayList<Friend>()

        db.collection("users")
            .whereEqualTo("type", "user")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    member.name = document.data.get("name").toString()
                    member.uid = document.data.get("uid").toString()
//                    temp_user.addAll(listOf(member))
                    temp_user.add(Friend(member.uid, member.name))
                }
            }
            .addOnFailureListener { exception ->
            }
        println("tempUser 종료")
        return temp_user
    }
}
package com.example.termproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns_project.PostInfo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MainAdapter(private val context : Context, private val items:ArrayList<PostInfo>) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false)
        return ItemViewHolder(view)
    }
    override fun getItemCount() : Int{
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], context)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var v : View= view
        private val username: TextView = itemView.findViewById(R.id.post_username)
        private val contents: TextView = itemView.findViewById(R.id.post_contents)
        private val createdAt: TextView = itemView.findViewById(R.id.post_createdAt)
        private val postimage: ImageView = itemView.findViewById(R.id.post_img)
        private val posttiltle: TextView = itemView.findViewById((R.id.post_title))

        fun bind(item:PostInfo, context: Context) {

            if(item.image != "") {
                println(item.image)

                        Glide.with(context.applicationContext)
                            .load(item.image)
                            .into(postimage)

            }
            else{
                val param = postimage.layoutParams
                param.height = 0
                postimage.setLayoutParams(param)
            }
            username.text = item.name
            contents.text = item.text
            createdAt.text = item.Date
            posttiltle.text = item.title

            v.setOnClickListener {
                Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
            }

        }


    }
}




package com.example.termproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sns_project.PostInfo

class MainAdapter(private val context : Context, private val items:ArrayList<PostInfo>) : RecyclerView.Adapter<MainAdapter.ItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post,parent,false)
        println("itmesize           " + items.size)
        return ItemViewHolder(view)
    }
    override fun getItemCount() : Int{
        println("                                  !!!!!!!!!!!!!!!!!!!")
        println(items.size)
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
        fun bind(item:PostInfo, context: Context) {
                username.text = item.name
                contents.text = item.text
                createdAt.text = item.text
                if(item.image != ""){

                }
                else{

                }

                v.setOnClickListener {
                    Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
                }

        }
    }
}


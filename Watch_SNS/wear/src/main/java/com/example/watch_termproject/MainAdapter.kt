package com.example.watch_termproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
            createdAt.text = item.Date.substring(0, item.Date.length - 7)
            posttiltle.text = item.title



        }



    }
}
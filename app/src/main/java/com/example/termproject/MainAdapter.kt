package com.example.termproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.sns_project.PostInfo

class MainAdapter(private val items:ArrayList<PostInfo>) : RecyclerView.Adapter<MainAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false)

        return MainAdapter.ViewHolder(view)
    }
    override fun getItemCount() : Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val listener = View.OnClickListener {
            Toast.makeText(it.context, "Clicked", Toast.LENGTH_SHORT).show()
        }

        holder.apply{
            bind(listener, item)
            itemView.tag = item
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        fun bind(listener: View.OnClickListener, item:PostInfo) {


        }
    }
}


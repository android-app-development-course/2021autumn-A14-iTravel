package com.example.xiaochengshu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class Fans(val imgId:Int,val user_name:String,val profile:String)

class FansAdapter(val fansList:ArrayList<Fans>) : RecyclerView.Adapter<FansAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val fansImage:ImageView=view.findViewById(R.id.fansImage)
        val fansName:TextView=view.findViewById(R.id.fansName)
        val profile:TextView=view.findViewById(R.id.profile)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fans_item,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val fans = fansList[position]
            Toast.makeText(parent.context, "you clicked view ${fans.user_name}",
                Toast.LENGTH_SHORT).show()
        }


        return viewHolder
    }
    override fun onBindViewHolder(holder: FansAdapter.ViewHolder, position: Int) {
        val fanser = fansList[position]
        holder.fansImage.setImageResource(fanser.imgId)
        holder.fansName.text = fanser.user_name
        holder.profile.text = fanser.profile
    }
    override fun getItemCount()=fansList.size

}

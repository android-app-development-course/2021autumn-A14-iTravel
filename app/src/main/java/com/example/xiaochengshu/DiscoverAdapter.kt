package com.example.xiaochengshu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.discover_item.view.*

class DiscoverAdapter(val titleList:List<String>) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val discoverTitle : TextView = view.discoverTitle
        val discoverImage : ImageView = view.discoverImage
        val discoverTouxiang : ImageView = view.discoverTouxiang
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.discover_item,parent,false)

        /*注册点击事件*/
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
            val title = titleList[position]
            val context: Context = view.context
            val intent = Intent(context,TripActivity::class.java)
            context.startActivity(intent)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: DiscoverAdapter.ViewHolder, position: Int) {
        val title = titleList[position]
        holder.discoverTitle.text = title
        holder.discoverImage.setImageResource(R.drawable.picture_sample2)
        holder.discoverTouxiang.setImageResource(R.drawable.touxiang)
    }

    override fun getItemCount() = titleList.size
}
package com.example.xiaochengshu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.picture_layout.view.*

class PictureAdapter(val picture_list:List<Int>) : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val container : ImageView = view.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_layout,parent,false))
    }

    override fun onBindViewHolder(holder: PictureAdapter.ViewHolder, position: Int) {
        holder.container.setImageResource(picture_list[position])
    }

    override fun getItemCount() = picture_list.size

}
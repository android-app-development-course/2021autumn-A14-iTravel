package com.example.xiaochengshu

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import kotlinx.android.synthetic.main.picture_layout.view.*

class PictureAdapter(val picture_list:List<String>,val activity:Activity) : RecyclerView.Adapter<PictureAdapter.ViewHolder>() {

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val container : ImageView = view.imageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_layout,parent,false))
    }

    override fun onBindViewHolder(holder: PictureAdapter.ViewHolder, position: Int) {
        Glide.with(activity).load(picture_list[position]).override(holder.container.width,SIZE_ORIGINAL).fitCenter().into(holder.container)
    }

    override fun getItemCount() = picture_list.size

}
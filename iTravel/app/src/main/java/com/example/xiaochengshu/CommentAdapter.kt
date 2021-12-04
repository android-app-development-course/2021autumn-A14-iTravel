package com.example.xiaochengshu

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_trip.view.*
import kotlinx.android.synthetic.main.discover_item.view.*
import java.lang.IllegalArgumentException


class CommentAdapter(val commentsList:List<Comments>):RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val commentUserImg:ImageView=view.findViewById(R.id.user_id)
        val commentUserName:TextView=view.findViewById(R.id.user_name)
        val commentGrass:CheckBox=view.findViewById(R.id.grass_id)
        val commentRating:RatingBar=view.findViewById(R.id.rating)
        val comment:TextView=view.findViewById(R.id.comment_result)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments_item,parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: CommentAdapter.ViewHolder, position: Int) {
        val comment = commentsList[position]
        holder.commentUserImg.setImageResource(comment.imgId)
        holder.commentUserName.text = comment.user_name
        holder.commentGrass.isChecked = comment.grass
        holder.commentRating.numStars = comment.rating
        holder.comment.text = comment.comment
    }

    override fun getItemCount(): Int {
        return commentsList.size
    }
}
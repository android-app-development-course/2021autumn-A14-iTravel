package com.example.xiaochengshu

import android.app.Activity
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView


class MessageAdapter(val messageList:ArrayList<Messages>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val senderImage:ImageView=view.findViewById(R.id.senderImage)
        val senderName:TextView=view.findViewById(R.id.senderName)
        val message:TextView=view.findViewById(R.id.message)
        val time:TextView=view.findViewById(R.id.sendTime)//
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val message = messageList[position]

            Toast.makeText(parent.context, "you clicked view ${message.user_name}", Toast.LENGTH_SHORT).show()
        }

        return viewHolder
    }
    override fun onBindViewHolder(holder: MessageAdapter.ViewHolder, position: Int) {
        val message = messageList[position]
        holder.senderImage.setImageResource(message.imgId)
        holder.senderName.text = message.user_name
        holder.message.text = message.message
        holder.time.text=message.time
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}

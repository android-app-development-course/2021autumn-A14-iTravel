package com.example.xiaochengshu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
class Focus(val imgId:Int,val user_name:String,val profile:String)

class FocusAdapter(val focusList:ArrayList<Focus>) : RecyclerView.Adapter<FocusAdapter.ViewHolder>() {

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val focusImage:ImageView=view.findViewById(R.id.focusImage)
        val focusName:TextView=view.findViewById(R.id.focusName)
        val profile:TextView=view.findViewById(R.id.profile)
        val button:Button=view.findViewById(R.id.f_id)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.focus_item,parent,false)
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val focus = focusList[position]
            val context: Context = view.context
            val intent = Intent(context,MainActivity::class.java)
            intent.putExtra("user_name",focusList[position].user_name)
            context.startActivity(intent)
            Toast.makeText(parent.context, "you clicked view ${focus.user_name}",
                Toast.LENGTH_SHORT).show()
        }
        //通过从数据库获取标记
        var focus = "已关注"
        viewHolder.button.setText(focus)
        viewHolder.button.setOnClickListener {
            if( focus=="关注"){
                focus="已关注"
                viewHolder.button.setText(focus)
            }
            else{
                focus="关注"
                viewHolder.button.setText(focus)
            }
        }
        return viewHolder
    }
    override fun onBindViewHolder(holder: FocusAdapter.ViewHolder, position: Int) {
        val focuser = focusList[position]
        holder.focusImage.setImageResource(focuser.imgId)
        holder.focusName.text = focuser.user_name
        holder.profile.text = focuser.profile
    }
    override fun getItemCount()=focusList.size

}

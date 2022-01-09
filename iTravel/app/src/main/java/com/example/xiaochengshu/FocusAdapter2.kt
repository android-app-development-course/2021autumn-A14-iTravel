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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import kotlinx.android.synthetic.main.discover_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class FocusAdapter2(val fragment: Fragment?, val activity: Activity?) : RecyclerView.Adapter<FocusAdapter2.ViewHolder>() {

    constructor(_fragment: Fragment) : this(fragment = _fragment, activity = null) {}
    constructor(_activity: Activity) : this(fragment = null, activity = _activity) {}

    var tripList:ArrayList<JSONObject> = arrayListOf()

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val discoverTitle : TextView = view.discoverTitle
        val authorName : TextView = view.authorName
        val discoverImage : ImageView = view.discoverImage
        val discoverTouxiang : ImageView = view.discoverTouxiang
        val collect_button:Button=view.collect_button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FocusAdapter2.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.discover_item,parent,false)

        /*注册点击事件*/
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
            val context: Context = view.context
            val intent = Intent(context,TripActivity::class.java)
            intent.putExtra("ID",tripList[position].getInt("id"))
            context.startActivity(intent)
        }

        var flag=false
        viewHolder.collect_button.setOnClickListener{
            if(flag){flag=false}
            else flag = true
            if(flag){
                viewHolder.collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collected, 0, 0, 0)
                //通过 id 记录该数据到收藏数据表
            }
            else{
                viewHolder.collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collect, 0, 0, 0)
                //通过 id 删除收藏数据表的该记录
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: FocusAdapter2.ViewHolder, position: Int) {
        holder.discoverTitle.text = tripList[position].getString("title")
        holder.authorName.text = tripList[position].getString("author")
        if (activity != null) {
            Glide.with(activity).load(tripList[position].getString("cover_img")).override(500,700).fitCenter().into(holder.discoverImage)
        }
        else if (fragment != null) {
            Glide.with(fragment).load(tripList[position].getString("cover_img")).override(500,700).fitCenter().into(holder.discoverImage)
        }
        if (activity != null) {
            Glide.with(activity).load(tripList[position].getString("head_img")).into(holder.discoverTouxiang)
        }
        else if (fragment != null) {
            Glide.with(fragment).load(tripList[position].getString("head_img")).into(holder.discoverTouxiang)
        }
    }

    override fun getItemCount() = tripList.size

    fun addData(dataJsonList : JSONArray)
    {
        for (i in 0 until dataJsonList.length()){
            val itemData = dataJsonList.getJSONObject(i)
            tripList.add(dataJsonList.getJSONObject(i))

            notifyItemInserted(tripList.size)
        }
    }

    fun initData(dataJsonList : JSONArray){
        tripList.clear()

        for (i in 0 until dataJsonList.length()){
            val itemData = dataJsonList.getJSONObject(i)

            tripList.add(dataJsonList.getJSONObject(i))

            notifyItemInserted(tripList.size)
        }
    }

    fun updateData(dataJsonList : JSONArray){
        while (!tripList.isEmpty()){

            tripList.removeAt(0)

            notifyItemRemoved(0)
            notifyItemRangeChanged(0, tripList.size)
        }
        for (i in 0 until dataJsonList.length()){
            val itemData = dataJsonList.getJSONObject(i)

            tripList.add(dataJsonList.getJSONObject(i))

            notifyItemInserted(tripList.size)
        }
    }
}
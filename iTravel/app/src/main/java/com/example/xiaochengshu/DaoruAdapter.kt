package com.example.xiaochengshu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.daoru_item.view.*
import kotlinx.android.synthetic.main.discover_item.view.*
import kotlinx.android.synthetic.main.discover_item.view.authorName
import kotlinx.android.synthetic.main.discover_item.view.discoverImage
import kotlinx.android.synthetic.main.discover_item.view.discoverTitle
import kotlinx.android.synthetic.main.discover_item.view.discoverTouxiang
import org.json.JSONArray
import org.json.JSONObject

class DaoruAdapter(val fragment: Fragment?, val activity: Activity?) :
    RecyclerView.Adapter<DaoruAdapter.ViewHolder>() {

    var xuan:Int=0
    constructor(_fragment: Fragment) : this(fragment = _fragment, activity = null) {}
    constructor(_activity: Activity) : this(fragment = null, activity = _activity) {}

    var tripList: ArrayList<JSONObject> = arrayListOf()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val discoverTitle: TextView = view.discoverTitle
        val authorName: TextView = view.authorName
        val discoverImage: ImageView = view.discoverImage
        val discoverTouxiang: ImageView = view.discoverTouxiang
        var Bxuanzhong: Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaoruAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.daoru_item, parent, false)

        /*注册点击事件*/
        val viewHolder = ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val context: Context = view.context
            val intent = Intent(context, TripActivity::class.java)
            intent.putExtra("ID", tripList[position].getInt("id"))
            context.startActivity(intent)
        }
        viewHolder.itemView.xuanzhong.setOnClickListener {
            if (!viewHolder.Bxuanzhong) {
                viewHolder.itemView.zhezhao2.visibility = View.VISIBLE
                viewHolder.itemView.xuanzhong.setImageResource(R.drawable.round_check_orange)
                viewHolder.Bxuanzhong=true
                xuan= viewHolder.absoluteAdapterPosition

            }
            else{
                viewHolder.itemView.zhezhao2.visibility = View.INVISIBLE
                viewHolder.itemView.xuanzhong.setImageResource(R.drawable.round_check)
                viewHolder.Bxuanzhong=false
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: DaoruAdapter.ViewHolder, position: Int) {
        holder.discoverTitle.text = tripList[position].getString("title")
        holder.authorName.text = tripList[position].getString("author")
        if (activity != null) {
            Glide.with(activity).load(tripList[position].getString("cover_img"))
                .into(holder.discoverImage)
        } else if (fragment != null) {
            Glide.with(fragment).load(tripList[position].getString("cover_img"))
                .into(holder.discoverImage)
        }
        if (activity != null) {
            Glide.with(activity).load(tripList[position].getString("head_img"))
                .into(holder.discoverTouxiang)
        } else if (fragment != null) {
            Glide.with(fragment).load(tripList[position].getString("head_img"))
                .into(holder.discoverTouxiang)
        }
    }

    override fun getItemCount() = tripList.size

    fun addData(dataJsonList: JSONArray) {
        for (i in 0 until dataJsonList.length()) {
            val itemData = dataJsonList.getJSONObject(i)
            tripList.add(dataJsonList.getJSONObject(i))

            notifyItemInserted(tripList.size)
        }
    }

    fun initData(dataJsonList: JSONArray) {
        tripList.clear()

        for (i in 0 until dataJsonList.length()) {
            val itemData = dataJsonList.getJSONObject(i)

            tripList.add(dataJsonList.getJSONObject(i))

            notifyItemInserted(tripList.size)
        }
    }

    fun updateData(dataJsonList: JSONArray) {
        while (!tripList.isEmpty()) {

            tripList.removeAt(0)

            notifyItemRemoved(0)
            notifyItemRangeChanged(0, tripList.size)
        }
        for (i in 0 until dataJsonList.length()) {
            val itemData = dataJsonList.getJSONObject(i)

            tripList.add(dataJsonList.getJSONObject(i))

            notifyItemInserted(tripList.size)
        }
    }
}
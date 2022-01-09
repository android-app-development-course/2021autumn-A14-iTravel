package com.example.xiaochengshu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.discover_item.view.*
import org.json.JSONArray
import org.json.JSONObject

class DiscoverAdapter(val fragment: Fragment?, val activity: Activity?) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    constructor(_fragment: Fragment) : this(fragment = _fragment, activity = null) {}
    constructor(_activity: Activity) : this(fragment = null, activity = _activity) {}

    var tripList:ArrayList<JSONObject> = arrayListOf()

    var id_array= arrayListOf<String>()//存放收藏id

    inner class ViewHolder(view:View) : RecyclerView.ViewHolder(view){
        val discoverTitle : TextView = view.discoverTitle
        val authorName : TextView = view.authorName
        val discoverImage : ImageView = view.discoverImage
        val discoverTouxiang : ImageView = view.discoverTouxiang
        val collect_button: Button =view.collect_button
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverAdapter.ViewHolder {
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

        var flag=false//是不是已经被种草

        /*是否种草显示，有bug*/
//        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItCollect/GetCollectList&user_id=${BitmapApplication.getMyUserId()}",object: HttpCallbackListener{
//            override fun onFinish(response: String) {
//                try {
//                    val jsonObject = JSONObject(response)
//                    val dataJsonList = jsonObject.getJSONArray("data")
//
//                    for (index in 0 until dataJsonList.length()){
//                        val id= dataJsonList.getJSONObject(index).getString("id")
//                        id_array.add(id)
//                    }
//                    for (i in 0 until  id_array.size){
//                        if (tripList[viewHolder.adapterPosition].getInt("id").toString() == id_array[i]){
//                            flag=true
//                            if(flag) {
//                                if (activity != null) {
//                                    activity.runOnUiThread ( object : Runnable{
//                                        override fun run() {
//                                            viewHolder.collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collected, 0, 0, 0)
//                                        }
//
//                                    } )
//                                }
//                                else if (fragment != null) {
//                                    fragment.activity?.runOnUiThread ( object : Runnable{
//                                        override fun run() {
//                                            viewHolder.collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collected, 0, 0, 0)
//                                        }
//
//                                    } )
//                                }
//                            }
//                        }
//                    }
//                } catch (e: java.lang.Exception) {
//                    e.printStackTrace()
//                }
//            }
//            override fun onError(e: Exception) {
//
//            }
//
//        })

        viewHolder.collect_button.setOnClickListener{
            if(!flag){
                viewHolder.collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collected, 0, 0, 0)
                //通过 id 记录该数据到收藏数据表
                NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItCollect/CreateOne&ItCollect[guide_id]=${tripList[viewHolder.adapterPosition].getInt("id")}&ItCollect[user_id]=${BitmapApplication.getMyUserId()}",
                    object : HttpCallbackListener {
                        override fun onFinish(response: String) {
                            try {
                                val jsonObject = JSONObject(response)
                                val dataJsonList = jsonObject.getJSONObject("data")
                                Log.d("hhhhhhhhh",dataJsonList.toString())
                                //获取后台返回的数据是成功还是失败
                                if (activity != null) {
                                    activity.runOnUiThread(object : Runnable {
                                        override fun run() {
                                            Toast.makeText(activity,"种草成功", Toast.LENGTH_SHORT).show()
                                            flag=true
                                        }
                                    })
                                }
                                else if (fragment != null){
                                    fragment.activity?.runOnUiThread(object : Runnable {
                                        override fun run() {
                                            Toast.makeText(fragment.activity,"种草成功", Toast.LENGTH_SHORT).show()
                                            flag=true
                                        }
                                    })
                                }

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                        override fun onError(e: Exception) {

                        }
                    })
            }
            else{
                viewHolder.collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collect, 0, 0, 0)
                //通过 id 删除收藏数据表的该记录
                NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItCollect/CancelCollect&guide_id=${tripList[viewHolder.adapterPosition].getInt("id")}&user_id=${BitmapApplication.getMyUserId()}",
                    object : HttpCallbackListener {
                        override fun onFinish(response: String) {
                            try {
                                val jsonObject = JSONObject(response)
                                val dataJsonList = jsonObject.getJSONObject("data")
                                Log.d("取消收藏",dataJsonList.toString())
                                //获取后台返回的数据是成功还是失败
                                if (activity != null) {
                                    activity.runOnUiThread(object : Runnable {
                                        override fun run() {
                                            Toast.makeText(activity,"取消种草", Toast.LENGTH_SHORT).show()
                                            flag=false
                                        }
                                    })
                                }
                                else if (fragment != null){
                                    fragment.activity?.runOnUiThread(object : Runnable {
                                        override fun run() {
                                            Toast.makeText(fragment.activity,"取消种草", Toast.LENGTH_SHORT).show()
                                            flag=false
                                        }
                                    })
                                }

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }
                        override fun onError(e: Exception) {

                        }
                    })
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: DiscoverAdapter.ViewHolder, position: Int) {
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
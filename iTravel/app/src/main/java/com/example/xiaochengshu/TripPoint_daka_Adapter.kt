package com.example.xiaochengshu

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.tencent.tencentmap.mapsdk.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_trip.*
import org.json.JSONObject


class TripPoint_daka_Adapter(val TripList: List<TripPoint>, val activity: Activity) :
    RecyclerView.Adapter<TripPoint_daka_Adapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TripPointImage:ImageView=view.findViewById(R.id.TripPointImage)
        val content:TextView=view.findViewById(R.id.content)
        val TripPointName:TextView=view.findViewById(R.id.TripPointName)
        val daka:Button=view.findViewById(R.id.daka)
        val zhezhao:ImageView=view.findViewById(R.id.zhezhao)
        val num:TextView=view.findViewById(R.id.num)
    }

    //首先加载一个布局，然后在ViewHolder(view)里获取布局上所有控件的实例（见上面一段代码）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.trippointdaka_item,parent,false)
        val viewHolder= ViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            val TripPoint = TripList[position]
            Toast.makeText(parent.context, "you clicked view ${TripPoint.name}",
                Toast.LENGTH_SHORT).show()
        }
        viewHolder.TripPointImage.setOnClickListener {
            val position = viewHolder.adapterPosition
            val TripPoint = TripList[position]
            Toast.makeText(parent.context, "you clicked image ${TripPoint.name}",
                Toast.LENGTH_SHORT).show()
        }

        viewHolder.daka.setOnClickListener {
            val position = viewHolder.adapterPosition
            val TripPoint = TripList[position]
            NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItPlace/Mark&user_id=1&place_id=${TripPoint.id}",object: HttpCallbackListener{
                override fun onFinish(response: String) {
                    activity.runOnUiThread(object :Runnable{
                        override fun run() {
                            viewHolder.daka.text="已打卡"
                            viewHolder.zhezhao.visibility=View.VISIBLE
                        }

                    })
                }
                override fun onError(e: Exception) {

                }
            })
        }
        return viewHolder
    }

    // onBindViewHolder()方法用于对RecyclerView子项布局的控件实例的数据进行赋值，
    // 会在每个子项被滚动到屏幕内的时候执行
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val TripPoint=TripList[position]
        Glide.with(activity).load(TripList[position].imageurl).override(holder.TripPointImage.width,
            Target.SIZE_ORIGINAL
        ).fitCenter().into(holder.TripPointImage)
        holder.content.text=TripPoint.content
        holder.TripPointName.text=TripPoint.name
        holder.num.text=(position+1).toString()+'.'
        if (TripList[position].state_name == "已打卡"){
            holder.daka.text="已打卡"
            holder.zhezhao.visibility=View.VISIBLE
        }
    }

    override fun getItemCount()=TripList.size

}


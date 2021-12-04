package com.example.xiaochengshu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TripPoint(val name: String, val content:String,val imageId: Int) {
}
class TripPointAdapter(val TripList: List<TripPoint>) :
    RecyclerView.Adapter<TripPointAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TripPointImage:ImageView=view.findViewById(R.id.TripPointImage)
        val content:TextView=view.findViewById(R.id.content)
        val TripPointName:TextView=view.findViewById(R.id.TripPointName)
    }

    //首先加载一个布局，然后在ViewHolder(view)里获取布局上所有控件的实例（见上面一段代码）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.trippoint_item,parent,false)
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
        return viewHolder
    }

    // onBindViewHolder()方法用于对RecyclerView子项布局的控件实例的数据进行赋值，
    // 会在每个子项被滚动到屏幕内的时候执行
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val TripPoint=TripList[position]
        holder.TripPointImage.setImageResource(TripPoint.imageId)
        holder.content.text=TripPoint.content
        holder.TripPointName.text=TripPoint.name
    }

    override fun getItemCount()=TripList.size
}
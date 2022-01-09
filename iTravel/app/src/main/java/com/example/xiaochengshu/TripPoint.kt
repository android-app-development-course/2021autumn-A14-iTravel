package com.example.xiaochengshu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target

class TripPoint(val name: String, val content:String,val imageurl: String,val id : Int, val state_name: String) {
}
class TripPointAdapter(val TripList: List<TripPoint>, val activity: Activity) :
    RecyclerView.Adapter<TripPointAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TripPointImage:ImageView=view.findViewById(R.id.TripPointImage)
        val content:TextView=view.findViewById(R.id.content)
        val TripPointName:TextView=view.findViewById(R.id.TripPointName)
        val num:TextView=view.findViewById(R.id.num)
        val tripPointItem:LinearLayout = view.findViewById(R.id.tripPointItem)
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
        viewHolder.tripPointItem.setOnClickListener {
            val position = viewHolder.adapterPosition
            val TripPoint = TripList[position]
            val context: Context = view.context
            Toast.makeText(parent.context, "you clicked image ${TripPoint.name}",
                Toast.LENGTH_SHORT).show()
            val intent = Intent(context,LocationActivity::class.java)
            intent.putExtra("ID",TripPoint.id)
            context.startActivity(intent)
        }
        return viewHolder
    }

    // onBindViewHolder()方法用于对RecyclerView子项布局的控件实例的数据进行赋值，
    // 会在每个子项被滚动到屏幕内的时候执行
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val TripPoint=TripList[position]
//        holder.TripPointImage.setImageResource(TripPoint.imageId)
        Glide.with(activity).load(TripList[position].imageurl).override(holder.TripPointImage.width,
            Target.SIZE_ORIGINAL
        ).fitCenter().into(holder.TripPointImage)
        holder.content.text=TripPoint.content
        holder.TripPointName.text=TripPoint.name
        holder.num.text=(position+1).toString()+'.'
    }

    override fun getItemCount()=TripList.size
}
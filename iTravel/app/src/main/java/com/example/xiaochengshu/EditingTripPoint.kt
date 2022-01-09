package com.example.xiaochengshu

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.tencent.tencentmap.mapsdk.maps.model.LatLng

class EditingTripPoint(val name: String, val content:String,val bitmap: Bitmap,val latLng: LatLng) {
}
class EditingTripPointAdapter(var EditingTripPointList: ArrayList<EditingTripPoint>, val activity: Activity) :
    RecyclerView.Adapter<EditingTripPointAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val TripPointImage:ImageView=view.findViewById(R.id.TripPointImage)
        val content:TextView=view.findViewById(R.id.content)
        val TripPointName:TextView=view.findViewById(R.id.TripPointName)
        val num:TextView=view.findViewById(R.id.num)
        val delete: Button = view.findViewById(R.id.delete)
    }

    //首先加载一个布局，然后在ViewHolder(view)里获取布局上所有控件的实例（见上面一段代码）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.trippointedit_item,parent,false)
        val viewHolder= ViewHolder(view)
        //删除按钮的点击事件
        viewHolder.delete.setOnClickListener {
            //弹出警示对话框
            AlertDialog.Builder(parent.context).apply {
                setTitle("提示")
                setMessage("\n确定要删除该行程点吗？")
                setCancelable(false)
                setPositiveButton("确定") { dialog, which ->
                    //若点击确认，则删除
                    val i: Int = viewHolder.absoluteAdapterPosition
                    EditingTripPointList.remove(EditingTripPointList[i])
                    notifyItemRemoved(i)
                    notifyItemRangeRemoved(i, EditingTripPointList.size)
                }
                setNegativeButton("取消") { dialog, which ->
                }
                show()
            }
        }
        return viewHolder
    }

    // onBindViewHolder()方法用于对RecyclerView子项布局的控件实例的数据进行赋值，
    // 会在每个子项被滚动到屏幕内的时候执行
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val TripPoint=EditingTripPointList[position]
//        holder.TripPointImage.setImageResource(TripPoint.imageId)
//        Glide.with(activity).load(EditingTripPointList[position].imageurl).override(holder.TripPointImage.width,
//            Target.SIZE_ORIGINAL
//        ).fitCenter().into(holder.TripPointImage)
        holder.content.text=TripPoint.content
        holder.TripPointName.text=TripPoint.name
        holder.num.text=(position+1).toString()+'.'
        holder.TripPointImage.setImageBitmap(TripPoint.bitmap)
    }

    override fun getItemCount()=EditingTripPointList.size

    fun update(newEditingTripPointList: ArrayList<EditingTripPoint>){
        EditingTripPointList = newEditingTripPointList
        notifyItemInserted(EditingTripPointList.size)
    }
}
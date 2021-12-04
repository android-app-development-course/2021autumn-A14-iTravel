package com.example.xiaochengshu

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.picture_layout.*
import com.tencent.tencentmap.mapsdk.maps.MapView
import com.tencent.tencentmap.mapsdk.maps.TencentMap


class TripActivity : AppCompatActivity() {
    lateinit var mapView : MapView
    private val TripPointList=ArrayList<TripPoint>()
    private val commentsList=ArrayList<Comments>()
    private var comment_adapter:CommentAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        initTripPoints()
        //LayoutManager用于指定RecyclerView的布局方式
        // 这里使用的LinearLayoutManager是线性布局的意思，可以实现和ListView类似的效果
        val layoutManager1= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager1
        recyclerView.adapter=TripPointAdapter(TripPointList)


        val picture_list = listOf<Int>(R.drawable.picture_sample1,R.drawable.picture_sample2,R.drawable.picture_sample3)//图片轮播

        val adapter = PictureAdapter(picture_list)//数据传入适配器
        picture_view_pager.adapter = adapter

        /*地图*/
        val maplayout = MapLayout(this)//创建一个MapLayout，继承自LinearLayout
        maplayout.setScrollView(scrollview)//给这个MapLayout传入会与地图冲突的scrollview

        mapView = MapView(this)//创建一个地图
        maplayout.addView(mapView)//在Maplayout中加入这个地图

        map_with_button.addView(maplayout)//在页面中加入刚才创建的Maplayout

        val mTencentMap = mapView.map
        mTencentMap.addOnMapLoadedCallback(TencentMap.OnMapLoadedCallback {
            fun onMapLoaded(){}
        })
        /*地图结束*/

        back_button.setOnClickListener {
            this.finish()
        }


        start.setOnClickListener {
            val intent = Intent(this,zhengzaidaka::class.java)
            this.startActivity(intent)
        }
//        评论区
        initCommentsList()
        val layoutManager= LinearLayoutManager(this)
        recyclerView2.layoutManager=layoutManager
        comment_adapter = CommentAdapter(commentsList)
        recyclerView2.adapter=comment_adapter
        comment_send_id.setOnClickListener {
            val content = comment_input.text.toString()
            if (content.isNotEmpty()) {
                val user_id = R.drawable.touxiang
                val user_name = "Jack"
                val comments = Comments(user_id, user_name, true, 5, content)
                commentsList.add(comments)
//                    comment_adapter?.notifyItemInserted(comment_adapter.size-1)
//                    recyclerView2.scrollToPosition(comment_adapter.size-1)
                comment_input.setText("")//设置为空
                println("输出评论")
            }
        }

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    private fun initTripPoints() {
        repeat(1) {
            TripPointList.add(TripPoint("碧石岛","非常适合看日出的地方", R.drawable.xingcheng1))
            TripPointList.add(TripPoint("黄金海岸","中午在沙滩上日光浴" ,R.drawable.xingcheng2))
            TripPointList.add(TripPoint("黄金餐厅","附近的黄金餐厅平价又好吃！墙裂推荐！", R.drawable.xingcheng3))
        }
    }


    private fun initCommentsList(){
        repeat(1){
            commentsList.add(Comments(R.drawable.touxiang,"张三",true,5,"已打卡"))
            commentsList.add(Comments(R.drawable.touxiang,"张三",false,5,"已打卡"))
            commentsList.add(Comments(R.drawable.touxiang,"张三",false,5,"已打卡"))
            commentsList.add(Comments(R.drawable.touxiang,"张三",true,5,"已打卡"))
        }
    }
}
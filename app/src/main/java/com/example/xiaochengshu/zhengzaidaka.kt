package com.example.xiaochengshu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.tencentmap.mapsdk.maps.MapView
import com.tencent.tencentmap.mapsdk.maps.TencentMap
import kotlinx.android.synthetic.main.activity_trip.*

class zhengzaidaka : AppCompatActivity() {
    lateinit var mapView : MapView
    private val TripPointList=ArrayList<TripPoint>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zhengzaidaka_layout)

        initTripPoints()
        //LayoutManager用于指定RecyclerView的布局方式
        // 这里使用的LinearLayoutManager是线性布局的意思，可以实现和ListView类似的效果

        val layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=TripPointAdapter(TripPointList)

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
}
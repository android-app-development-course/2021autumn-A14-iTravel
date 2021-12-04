package com.example.xiaochengshu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tencent.tencentmap.mapsdk.maps.MapView
import kotlinx.android.synthetic.main.activity_create_trip.*

class CreateTripActivity : AppCompatActivity() {
    lateinit var mapView : MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        /*地图*/
        val maplayout = MapLayout(this)//创建一个MapLayout，继承自LinearLayout
        maplayout.setScrollView(scrollview)//给这个MapLayout传入会与地图冲突的scrollview

        mapView = MapView(this)//创建一个地图
        maplayout.addView(mapView)//在Maplayout中加入这个地图

        map.addView(maplayout)//在页面中加入刚才创建的Maplayout
        /*地图结束*/

        save.setOnClickListener {
            finish()
        }

        back_button.setOnClickListener {
            finish()
        }
    }

    /*绑定生命周期*/
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
}
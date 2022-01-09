package com.example.xiaochengshu

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.tencentmap.mapsdk.maps.MapView
import com.tencent.tencentmap.mapsdk.maps.TencentMap
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.zhengzaidaka_layout.*
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.maps.model.*
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.activity_trip.back_button
import kotlinx.android.synthetic.main.activity_trip.map_with_button
import kotlinx.android.synthetic.main.activity_trip.recyclerView
import kotlinx.android.synthetic.main.activity_trip.scrollview
import org.json.JSONObject


class zhengzaidaka : AppCompatActivity() {
    lateinit var mapView: MapView
    private val TripPointList = ArrayList<TripPoint>()
    var id = 0
    /*Infowindow数据*/
    val latLngArrayList = arrayListOf<LatLng>()
    val titles = arrayListOf<String>()
    val snippets = arrayListOf<String>()

    private val markerIdList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.zhengzaidaka_layout)

        id = intent.getIntExtra("ID",0)
        authorName.text = intent.getStringExtra("title")

        BitmapApplication.setZhengzaidakaId(id)

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
        /*地图初始化结束*/

        //LayoutManager用于指定RecyclerView的布局方式
        // 这里使用的LinearLayoutManager是线性布局的意思，可以实现和ListView类似的效果
        val layoutManager1= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager1

        initTripPoints()

        //获取全屏的宽度
        val resources: Resources = this.resources
        val dm: DisplayMetrics = resources.getDisplayMetrics()
        val width = dm.widthPixels

//        //动态设置列表高度，解决scrollview嵌套recyclerview的问题
//        val height=2*260
//        val params = LinearLayout.LayoutParams(width, height)
//        recyclinear.layoutParams = params

        back_button.setOnClickListener {
            this.finish()
        }
        // 点击《丽江一日游平价攻略》跳转到原攻略
        authorName.setOnClickListener {
            val intent = Intent(this,TripActivity::class.java)
            intent.putExtra("ID",id)
            this.startActivity(intent)
        }

        edit.setOnClickListener {
            val intent = Intent(this,edit_trip::class.java)
            intent.putExtra("ID",id)
            this.startActivity(intent)
        }

        add.setOnClickListener {
            val intent = Intent(this,CreateIntroductionActivity::class.java)
            this.startActivity(intent)
        }

        end_trip.setOnClickListener {
            BitmapApplication.setZhengzaidakaId(0)
            finish()
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
        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItPlace/GetListByGid&guide_id=$id",object: HttpCallbackListener{
            override fun onFinish(response: String) {
                try {
                    Log.d("cccccc",response)
                    val jsonObject = JSONObject(response)
                    val dataJsonList = jsonObject.getJSONArray("data")
                    for (i in 0 until dataJsonList.length()){
                        TripPointList.add(TripPoint(dataJsonList.getJSONObject(i).getString("place_name"),dataJsonList.getJSONObject(i).getString("place_intro"),dataJsonList.getJSONObject(i).getString("cover_img"),dataJsonList.getJSONObject(i).getInt("id"),dataJsonList.getJSONObject(i).getString("state_name")))
                        latLngArrayList.add(LatLng(dataJsonList.getJSONObject(i).getDouble("latitude"),dataJsonList.getJSONObject(i).getDouble("longitude")))
                        titles.add(dataJsonList.getJSONObject(i).getString("place_name"))
                        snippets.add(dataJsonList.getJSONObject(i).getString("place_intro"))
                    }
                    runOnUiThread(object : Runnable{
                        override fun run() {
                            recyclerView.adapter=TripPoint_daka_Adapter(TripPointList,this@zhengzaidaka)
                            map()
                        }
                    })
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Exception) {

            }

        })
    }

    fun map(){
        if (latLngArrayList.size == 0)
            return
        /*地图打卡点信息显示功能（覆盖物）*/
        val tencentMap = mapView.map
        //开启多窗口模式
        tencentMap.enableMultipleInfowindow(true)
        //传入数据
        for (i in 0 until latLngArrayList.size) {
            val marker: Marker = tencentMap.addMarker(
                MarkerOptions()
                    .position(latLngArrayList.get(i)).icon(
                        BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                    ).title(titles.get(i)).snippet(snippets.get(i))
            )
            markerIdList.add(marker.id.toInt())
            marker.showInfoWindow()
            Log.d("id",marker.id.toString())
        }
        //设置信息窗口点击事件
        tencentMap.setOnInfoWindowClickListener(object : TencentMap.OnInfoWindowClickListener {
            override fun onInfoWindowClick(p0: Marker?) {
                if (p0 != null) {
                    Toast.makeText(this@zhengzaidaka, p0.title + "被点击了！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@zhengzaidaka,LocationActivity::class.java)
                    val id = markerIdList.indexOf(p0.id.toInt())
                    intent.putExtra("ID",TripPointList[id].id)
                    Toast.makeText(this@zhengzaidaka, TripPointList[id].id.toString(), Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }

            override fun onInfoWindowClickLocation(p0: Int, p1: Int, p2: Int, p3: Int) {

            }

        })
        val center = latLngArrayList[0]//设置中心点为第一个位置
        //调整视野范围，包含所有点
        val latLngBounds: LatLngBounds
        if (latLngArrayList.size == 1){
            latLngBounds = LatLngBounds.Builder().include(arrayListOf(latLngArrayList[0],latLngArrayList[0])).build()
        }
        else
            latLngBounds = LatLngBounds.Builder().include(latLngArrayList).build()
        tencentMap.animateCamera(
            CameraUpdateFactory.newLatLngBoundsWithMapCenter(
                latLngBounds,
                center,
                100
            )
        )
        /*地图结束*/
    }
}
package com.example.xiaochengshu

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.maps.MapView
import com.tencent.tencentmap.mapsdk.maps.TencentMap
import com.tencent.tencentmap.mapsdk.maps.model.*
import kotlinx.android.synthetic.main.activity_location.*
import kotlinx.android.synthetic.main.activity_trip.*
import org.json.JSONObject
import java.net.URISyntaxException

class LocationActivity : AppCompatActivity() {
    var locationId = 0
    lateinit var mapView : MapView
    var latitude = 0.0
    var longitude = 0.0
    lateinit var title :String
    lateinit var place_intro:String
    lateinit var cover_img:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        locationId = intent.getIntExtra("ID",0)

        navigationButton.setOnClickListener {
            val packageNames = getPackageNames(this)
//            if (isAvilible(packageNames,"com.autonavi.minimap")){
//                try {
//                    intent = Intent.getIntent("androidamap://route/plan?sourceApplication=" + R.string.app_name+ "&dName="+ p0.title + "&dlat="+p0.position.latitude+"&dlon="+p0.position.longitude+"&dev=0" + "&t="+ 2)
//                    startActivity(intent)
//                } catch (e: URISyntaxException){
//                    e.printStackTrace()
//                }
//            }
//            else if (isAvilible(packageNames,"com.baidu.BaiduMap")){
//                try {
//                    intent = Intent.getIntent("baidumap://map/direction?destination=name:" + p0.title + "|latlng:" + p0.position)
//                    startActivity(intent)
//                } catch (e: URISyntaxException){
//                    e.printStackTrace()
//                }
//            }
            if (isAvilible(packageNames,"com.tencent.map")){
                try {
                    intent = Intent.getIntent("qqmap://map/routeplan?type=walk&to=" + title + "&tocoord=" + latitude + "," + longitude)
                    startActivity(intent)
                } catch (e: URISyntaxException){
                    e.printStackTrace()
                }
            }
            else{
                val data = "geo:" + latitude + "," + longitude + "?q=" + title
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(data))
                startActivity(intent)
            }
        }

        /*地图*/
        val maplayout = MapLayout(this)//创建一个MapLayout，继承自LinearLayout
        maplayout.setScrollView(locationSrollView)//给这个MapLayout传入会与地图冲突的scrollview

        mapView = MapView(this)//创建一个地图
        maplayout.addView(mapView)//在Maplayout中加入这个地图

        map.addView(maplayout)//在页面中加入刚才创建的Maplayout

        val mTencentMap = mapView.map
        mTencentMap.addOnMapLoadedCallback(TencentMap.OnMapLoadedCallback {
            fun onMapLoaded(){}
        })

        /*地图结束*/

        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItPlace/GetOneByPk&id=$locationId",object: HttpCallbackListener{
            override fun onFinish(response: String) {
                try {
                    val jsonObject = JSONObject(response)
                    val dataJsonList = jsonObject.getJSONArray("data")
                    val tripJSONObject = dataJsonList.getJSONObject(0)
                    latitude = tripJSONObject.getDouble("latitude")
                    longitude = tripJSONObject.getDouble("longitude")
                    title = tripJSONObject.getString("place_name")
                    place_intro = tripJSONObject.getString("place_intro")
                    cover_img = tripJSONObject.getString("cover_img")
                    runOnUiThread(object : Runnable{
                        override fun run() {
                            place_name_view.text = title
                            place_intro_view.text = place_intro
                            Glide.with(this@LocationActivity).load(cover_img).override(cover_img_view.width,
                                Target.SIZE_ORIGINAL
                            ).fitCenter().into(cover_img_view)
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

    private fun map(){
        /*地图打卡点信息显示功能（覆盖物）*/
        val tencentMap = mapView.map
        //开启多窗口模式
        tencentMap.enableMultipleInfowindow(true)
        //传入数据
        val marker: Marker = tencentMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude,longitude)).icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
        )
        val center = LatLng(latitude,longitude)//设置中心点为第一个位置
        val latLngBounds: LatLngBounds = LatLngBounds.Builder().include(arrayListOf(LatLng(latitude,longitude),LatLng(latitude,longitude))).build()
        tencentMap.animateCamera(
            CameraUpdateFactory.newLatLngBoundsWithMapCenter(
                latLngBounds,
                center,
                100
            )
        )
    }

    /*获取手机中安装的app的名称*/
    private fun getPackageNames(context: Context):ArrayList<String>{
        //获取packagemanager
        val packageManager = context.packageManager;
        //获取所有已安装程序的包信息
        val packageInfos = packageManager.getInstalledApplications(0)
        //用于存储所有已安装程序的包名
        val packageNames: ArrayList<String> = ArrayList()
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null){
            for (item in packageInfos)
                packageNames.add(item.packageName)
        }
        return packageNames
    }

    /*判断指定APP是否安装*/
    private fun isAvilible(packageNames : ArrayList<String>, appName:String):Boolean{
        return packageNames.contains(appName)
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

    }


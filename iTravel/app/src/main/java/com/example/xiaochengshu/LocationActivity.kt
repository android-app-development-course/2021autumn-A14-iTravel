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

        /*??????*/
        val maplayout = MapLayout(this)//????????????MapLayout????????????LinearLayout
        maplayout.setScrollView(locationSrollView)//?????????MapLayout???????????????????????????scrollview

        mapView = MapView(this)//??????????????????
        maplayout.addView(mapView)//???Maplayout?????????????????????

        map.addView(maplayout)//?????????????????????????????????Maplayout

        val mTencentMap = mapView.map
        mTencentMap.addOnMapLoadedCallback(TencentMap.OnMapLoadedCallback {
            fun onMapLoaded(){}
        })

        /*????????????*/

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
        /*????????????????????????????????????????????????*/
        val tencentMap = mapView.map
        //?????????????????????
        tencentMap.enableMultipleInfowindow(true)
        //????????????
        val marker: Marker = tencentMap.addMarker(
            MarkerOptions()
                .position(LatLng(latitude,longitude)).icon(
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
        )
        val center = LatLng(latitude,longitude)//?????????????????????????????????
        val latLngBounds: LatLngBounds = LatLngBounds.Builder().include(arrayListOf(LatLng(latitude,longitude),LatLng(latitude,longitude))).build()
        tencentMap.animateCamera(
            CameraUpdateFactory.newLatLngBoundsWithMapCenter(
                latLngBounds,
                center,
                100
            )
        )
    }

    /*????????????????????????app?????????*/
    private fun getPackageNames(context: Context):ArrayList<String>{
        //??????packagemanager
        val packageManager = context.packageManager;
        //???????????????????????????????????????
        val packageInfos = packageManager.getInstalledApplications(0)
        //??????????????????????????????????????????
        val packageNames: ArrayList<String> = ArrayList()
        //???pinfo????????????????????????????????????pName list???
        if (packageInfos != null){
            for (item in packageInfos)
                packageNames.add(item.packageName)
        }
        return packageNames
    }

    /*????????????APP????????????*/
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


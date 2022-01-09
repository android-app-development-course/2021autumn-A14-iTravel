package com.example.xiaochengshu

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.picture_layout.*
import com.tencent.tencentmap.mapsdk.maps.MapView
import com.tencent.tencentmap.mapsdk.maps.TencentMap

import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory
import com.tencent.tencentmap.mapsdk.maps.model.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.comments_item.*
import org.json.JSONObject
import java.net.URISyntaxException
import kotlin.math.log


class TripActivity : AppCompatActivity() ,View.OnClickListener{
    var id = 0
    lateinit var mapView : MapView
    private val TripPointList=ArrayList<TripPoint>()
    private val commentsList=ArrayList<Comments>()
    private var comment_adapter:CommentAdapter?=null
    val picture_list = arrayListOf<String>()//图片轮播
    var id_array= arrayListOf<String>()//存放收藏id
    var flag=false

    var guide_id:String="1"
    val user_id = BitmapApplication.getMyUserId()
    private val markerIdList = ArrayList<Int>()

    private var adapter:CommentAdapter?=null
    /*Infowindow数据*/
    val latLngArrayList = arrayListOf<LatLng>()
    val titles = arrayListOf<String>()
    val snippets = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip)

        id = intent.getIntExtra("ID",0)
        /*显示行程攻略详细信息*/
        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItGuide/GetOneByPk&id=$id",object: HttpCallbackListener{
            override fun onFinish(response: String) {
                try {
                    val jsonObject = JSONObject(response)
                    val dataJsonList = jsonObject.getJSONArray("data")
                    val tripJSONObject = dataJsonList.getJSONObject(0)
                    Log.d("3332323233222222",dataJsonList.toString())
                    runOnUiThread(object : Runnable{
                        override fun run() {
                            trip_title.text = tripJSONObject.getString("title")
                            content.text = tripJSONObject.getString("short_text")
                            author.text = tripJSONObject.getString("author")
                            Glide.with(this@TripActivity).load(tripJSONObject.getString("head_img")).into(authorImage)
                            picture_list.add(tripJSONObject.getString("cover_img"))
                            guide_id = tripJSONObject.getString("id")
                            Log.d("guide_id",guide_id)
                            NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItCollect/GetCollectList&user_id=$user_id",object: HttpCallbackListener{
                                override fun onFinish(response: String) {
                                    try {
                                        val jsonObject = JSONObject(response)
                                        val dataJsonList = jsonObject.getJSONArray("data")

                                        for (index in 0 until dataJsonList.length()){
                                            val id= dataJsonList.getJSONObject(index).getString("id")
                                            id_array.add(id)
                                        }
                                        for (i in 0 until  id_array.size){
                                            if (guide_id==id_array[i]){
                                                flag=true
                                                if(flag) {
                                                    collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collected, 0, 0, 0)
                                                }
                                                break
                                            }
                                        }
                                    } catch (e: java.lang.Exception) {
                                        e.printStackTrace()
                                    }
                                }
                                override fun onError(e: Exception) {

                                }

                            })


                        }
                    })

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Exception) {

            }

        })

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


        back_button.setOnClickListener {
            this.finish()
        }


        start.setOnClickListener {//打卡
            val intent = Intent(this,zhengzaidaka::class.java)
            intent.putExtra("ID",id)
            intent.putExtra("title",trip_title.text)
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
                var comments:Comments = Comments(user_id, user_name, true, 5, content)

                commentsList.add(comments)
                //记录刷新
                adapter?.notifyItemInserted(commentsList.size-1)
                recyclerView2.scrollToPosition(commentsList.size-1)
                comment_input.setText("")//设置为空
            }
        }

            collect_button.setOnClickListener{
            //如果flag为true则说明已经种草过了
            if(!flag){
                collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collected, 0, 0, 0)
                //通过 id 记录该数据到收藏数据表
                    NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItCollect/CreateOne&ItCollect[guide_id]=$guide_id&ItCollect[user_id]=$user_id",
                        object : HttpCallbackListener {
                            override fun onFinish(response: String) {
                                try {
                                    val jsonObject = JSONObject(response)
                                    val dataJsonList = jsonObject.getJSONObject("data")
                                    Log.d("hhhhhhhhh",dataJsonList.toString())
                                    //获取后台返回的数据是成功还是失败
                                    runOnUiThread(object : Runnable {
                                        override fun run() {
                                            Toast.makeText(this@TripActivity,"种草成功",Toast.LENGTH_SHORT).show()
                                            flag=true
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
            else{
                collect_button.setCompoundDrawablesRelativeWithIntrinsicBounds(com.example.xiaochengshu.R.mipmap.collect, 0, 0, 0)
                //通过 id 删除收藏数据表的该记录
                NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItCollect/CancelCollect&guide_id=$guide_id&user_id=$user_id",
                    object : HttpCallbackListener {
                        override fun onFinish(response: String) {
                            try {
                                val jsonObject = JSONObject(response)
                                val dataJsonList = jsonObject.getJSONObject("data")
                                Log.d("取消收藏",dataJsonList.toString())
                                //获取后台返回的数据是成功还是失败
                                runOnUiThread(object : Runnable {
                                    override fun run() {
                                        Toast.makeText(this@TripActivity,"取消种草",Toast.LENGTH_SHORT).show()
                                        flag=false
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
        }
    }

    //
    override fun onClick(v:View){
        when(v?.id){
            R.id.collect_button->{
                "@mipmap/collected"
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
                        picture_list.add(dataJsonList.getJSONObject(i).getString("place_intro"))
                    }
                    runOnUiThread(object : Runnable{
                        override fun run() {
                            val adapter = PictureAdapter(picture_list,this@TripActivity)//数据传入适配器
                            picture_view_pager.adapter = adapter
                            recyclerView.adapter=TripPointAdapter(TripPointList,this@TripActivity)
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


    private fun initCommentsList(){
        repeat(1){
            commentsList.add(Comments(R.drawable.touxiang,"张三",true,5,"已打卡"))
            commentsList.add(Comments(R.drawable.touxiang,"张三",false,5,"已打卡"))
            commentsList.add(Comments(R.drawable.touxiang,"张三",false,5,"已打卡"))
            commentsList.add(Comments(R.drawable.touxiang,"张三",true,5,"已打卡"))
        }
    }

    //实现点击空白处隐藏软键盘，点击编辑框时显示软键盘
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.getAction() === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev!!)) {//点击的是其他区域，则调用系统方法隐藏软键盘
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    imm!!.hideSoftInputFromWindow(v!!.windowToken, 0)
                }
            }
            return super.dispatchTouchEvent(ev)
        }
// 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    /**

    判断是否是输入框区域
     */
    fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null) {
            when (v.id) {
                R.id.comment_input -> {
                    val leftTop = intArrayOf(0, 0)
//获取输入框当前的location位置
                    v!!.getLocationInWindow(leftTop)
                    val left = leftTop[0]
                    val top = leftTop[1]
                    val bottom = top + v!!.getHeight()
                    val right = left+v!!.getWidth()
                    return if (event.x > left && event.x < right && event.y > top && event.y < bottom) {
// 点击的是输入框区域，保留点击EditText的事件
                        false
                    } else {
                        true
                    }
                }
                else -> {
                    return false
                }
            }
        }
        return false
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
                    Toast.makeText(this@TripActivity, p0.title + "被点击了！", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@TripActivity,LocationActivity::class.java)
                    val id = markerIdList.indexOf(p0.id.toInt())
                    intent.putExtra("ID",TripPointList[id].id)
                    Toast.makeText(this@TripActivity, TripPointList[id].id.toString(), Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }

            override fun onInfoWindowClickLocation(p0: Int, p1: Int, p2: Int, p3: Int) {

            }

        })
        val center = latLngArrayList[0]//设置中心点为第一个位置
        //调整视野范围，包含所有点
        var latLngBounds: LatLngBounds
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
package com.example.xiaochengshu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.discover_layout.*
import kotlinx.android.synthetic.main.trip_layout.*
import kotlinx.android.synthetic.main.trip_layout.discoverRecycle
import org.json.JSONObject
import kotlin.concurrent.thread

class PunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.trip_layout)


       // val layoutManager = LinearLayoutManager(this)
        /*设置viewpager*/
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)//列数为2，方向垂直
        discoverRecycle.layoutManager = layoutManager
        val adapter = DiscoverAdapter(this)//数据传入适配器
        discoverRecycle.adapter = adapter

        /*初始化数据*/
        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItGuide/GetAllList",object: HttpCallbackListener{
            override fun onFinish(response: String) {
                try {
                    Log.d("bbbbbbbbb",response.toString())
                    val jsonObject = JSONObject(response)
                    val dataJsonList = jsonObject.getJSONArray("data")
                    runOnUiThread { adapter.initData(dataJsonList) }//一定要在主线程中更新UI
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Exception) {

            }

        })

        discoverRecycle.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var loading = false//标记是不是正在加载数据，防止重复加载
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(!recyclerView.canScrollVertically(1) && !loading){
                    loading = true
                    Toast.makeText(this@PunchActivity, "加载中！", Toast.LENGTH_SHORT).show()
                    NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItGuide/GetAllList",object: HttpCallbackListener{
                        override fun onFinish(response: String) {
                            try {
                                Log.d("bbbbbbbbb",response.toString())
                                val jsonObject = JSONObject(response)
                                val dataJsonList = jsonObject.getJSONArray("data")
                                runOnUiThread { adapter.addData(dataJsonList) }//一定要在主线程中更新UI
                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onError(e: Exception) {

                        }

                    })
                    loading = false
                }
            }
        })
    }


}
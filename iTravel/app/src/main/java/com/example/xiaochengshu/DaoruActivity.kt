package com.example.xiaochengshu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.daoru_layout.*
import kotlinx.android.synthetic.main.trip_layout.*
import kotlinx.android.synthetic.main.trip_layout.discoverRecycle
import org.json.JSONObject

class DaoruActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daoru_layout)

        var offset = 0//从第几条记录开始获取数据

        /*设置viewpager*/
        val layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)//列数为2，方向垂直
        discoverRecycle.layoutManager = layoutManager
        val adapter = DaoruAdapter(this)//数据传入适配器
        discoverRecycle.adapter = adapter


        /*初始化数据*/
        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItGuide/GetAllList&offset=$offset",
            object : HttpCallbackListener {
                override fun onFinish(response: String) {
                    try {
                        Log.d("bbbbbbbbb", response.toString())
                        val jsonObject = JSONObject(response)
                        val dataJsonList = jsonObject.getJSONArray("data")
                        offset += dataJsonList.length()
                        runOnUiThread { adapter.initData(dataJsonList) }//一定要在主线程中更新UI
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Exception) {

                }

            })

        discoverRecycle.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var loading = false//标记是不是正在加载数据，防止重复加载
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !loading) {
                    loading = true
                    Toast.makeText(this@DaoruActivity, "到底了！", Toast.LENGTH_SHORT).show()
                    NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItGuide/GetAllList&offset=$offset",
                        object : HttpCallbackListener {
                            override fun onFinish(response: String) {
                                try {
                                    Log.d("bbbbbbbbb", response.toString())
                                    val jsonObject = JSONObject(response)
                                    val dataJsonList = jsonObject.getJSONArray("data")
                                    offset += dataJsonList.length()
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

        lijidaka.setOnClickListener {
            val id=adapter.xuan
            val intent = Intent(this,com.example.xiaochengshu.zhengzaidaka::class.java)
            intent.putExtra("ID",adapter.tripList[id].getInt("id"))
            intent.putExtra("title",adapter.tripList[id].getString("title"))
            Toast.makeText(this,id.toString(),Toast.LENGTH_LONG)
            this.startActivity(intent)
            finish()


        }
    }
}
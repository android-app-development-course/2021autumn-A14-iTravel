package com.example.xiaochengshu

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.add_layout.*
import kotlinx.android.synthetic.main.add_layout.addIntroductionButton
import kotlinx.android.synthetic.main.trip_layout.*
import kotlinx.android.synthetic.main.trip_layout.discoverRecycle
import org.json.JSONObject

class TripFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.trip_layout,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



        super.onViewCreated(view, savedInstanceState)
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
                    activity?.runOnUiThread { adapter.initData(dataJsonList) }//一定要在主线程中更新UI
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
                    Toast.makeText(activity, "加载中！", Toast.LENGTH_SHORT).show()
                    NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItGuide/GetAllList",object: HttpCallbackListener{
                        override fun onFinish(response: String) {
                            try {
                                Log.d("bbbbbbbbb",response.toString())
                                val jsonObject = JSONObject(response)
                                val dataJsonList = jsonObject.getJSONArray("data")
                                activity?.runOnUiThread { adapter.addData(dataJsonList) }//一定要在主线程中更新UI
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

        daoru.setOnClickListener {
            val context: Context = view.context
            val intent = Intent(context,DaoruActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (BitmapApplication.getZhengzaidakaId() == 0){
            zhengzaidaka.visibility = View.GONE
            weidaka.visibility = View.VISIBLE
        }
        else{
            weidaka.visibility = View.GONE
            zhengzaidaka.visibility = View.VISIBLE
            NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItGuide/GetOneByPk&id=${BitmapApplication.getZhengzaidakaId()}",object: HttpCallbackListener{
                override fun onFinish(response: String) {
                    try {
                        val jsonObject = JSONObject(response)
                        val dataJsonList = jsonObject.getJSONArray("data")
                        val tripJSONObject = dataJsonList.getJSONObject(0)
                        activity?.runOnUiThread(object : Runnable{
                            override fun run() {
                                title.text = tripJSONObject.getString("title")
                                intro.text = tripJSONObject.getString("short_text")
                                Glide.with(activity!!).load(tripJSONObject.getString("cover_img")).into(cover)
                            }
                        })

                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Exception) {

                }

            })

            back_to.setOnClickListener {
                val intent = Intent(activity, com.example.xiaochengshu.zhengzaidaka::class.java)
                intent.putExtra("ID",BitmapApplication.getZhengzaidakaId())
                intent.putExtra("title",title.text)
                this.startActivity(intent)
            }
        }
    }

}
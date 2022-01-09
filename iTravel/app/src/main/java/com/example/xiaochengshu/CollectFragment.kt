package com.example.xiaochengshu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.comments_item.*
import kotlinx.android.synthetic.main.discover_item.*
import kotlinx.android.synthetic.main.discover_layout.*
import org.json.JSONObject
import kotlin.Exception
import kotlin.concurrent.thread


class CollectFragment : Fragment() {
//    var offset = 0//从第几条记录开始获取数据
    val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)//列数为2，方向垂直
    val adapter = CollectAdapter(this)//数据传入适配器

    val user_id=BitmapApplication.getMyUserId()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.discover_layout,container,false)
//        var btn:Button = view.findViewById(R.id.button3)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /*可与punchactivity和tripactivity复用*/
        /*设置viewpager*/
        discoverRecycle.layoutManager = layoutManager
        discoverRecycle.adapter = adapter
        /*初始化数据*/
        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItCollect/GetCollectList&user_id=$user_id",object: HttpCallbackListener{
            override fun onFinish(response: String) {
                try {
                    Log.d("bbbbbbbbbawavzvszvsv",response.toString())
                    val jsonObject = JSONObject(response)
                    val dataJsonList = jsonObject.getJSONArray("data")
//                    offset += dataJsonList.length()
                    activity?.runOnUiThread { adapter.initData(dataJsonList) }//一定要在主线程中更新UI
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Exception) {

            }

        })

        /*设置下拉刷新*/
        discoverRefresh.setColorSchemeColors(0xFF5722)
        discoverRefresh.setOnRefreshListener {
            thread {
                reflsh()
                discoverRefresh.isRefreshing = false
            }
        }

//        discoverRecycle.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//            var loading = false//标记是不是正在加载数据，防止重复加载
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if(!recyclerView.canScrollVertically(1) && !loading){
//                    loading = true
//                    Toast.makeText(activity, "加载中！", Toast.LENGTH_SHORT).show()
//                    NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItCollect/GetCollectList&offset=$offset&user_id=$user_id",object: HttpCallbackListener{
//                        override fun onFinish(response: String) {
//                            try {
//                                Log.d("bbbbbbbbb",response.toString())
//                                val jsonObject = JSONObject(response)
//                                val dataJsonList = jsonObject.getJSONArray("data")
//                                offset += dataJsonList.length()
//                                activity?.runOnUiThread { adapter.addData(dataJsonList) }//一定要在主线程中更新UI
//                            } catch (e: java.lang.Exception) {
//                                e.printStackTrace()
//                            }
//                        }
//
//                        override fun onError(e: Exception) {
//
//                        }
//
//                    })
//                    loading = false
//                }
//            }
//        })

    }

    fun reflsh(){
        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/?r=IoItCollect/GetCollectList&user_id=$user_id",object: HttpCallbackListener{
            override fun onFinish(response: String) {
                try {
                    Log.d("bbbbbbbbb",response.toString())
                    val jsonObject = JSONObject(response)
                    val dataJsonList = jsonObject.getJSONArray("data")
                    activity?.runOnUiThread { adapter.updateData(dataJsonList) }//一定要在主线程中更新UI
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Exception) {

            }

        })

    }

    fun reflshWithEffect(){
        discoverRefresh.post(object : Runnable{
            override fun run() {
                discoverRecycle.scrollToPosition(0)//返回顶部
                discoverRefresh.isRefreshing = true//显示刷新动画
                reflsh()//刷新
            }
        })
    }


}
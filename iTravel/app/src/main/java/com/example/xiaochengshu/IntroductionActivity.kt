package com.example.xiaochengshu

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tencent.tencentmap.mapsdk.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_introduction.*
import kotlinx.android.synthetic.main.activity_introduction.back_button
import kotlinx.android.synthetic.main.activity_introduction.recyclerView
import kotlinx.android.synthetic.main.activity_trip.*
import org.json.JSONObject

class IntroductionActivity : BaseActivity() {

    private val EditingTripPointList = ArrayList<EditingTripPoint>()

    val adapter = EditingTripPointAdapter(EditingTripPointList,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        val layoutManager= LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=adapter

        val title = intent.getStringExtra("title")
        val intro = intent.getStringExtra("intro")
        val bitmap = BitmapApplication.getMyBitmap()
        val userId = BitmapApplication.getMyUserId()

        title_text.text = title
        intro_text.text = intro
        imageView3.setImageBitmap(bitmap)

        back_button.setOnClickListener {
            finish()
        }

        publish.setOnClickListener {
            //点击发布按钮后，向MainActivity发送广播
//            val intent = Intent("com.example.broadcasttest.MY_BROADCAST")
//            intent.setPackage(packageName)
//            sendBroadcast(intent)
            NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItGuide/CreateOne&ItGuide[title]=$title&ItGuide[short_text]=$intro&ItGuide[author_id]=$userId&ItGuide[author]=用户&ItGuide[is_public]=是",object: HttpCallbackListener{
                override fun onFinish(response: String) {
//                    NetworkRequest.uploadBitmap("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php",bitmap,"测试",object :HttpCallbackListener{
//                        override fun onFinish(response: String) {
//
//                        }
//
//                        override fun onError(e: Exception) {
//
//                        }
//
//                    })
                    val jsonObject = JSONObject(response)
                    val dataJsonObject = jsonObject.getJSONObject("data")
                    val id = dataJsonObject.getInt("id")
                    NetworkRequest.sendRequestWithOkHttp("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php",bitmap,id)
                    for (i in 0 until EditingTripPointList.size){
                        NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItPlace/CreateOne&ItPlace[place_name]=${EditingTripPointList[i].name}&ItPlace[place_intro]=${EditingTripPointList[i].content}&ItPlace[author]=用户&ItPlace[guide_id]=$id&ItPlace[stars]=5&ItPlace[longitude]=${EditingTripPointList[i].latLng.longitude}&ItPlace[latitude]=${EditingTripPointList[i].latLng.latitude}&ItPlace[f_order]=1",object: HttpCallbackListener{
                            override fun onFinish(response: String) {
                                val jsonObject = JSONObject(response)
                                val dataJsonObject = jsonObject.getJSONObject("data")
                                val id = dataJsonObject.getInt("id")
                                NetworkRequest.sendImageWithOkHttp("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php",EditingTripPointList[i].bitmap,id)
                            }
                            override fun onError(e: Exception) {

                            }
                        })
                    }
                    val intent = Intent()
                    setResult(RESULT_OK,intent)
                    finish()
                }
                override fun onError(e: Exception) {
                    Toast.makeText(this@IntroductionActivity,"行程创建失败！",Toast.LENGTH_SHORT).show()
                }
            })
        }

        addTrip.setOnClickListener {
            val intent = Intent(this,CreateTripActivity::class.java)
            startActivityForResult(intent,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {//获取添加的行程的数据
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                if (data != null) {
                    val location_title = data.getStringExtra("location_title")
                    val intro = data.getStringExtra("intro")
                    val location_LatLng = data.getParcelableExtra<LatLng>("location_LatLng")
                    val bitmap = BitmapApplication.getMyBitmap()
                    if (!location_title.isNullOrEmpty() && !intro.isNullOrEmpty() && location_LatLng!=null && bitmap!=null){
                        EditingTripPointList.add(EditingTripPoint(location_title,intro,bitmap,location_LatLng))
                        adapter.update(EditingTripPointList)
                    }
                }
            }
        }
    }
}
package com.example.xiaochengshu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.tencent.tencentmap.mapsdk.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_introduction.addTrip
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.edit_trip_layout.*
import kotlinx.android.synthetic.main.edit_trip_layout.recyclerView
import org.json.JSONObject

class edit_trip : AppCompatActivity() {
    private val TripPointList = ArrayList<TripPoint>()
    var id = 0
    /*Infowindow数据*/
    val latLngArrayList = arrayListOf<LatLng>()
    val titles = arrayListOf<String>()
    val snippets = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_trip_layout)

        id = intent.getIntExtra("ID",0)
        initTripPoints()

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        addTrip.setOnClickListener {
            val intent = Intent(this,CreateTripActivity::class.java)
            startActivity(intent)
        }
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
                            recyclerView.adapter=TripPoint_edit_Adapter(TripPointList,this@edit_trip)
                        }
                    })
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Exception) {}
        })
    }
}
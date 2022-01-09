package com.example.xiaochengshu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_fans.*

class FansActivity : AppCompatActivity() {
    private val fansList = ArrayList<Fans>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fans)
        initFans()//初始化粉丝
        val layoutManager = LinearLayoutManager(this)
        fansRecyclerView.layoutManager = layoutManager
        val adapter = FansAdapter(fansList)
        fansRecyclerView.adapter = adapter
        back_button.setOnClickListener{
            this.finish()
        }
    }
    private fun initFans(){
        fansList.add(Fans(R.drawable.touxiang,"小明","开启每一天"))
    }
}
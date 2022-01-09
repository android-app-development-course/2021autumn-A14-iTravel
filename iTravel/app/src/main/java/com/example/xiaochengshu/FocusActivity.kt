package com.example.xiaochengshu

import android.R.attr
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_fans.*
import kotlinx.android.synthetic.main.activity_fans.back_button
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.focus_item.*
import kotlinx.android.synthetic.main.focus_layout.*
import android.R.attr.button
import android.view.View
import android.widget.Toast


class FocusActivity : AppCompatActivity() {

    private val focusList = ArrayList<Focus>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.focus_layout)
        initFocus()//初始化粉丝
        val layoutManager = LinearLayoutManager(this)
        focusRecyclerView.layoutManager = layoutManager
        val adapter = FocusAdapter(focusList)
        focusRecyclerView.adapter = adapter
        back_button.setOnClickListener{
            this.finish()
        }


    }
    private fun initFocus(){
        var profile="还没有简介"
        focusList.add(Focus(R.drawable.touxiang,"小明",profile))
        focusList.add(Focus(R.drawable.touxiang,"小红",profile))
    }
}
package com.example.xiaochengshu
//
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_trip.*
import kotlinx.android.synthetic.main.focus_layout.*
import kotlinx.android.synthetic.main.message_layout.*

import java.util.*
import kotlin.collections.ArrayList
//
class MessageFragment:Fragment(){
    private val messageList = ArrayList<Messages>()
    private val  focusList = ArrayList<Focus>()
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message_layout: LinearLayout = view.findViewById(R.id.message_item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.message_layout, container, false)
    }
//
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//
//        /*初始化数据*/
        initSendMessage()
//        /*设置viewpager*/
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)//列数为2，方向垂直
        messageRecylerView.layoutManager = layoutManager
        val adapter = MessageAdapter(messageList)//数据传入适配器
    messageRecylerView.adapter = adapter

//    val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)//列数为2，方向垂直
//    focusRecycle.layoutManager = layoutManager
//    val adapter = FocusAdapter(focusList)//数据传入适配器
//    focusRecycle.adapter = adapter
    }
//
    private fun initSendMessage() {
        repeat(3) {
            messageList.add(Messages(R.drawable.touxiang, "Jack", "消息内容","12-24"))
            messageList.add(Messages(R.drawable.touxiang, "小明", "消息内容","12-24"))
            messageList.add(Messages(R.drawable.touxiang, "小红", "消息内容","12-24"))

        }
    }
}
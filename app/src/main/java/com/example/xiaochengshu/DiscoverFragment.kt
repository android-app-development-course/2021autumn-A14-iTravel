package com.example.xiaochengshu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.discover_layout.*
import kotlin.concurrent.thread

class DiscoverFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.discover_layout,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /*初始化数据*/
        val titleList = listOf("白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生","白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生","白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生","白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生","白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生","白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生","白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生","白云区最美的图书馆，看我怎么吹爆它","所以那么认真学习是为了什么啊","22岁男生花三个月工资把老家厨房重新装修",
            "我真的很享受自律。","同一个宿舍6个985金融男，毕业十五年后的真实人生")

        /*设置viewpager*/
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)//列数为2，方向垂直
        discoverRecycle.layoutManager = layoutManager
        val adapter = DiscoverAdapter(titleList)//数据传入适配器
        discoverRecycle.adapter = adapter

        /*设置下拉刷新*/
        discoverRefresh.setColorSchemeColors(0xFF5722)
        discoverRefresh.setOnRefreshListener {
            thread {
                Thread.sleep(2000)
                discoverRefresh.isRefreshing = false
            }
        }
    }
}
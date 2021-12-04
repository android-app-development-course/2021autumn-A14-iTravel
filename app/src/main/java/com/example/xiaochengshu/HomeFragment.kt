package com.example.xiaochengshu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.home_layout.*

import android.util.Log

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_layout,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //设置首页viewpager的adapter
        viewpager.adapter = object : FragmentStateAdapter(this){
            override fun getItemCount() = 3//页数

            override fun createFragment(position: Int) = //设置对应的fragment
                when(position){
                    0 -> FocusFragment()
                    1 -> DiscoverFragment()
                    else -> NearFragment()
                }
        }

        //关联tablayout和viewpager
        TabLayoutMediator(tablayout,viewpager){tab,position ->
            when(position){
                0->tab.text = "关注"
                1->tab.text = "发现"
                else -> tab.text = "附近"
            }
        }.attach()

        viewpager.setCurrentItem(1)//设置默认页
    }
}
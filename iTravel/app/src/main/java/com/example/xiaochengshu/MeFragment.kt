package com.example.xiaochengshu

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.home_layout.*
import kotlinx.android.synthetic.main.me_layout.*
import kotlinx.android.synthetic.main.me_layout.viewpager
import kotlin.math.log

class MeFragment : Fragment() {
    val original_fragment = OriginalFragment()//原创
    val collect_fragment = CollectFragment()//收藏
    val carve_fragment = OriginalFragment()//复刻

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var bundle = arguments
        Log.d("22211",bundle.toString())
        return inflater.inflate(R.layout.me_layout,container,false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var bundle = arguments
        Log.d("3233",bundle.toString())
        //点击编辑按钮跳转到个人信息编辑界面
        edit_profile.setOnClickListener {
            var intent = Intent(activity,MyInfoActivity::class.java)
            startActivity(intent)
        }
        //点击设置跳转到设置界面
        set_id.setOnClickListener {
            var intent = Intent(activity,SetActivity::class.java)
            startActivity(intent)
        }
        //点击关注跳转到关注界面
        focusId.setOnClickListener {
            val intent = Intent(activity, FocusActivity::class.java)
            //intent.putExtra("id", 0) //在这里传递参数
            this.startActivity(intent)
        }
        //点击粉丝跳转到粉丝界面
        fansId.setOnClickListener {
            val intent = Intent(activity, FansActivity::class.java)
            //intent.putExtra("id", 0) //在这里传递参数
            this.startActivity(intent)
        }
        //点击收藏和点赞弹窗收藏点赞弹窗界面
        collect_id.setOnClickListener {
            val intent = Intent(activity, CollectDialogActivity::class.java)
            //intent.putExtra("id", 0) //在这里传递参数
            this.startActivity(intent)
        }
    }

    private var value: String? = null

    public fun setValue(value: String) {
        this.value = value
    }

    public fun getValue(): String? {
        return value
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var bundle = arguments
        Log.d("1222222222222",bundle.toString())
        if(bundle!=null){
//            edit_profile.visibility(View.INVISIBLE)
            edit_profile.setVisibility(View.GONE)
            set_id.setVisibility(View.GONE)
            bundle=null
        }

        //设置原创页面
        viewpager.adapter = object :FragmentStateAdapter(this){
            override fun getItemCount() = 3//页数
            override fun createFragment(position: Int)=
                when(position){
                    0->original_fragment
                    1->collect_fragment
                    else->carve_fragment
            }

        }
        //关联tablayout和viewpage
        TabLayoutMediator(my_table,viewpager){tab,position->
            when(position){
                0->tab.text="原创"
                1->tab.text = "已种草"
                else -> tab.text = "已打卡"
            }
        }.attach()
        viewpager.setCurrentItem(0)//设置默认页

    }

    override fun onResume() {
        original_fragment.reflsh()
        collect_fragment.reflsh()
        carve_fragment.reflsh()
        super.onResume()
    }
}


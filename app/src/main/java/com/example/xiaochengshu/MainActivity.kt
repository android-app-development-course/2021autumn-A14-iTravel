package com.example.xiaochengshu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val home_fragment = HomeFragment()
    val store_fragment = TripFragment()
    val add_fragment = AddFragment()
    val message_fragment = MessageFragment()
    val me_fragment = MeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        /*初始化*/
        /*
        动态添加Fragment主要分为4步：
        1.获取到FragmentManager，在V4包中通过getSupportFragmentManager，在系统中原生的Fragment是通过getFragmentManager获得的。
        2.开启一个事务，通过调用beginTransaction方法开启。
        3.向容器内加入Fragment，一般使用add或者replace方法实现，需要传入容器的id和Fragment的实例。
        4.提交事务，调用commit方法提交。
        这部分有关fragment的操作看不大懂也没关系，下节我们会具体讲有关Fragment的管理！
         */
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.add(R.id.fragmentlayout, home_fragment)
        transaction.add(R.id.fragmentlayout, store_fragment)
        transaction.add(R.id.fragmentlayout, add_fragment)
        transaction.add(R.id.fragmentlayout, message_fragment)
        transaction.add(R.id.fragmentlayout, me_fragment)
        transaction.hide(store_fragment)
        transaction.hide(add_fragment)
        transaction.hide(message_fragment)
        transaction.hide(me_fragment)
        transaction.commit()

        //设置按钮文字大小
        home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
        store.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

        //设置底部导航栏的按钮功能
        home.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
            transaction.hide(me_fragment)
            if (home_fragment.isAdded)
                transaction.show(home_fragment)
            else
                transaction.add(R.id.fragmentlayout,home_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
            store.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        store.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
            transaction.hide(me_fragment)
            if (store_fragment.isAdded)
                transaction.show(store_fragment)
            else
                transaction.add(R.id.fragmentlayout,store_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            store.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        add.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(store_fragment)
            transaction.hide(message_fragment)
            transaction.hide(me_fragment)
            if (add_fragment.isAdded)
                transaction.show(add_fragment)
            else
                transaction.add(R.id.fragmentlayout,add_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            store.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        massage.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(me_fragment)
            if (message_fragment.isAdded)
                transaction.show(message_fragment)
            else
                transaction.add(R.id.fragmentlayout,message_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            store.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        me.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
            if (me_fragment.isAdded)
                transaction.show(me_fragment)
            else
                transaction.add(R.id.fragmentlayout,me_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            store.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
        }
    }
}
package com.example.xiaochengshu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.comments_item.*
import kotlinx.android.synthetic.main.home_layout.*






class MainActivity : BaseActivity() {
    val home_fragment = HomeFragment()
    val store_fragment = TripFragment()
    val add_fragment = AddFragment()
    val message_fragment = MessageFragment()
    val me_fragment = MeFragment()

    val userId=BitmapApplication.getMyUserId()

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
        trip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)

        //设置底部导航栏的按钮功能
        home.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            if (!home_fragment.isHidden && home_fragment.tablayout.selectedTabPosition == 1)//正在被显示则返回顶部刷新
                home_fragment.discover_fragment.reflshWithEffect()

            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
            transaction.hide(me_fragment)
            if (home_fragment.isAdded)
                transaction.show(home_fragment)
            else
                transaction.add(R.id.fragmentlayout, home_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
            trip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        trip.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
            transaction.hide(me_fragment)
            if (store_fragment.isAdded)
                transaction.show(store_fragment)
            else
                transaction.add(R.id.fragmentlayout, store_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            trip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        add.setOnClickListener {
            val intent = Intent(this,CreateIntroductionActivity::class.java)
            startActivity(intent)
        }

        massage.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(me_fragment)
            if (message_fragment.isAdded)
                transaction.show(message_fragment)
            else
                transaction.add(R.id.fragmentlayout, message_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            trip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }

        me.setOnClickListener {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
            if (me_fragment.isAdded)
                transaction.show(me_fragment)
            else
                transaction.add(R.id.fragmentlayout, me_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            trip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
        }
        val bundle = Bundle()

        val user_name = intent!!.getStringExtra("user_name")
        if (user_name != null) {
            Log.d("1111111222222","23333333")
            bundle.putString("user_name", user_name)
            me_fragment.setArguments(bundle)
        }

    }


    override fun onResume() {
        super.onResume()
        val user_name = intent!!.getStringExtra("user_name")
        if (user_name != null) {
            val bundle = Bundle()
            bundle.putString("user_name", user_name)

            me_fragment.setArguments(bundle)
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.hide(home_fragment)
            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
//            var me_fragment =
//            me_fragment.
            if (me_fragment.isAdded){
                transaction.show(me_fragment)
            }
            else
                transaction.add(R.id.fragmentlayout, me_fragment)
                transaction.commit()



            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            trip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
        }
    }


    //内部类：广播接收器
    inner class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()

            if (!home_fragment.isHidden && home_fragment.tablayout.selectedTabPosition == 1)//正在被显示则返回顶部刷新
                home_fragment.discover_fragment.reflshWithEffect()

            transaction.hide(store_fragment)
            transaction.hide(add_fragment)
            transaction.hide(message_fragment)
            transaction.hide(me_fragment)
            if (home_fragment.isAdded)
                transaction.show(home_fragment)
            else
                transaction.add(R.id.fragmentlayout, home_fragment)
            transaction.commit()

            home.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
            trip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            massage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
            me.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F)
        }
    }

}




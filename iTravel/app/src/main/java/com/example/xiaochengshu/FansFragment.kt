package com.example.xiaochengshu
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
import java.util.*
import kotlin.collections.ArrayList
class FansFragment: Fragment() {
    private val  focusList = ArrayList<Focus>()

    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val  focus_layout:LinearLayout = view.findViewById(R.id.focus_item)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.focus_layout,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /*初始化数据*/
        initFans()
        /*设置viewpager*/
        val layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)//列数为2，方向垂直
        focusRecyclerView.layoutManager = layoutManager
        val adapter = FocusAdapter(focusList)//数据传入适配器
        focusRecyclerView.adapter = adapter
    }
    private fun initFans() {
        repeat(2) {
            focusList.add(Focus(R.drawable.touxiang,"Jack","粉丝·7"))
            focusList.add(Focus(R.drawable.touxiang,"小明","粉丝·3"))
            focusList.add(Focus(R.drawable.touxiang,"小红","粉丝·2"))
        }
    }

}
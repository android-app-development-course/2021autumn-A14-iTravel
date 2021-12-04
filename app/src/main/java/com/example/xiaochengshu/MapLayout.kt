package com.example.xiaochengshu

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.ScrollView

class MapLayout : LinearLayout {
    private var scrollView: ScrollView? = null
    constructor(context: Context) : super(context){}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){}

    fun setScrollView(scrollView: ScrollView?) {
        this.scrollView = scrollView
    }

    /*解决scrollview和map冲突的问题*/
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            scrollView?.requestDisallowInterceptTouchEvent(false)
        } else {
            scrollView?.requestDisallowInterceptTouchEvent(true)
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}
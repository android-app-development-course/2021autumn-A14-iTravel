package com.example.xiaochengshu

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import kotlinx.android.synthetic.main.activity_login.input_account
import kotlinx.android.synthetic.main.activity_login.input_pwd
import kotlinx.android.synthetic.main.activity_login.register
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_trip.*
interface Function {

    /**
    判断是否是输入框区域
     */
    fun isShouldHideInput(v: View?, event: MotionEvent,idList:ArrayList<String>): Boolean {

        var str:String
        str = "R.id."
        str+=idList[0].toString()
        println(str)
        if (v != null) {
            when (v.id) {
                R.id.input_account,R.id.input_pwd,R.id.input_new_pwd -> {
                    val leftTop = intArrayOf(0, 0)
//获取输入框当前的location位置
                    v!!.getLocationInWindow(leftTop)
                    val left = leftTop[0]
                    val top = leftTop[1]
                    val bottom = top + v!!.getHeight()
                    val right = left+v!!.getWidth()
                    return if (event.x > left && event.x < right && event.y > top && event.y < bottom) {
// 点击的是输入框区域，保留点击EditText的事件
                        false
                    } else {
                        true
                    }
                }
                else -> {
                    return false
                }
            }
        }
        return false
    }

}
package com.example.xiaochengshu

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_introduction.*
import kotlinx.android.synthetic.main.activity_login.input_account
import kotlinx.android.synthetic.main.activity_login.input_pwd
import kotlinx.android.synthetic.main.activity_login.register
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_trip.*
import org.json.JSONObject

class RegisterActivity : AppCompatActivity(),Function {
    private val idList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView (R.layout.activity_register)
        //注册
        register.setOnClickListener {
            val account = input_account.text.toString()
            val pwd = input_pwd.text.toString()
            val newPwd = input_new_pwd.text.toString()
            if (account == "" || pwd == "" || newPwd == "") {
                Toast.makeText(
                    this, R.string.enter_act_and_psw,
                    Toast.LENGTH_LONG
                ).show()
            } else if (pwd != newPwd) {
                    Toast.makeText(this, R.string.makesure, Toast.LENGTH_LONG).show()

            }
            else{

                NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItGuide/Register&username=$account&password=$pwd",
                    object : HttpCallbackListener {
                        override fun onFinish(response: String) {
                            try {
                                val jsonObject = JSONObject(response)
                                val dataJsonList = jsonObject.getJSONObject("data")
                                //获取后台返回的数据是成功还是失败
                                val msg = dataJsonList.getString("msg")
                                runOnUiThread(object : Runnable {
                                    override fun run() {
                                        if (msg == "注册成功" ) {
                                            Toast.makeText(
                                                this@RegisterActivity, "注册成功！！！", Toast.LENGTH_SHORT
                                            ).show()
                                            val intent = Intent(
                                                this@RegisterActivity,
                                                LoginActivity::class.java
                                            )
                                            startActivity(intent)
                                            finish()
                                        } else if (msg == "用户名已存在") {
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "用户名已存在，请前往登录！",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                "注册失败！！！",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                })

                            } catch (e: java.lang.Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onError(e: Exception) {
                        }
                    })
            }
        }
        //登录按钮的点击事件
        login.setOnClickListener {
            finish()
        }
        idList.add("input_account")
        idList.add("input_pwd")
    }


//实现点击空白处隐藏软键盘，点击编辑框时显示软键盘
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev!!.getAction() === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev!!,idList)) {//点击的是其他区域，则调用系统方法隐藏软键盘
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) {
                    imm!!.hideSoftInputFromWindow(v!!.windowToken, 0)
                }
            }
            return super.dispatchTouchEvent(ev)
        }
// 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

}
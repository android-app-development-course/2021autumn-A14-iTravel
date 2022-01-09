package com.example.xiaochengshu

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.input_account
import kotlinx.android.synthetic.main.activity_login.input_pwd
import kotlinx.android.synthetic.main.activity_login.login
import kotlinx.android.synthetic.main.activity_login.register
import kotlinx.android.synthetic.main.activity_login.rememberPass
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.me_layout.*
import org.json.JSONObject


class LoginActivity : BaseActivity(),Function {
    private val idList=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var count_try = 5
        edit_count.setText(count_try.toString())

        //注册按钮的点击事件
        register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val prefs = getPreferences(Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)

        if (isRemember) {
            // 将账号和密码都设置到文本框中
            val account = prefs.getString("account", "")
            val password = prefs.getString("pwd", "")
            input_account.setText(account)
            input_pwd.setText(password)
            rememberPass.isChecked = true
        }
        //登录
        login.setOnClickListener {
            //获取输入框输入的数据
            val account = input_account.text.toString()
            val password = input_pwd.text.toString()
            NetworkRequest.sendRequestWithHttpURLConnection("http://xiaoyuanhaobangshou.com:8080/Yii-min/index.php?r=IoItGuide/Login&username=$account&password=$password",
                object : HttpCallbackListener {
                    override fun onFinish(response: String) {
                        try {
                            val jsonObject = JSONObject(response)
                            val dataJsonList = jsonObject.getJSONObject("data")
                            Log.d("1111111111",dataJsonList.toString())
                            //获取后台返回的数据是成功还是失败
                            val msg = dataJsonList.getString("msg")

                            runOnUiThread(object : Runnable {
                                override fun run() {
                                    val editor = prefs.edit()
                                    if (rememberPass.isChecked) { // 检查复选框是否被选中
                                        editor.putBoolean("remember_password", true)
                                        editor.putString("account", account)
                                        editor.putString("pwd", password)
                                    } else {
                                        editor.clear()
                                    }
                                    editor.apply()

                                    //如果后台返回的数据是 ”登录成功“ 说明用户填写的账号信息正确则可以进入主界面
                                    if (msg == "登录成功") {
                                        user_id = dataJsonList.getString("user_id")
                                        Log.d("user_id",user_id)
                                        Toast.makeText(
                                            this@LoginActivity,
                                            "登录成功！！！",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        BitmapApplication.setMyUserId(user_id)
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                                    //如果后台返回的数据是 ”用户名或者密码错误“ 则告诉用户登录失败
                                    if (msg == "用户名或者密码错误") {
                                        if(count_try>0) {
                                            count_try--
                                            edit_count.setText(count_try.toString())
                                            Toast.makeText(
                                                this@LoginActivity, "用户名或者密码错误", Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        else{
                                            Toast.makeText(this@LoginActivity,"登录机会已用完",Toast.LENGTH_SHORT).show()
                                        }
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
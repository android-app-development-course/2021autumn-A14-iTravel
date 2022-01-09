package com.example.xiaochengshu

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity(){
    var user_id="1"
    class MyBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Toast.makeText(context, "received in MyBroadcastReceiver",
                Toast.LENGTH_SHORT).show()
        }
    }
}
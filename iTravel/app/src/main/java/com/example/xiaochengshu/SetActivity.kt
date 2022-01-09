package com.example.xiaochengshu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_fans.*
import kotlinx.android.synthetic.main.activity_set.*
import kotlinx.android.synthetic.main.activity_set.back_button

class SetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)
        logout.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        back_button.setOnClickListener{
            this.finish()
        }
    }
}
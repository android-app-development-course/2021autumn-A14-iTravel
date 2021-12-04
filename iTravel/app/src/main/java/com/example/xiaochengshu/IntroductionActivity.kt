package com.example.xiaochengshu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_introduction.*

class IntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        back_button.setOnClickListener {
            finish()
        }

        finish.setOnClickListener {
            val intent = Intent()
            setResult(RESULT_OK,intent)
            finish()
        }

        addTrip.setOnClickListener {
            val intent = Intent(this,CreateTripActivity::class.java)
            startActivity(intent)
        }
    }
}
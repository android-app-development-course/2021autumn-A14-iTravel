package com.example.xiaochengshu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_create_introduction.*
import kotlinx.android.synthetic.main.activity_create_introduction.back_button

class CreateIntroductionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_introduction)

        back_button.setOnClickListener {
            finish()
        }

        save.setOnClickListener {
            val intent = Intent(this,IntroductionActivity::class.java)
            startActivityForResult(intent,1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            1->if (resultCode == RESULT_OK){
                finish()
            }
        }
    }
}
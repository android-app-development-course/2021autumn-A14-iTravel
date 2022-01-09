package com.example.xiaochengshu

import android.app.Application
import android.graphics.Bitmap

class BitmapApplication : Application() {//用于在activity间传bitmap
    companion object {
        lateinit var bitmap : Bitmap

        fun setMyBitmap(newbitmap: Bitmap){
            bitmap = newbitmap
        }

        fun getMyBitmap(): Bitmap {
            return bitmap
        }
    lateinit var userId:String
    fun setMyUserId(user_id:String){
        userId=user_id
    }
    fun getMyUserId():String{
        return userId
    }

    var zhengzaidaka_id = 0
    fun setZhengzaidakaId(new_id: Int){
        zhengzaidaka_id = new_id
    }

    fun getZhengzaidakaId(): Int {
        return zhengzaidaka_id
    }
    }
}
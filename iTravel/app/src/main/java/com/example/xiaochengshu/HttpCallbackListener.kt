package com.example.xiaochengshu

interface HttpCallbackListener {
    fun onFinish(response: String)
    fun onError(e: Exception)
}
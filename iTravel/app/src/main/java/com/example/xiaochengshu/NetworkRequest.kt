package com.example.xiaochengshu

import android.graphics.Bitmap
import android.util.Base64.encodeToString
import android.util.Log
import com.bumptech.glide.Glide
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.thread




class NetworkRequest {
    companion object {
        fun sendRequestWithHttpURLConnection(urlString: String, listener: HttpCallbackListener) {
            thread {
                var connection: HttpURLConnection? = null
                try {
                    val url = URL(urlString)
                    val response = StringBuilder()
                    connection = url.openConnection() as HttpURLConnection
                    connection.connectTimeout = 8000
                    connection.readTimeout = 8000
                    connection.requestMethod = "GET"
                    val input = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(input))
                    reader.use {
                        reader.forEachLine {
                            response.append(it)
                        }
                    }
                    Log.d("tttttttttt",response.toString())
                    listener.onFinish(response.toString())
                } catch (e: Exception) {
                    e.printStackTrace()
                    listener.onError(e)
                } finally {
                    connection?.disconnect()
                }
            }
        }

        fun sendRequestWithOkHttp(urlString: String, bitmap: Bitmap, id : Int) {
            thread{
                try {
                    val client = OkHttpClient()

                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                    val bytes: ByteArray = outputStream.toByteArray()
                    val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                    val requestBody = FormBody.Builder().add("r", "IoItGuide/UploadCoverImg").
                        add("guide_id",id.toString()).
                    add("logo", base64.toString()).build()
                    val request = Request.Builder().url(urlString).post(requestBody).build()
                    val response = client.newCall(request).execute()
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        Log.d("qqqqqqqqqqqqq",responseData)
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun sendImageWithOkHttp(urlString: String, bitmap: Bitmap, id : Int) {
            thread{
                try {
                    val client = OkHttpClient()
                    val outputStream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
                    val bytes: ByteArray = outputStream.toByteArray()
                    val base64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                    val requestBody = FormBody.Builder().add("r", "IoItPlace/UploadCoverImg").
                    add("place_id",id.toString()).
                    add("logo", base64.toString()).build()
                    val request = Request.Builder().url(urlString).post(requestBody).build()
                    val response = client.newCall(request).execute()
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        Log.d("qqqqqqqqqqqqq",responseData)
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun uploadBitmap(urlString: String, bitmap: Bitmap,filename: String, listener: HttpCallbackListener) {
//            thread {
//                val lineEnd = "\r\n"
//                val prefix = "__"
//                val boundary = UUID.randomUUID().toString()
//                var connection: HttpURLConnection? = null
//                try {
//                    val url = URL(urlString)
//                    val response = StringBuilder()
//                    connection = url.openConnection() as HttpURLConnection
//                    connection.connectTimeout = 8000
//                    connection.readTimeout = 8000
//                    connection.doInput = true; // 允许输入流
//                    connection.doOutput = true; // 允许输出流
//                    connection.useCaches = false; // 不允许使用缓存
//                    connection.requestMethod = "POST"
//                    connection.setRequestProperty("Connection","Keep-Alive")
//                    connection.setRequestProperty("Charset","UTF-8")
//                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary)
//                    val output = DataOutputStream(connection.outputStream)
////                    output.writeBytes(prefix + boundary + lineEnd)
////                    output.writeBytes("Content-Disposition: form-data; " +
////                            "name=\"file\";filename=\"" +
////                            filename + "\"" + lineEnd)
////                    output.writeBytes(lineEnd)
//////                    val byteArrayOutputStream = ByteArrayOutputStream()
//////                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
//////                    output.write(byteArrayOutputStream.toByteArray(),0,byteArrayOutputStream.toByteArray().size)
//                    output.writeBytes("r=IoItGuide/GetOneByPk&id=1")
////                    output.writeBytes(lineEnd)
////                    output.writeBytes(prefix + boundary + prefix + lineEnd)
//                    output.close()
//                    val input = connection.inputStream
//                    val reader = BufferedReader(InputStreamReader(input))
//                    reader.use {
//                        reader.forEachLine {
//                            response.append(it)
//                        }
//                    }
//                    Log.d("aaaaaaaaaaa",response.toString())
//                    listener.onFinish(response.toString())
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    listener.onError(e)
//                } finally {
//                    connection?.disconnect()
//                }
//            }

        }
    }
}
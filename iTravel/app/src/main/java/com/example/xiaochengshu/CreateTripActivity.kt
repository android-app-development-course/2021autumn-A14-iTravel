package com.example.xiaochengshu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.tencent.tencentmap.mapsdk.maps.MapView
import kotlinx.android.synthetic.main.activity_create_trip.*

import androidx.core.content.FileProvider
import com.tencent.tencentmap.mapsdk.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_create_introduction.*
import kotlinx.android.synthetic.main.activity_create_trip.addImg
import kotlinx.android.synthetic.main.activity_create_trip.back_button
import kotlinx.android.synthetic.main.activity_create_trip.intro_text
import kotlinx.android.synthetic.main.activity_create_trip.save
import java.io.File


class CreateTripActivity : AppCompatActivity() {

    val takePhoto = 3
    val fromAlbum = 4
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    lateinit var location_title : String
    lateinit var location_LatLng : LatLng

    lateinit var bitmap : Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_trip)

        save.setOnClickListener {
            if (!::bitmap.isInitialized)
                Toast.makeText(this,"请选择封面", Toast.LENGTH_SHORT).show()
            else if (!::location_title.isInitialized || !::location_LatLng.isInitialized)
                Toast.makeText(this,"请选择地点", Toast.LENGTH_SHORT).show()
            else if (intro_text.text.isNullOrEmpty())
                Toast.makeText(this,"请输入描述", Toast.LENGTH_SHORT).show()
            else{
                val intent = Intent()
                intent.putExtra("intro",intro_text.text.toString())
                intent.putExtra("location_title",location_title)
                intent.putExtra("location_LatLng",location_LatLng)
                BitmapApplication.setMyBitmap(bitmap)
                setResult(RESULT_OK,intent)
                finish()
            }
        }

        back_button.setOnClickListener {
            finish()
        }

        searchButton.setOnClickListener {
            //SearchPosition.searchPoi(this,positionSearch.text.toString(),mapView.map)
            val intent = Intent(this,SearchPOIActivity::class.java)
            startActivityForResult(intent,1)
        }

        //点击加号选择图片
        addImg.setOnClickListener {
            val intent = Intent(this, addImg_Dialog_Activity::class.java)
            startActivityForResult(intent, 2)
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if (resultCode == RESULT_OK) {
                if (data != null) {
                    location_title = data.getStringExtra("location_title").toString()
                    location_name.text = location_title
                    val location_LatLng_string = data.getStringExtra("location_LatLng").toString()
                    location_LatLng = LatLng( location_LatLng_string.substring(location_LatLng_string.indexOf("latitude")+9,location_LatLng_string.indexOf("longitude")-2).toDouble(),
                        location_LatLng_string.substring(location_LatLng_string.indexOf("longitude")+10,location_LatLng_string.indexOf("altitude")-2).toDouble())
                }
            }
            //2代表是从对话框（选择拍摄或从相册选择）回来的
            2 -> if (resultCode == RESULT_OK) {
                //从对话框返回的字符判断市点击了哪个按钮
                val clicked_btn = data?.getStringExtra("clicked_btn")
                //若点击了“拍摄”按钮
                if (clicked_btn == "takePhotoBtn") {
                    // 创建File对象，用于存储拍照后的图片
                    outputImage = File(externalCacheDir, "output_image.jpg")
                    if (outputImage.exists()) {
                        outputImage.delete()
                    }
                    outputImage.createNewFile()
                    imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        FileProvider.getUriForFile(
                            this,
                            "com.example.cameraalbumtest.fileprovider",
                            outputImage
                        )
                    } else {
                        Uri.fromFile(outputImage)
                    }
                    // 启动相机程序
                    val intent = Intent("android.media.action.IMAGE_CAPTURE")
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent, takePhoto)
                }
                //若点击了“从相册选择”按钮
                else if (clicked_btn == "fromAlbumBtn") {
                    //  打开文件选择器
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intent.addCategory(Intent.CATEGORY_OPENABLE)
                    // 指定只显示图片
                    intent.type = "image/*"
                    startActivityForResult(intent, fromAlbum)
                }
            }
            //从相机程序回来的
            takePhoto -> {
                if (resultCode == Activity.RESULT_OK) {
                    // 将拍摄的照片显示出来
                    bitmap = BitmapFactory.decodeStream(
                        contentResolver.openInputStream(imageUri)
                    )
                    addImg.setImageBitmap(rotateIfRequired(bitmap))

                }
            }
            //从相册（文件选择器）回来的
            fromAlbum -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    data.data?.let { uri ->
                        // 将选择的图片显示
                        val tbitmap = getBitmapFromUri(uri)
                        addImg.setImageBitmap(tbitmap)
                        if (tbitmap != null) {
                            bitmap = tbitmap
                        }
                    }
                }
            }
        }
    }

    private fun getBitmapFromUri(uri: Uri) = contentResolver
        .openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }

    //若需要则旋转图片
    private fun rotateIfRequired(bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(outputImage.path)
        val orientation = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedBitmap = Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height,
            matrix, true
        )
        bitmap.recycle() // 将不再需要的Bitmap对象回收
        return rotatedBitmap
    }
}
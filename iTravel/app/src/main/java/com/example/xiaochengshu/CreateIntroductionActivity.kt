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
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_create_introduction.*
import kotlinx.android.synthetic.main.activity_create_introduction.back_button
import java.io.File

class CreateIntroductionActivity : AppCompatActivity() {
    val takePhoto = 3 //作为后面的requestCode
    val fromAlbum = 4 //作为后面的requestCode
    lateinit var imageUri: Uri
    lateinit var outputImage: File

    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_introduction)

        back_button.setOnClickListener {
            finish()
        }

        save.setOnClickListener {
            if (!::bitmap.isInitialized)
                Toast.makeText(this,"请选择封面",Toast.LENGTH_SHORT).show()
            else if (title_text.text.isNullOrEmpty())
                Toast.makeText(this,"请输入标题",Toast.LENGTH_SHORT).show()
            else if (intro_text.text.isNullOrEmpty())
                Toast.makeText(this,"请输入正文",Toast.LENGTH_SHORT).show()
            else{
                val intent = Intent(this, IntroductionActivity::class.java)
                intent.putExtra("title", title_text.text.toString())
                intent.putExtra("intro", intro_text.text.toString())
                BitmapApplication.setMyBitmap(bitmap)
                startActivityForResult(intent, 1)
            }
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
                finish()
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
package com.my.collector

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.my.collector.bean.ImageSingleton
import java.io.ByteArrayOutputStream
import java.io.IOException

class PopUpActivity : AppCompatActivity() {
    private var bytes: ByteArray? = null
    private var category = ""
    private var imageAvailable = 0
    private var name: String? = null
    private var description: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pop_up)

        val intent = intent
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        window.setLayout((width * .8).toInt(), (height * .6).toInt())
        val upload = findViewById<View>(R.id.upload) as Button
        val capture = findViewById<View>(R.id.capture) as Button
        category =intent.getStringExtra("category") ?: ""
        name = intent.getStringExtra("name")
        description = intent.getStringExtra("description")
        capture.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(intent, 1)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this@PopUpActivity, "Error while trying to take picture", Toast.LENGTH_SHORT).show()
            }
        }
        upload.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            try {
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
                Toast.makeText(this@PopUpActivity, "Error while trying to take picture", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //retrieve captured image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1) {
            val bitmap = data!!.extras!!["data"] as Bitmap?
            bytes = getImageBytes(bitmap)
            if (bytes != null) {
                intentRedirect()
            }
        } else if (resultCode == RESULT_OK && requestCode == 2) {
            val uri = data!!.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                bytes = getImageBytes(bitmap)
                if (bytes != null) {
                    intentRedirect()
                }
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun intentRedirect() {
        imageAvailable = 1
        var instance = ImageSingleton.instance
        instance!!.bytes = bytes!!
        //instance. = ByteArray(0)
        //ImageSingleton.getInstance().setBytes(bytes) //Save image data in a singleton in case it's too big for the intent.
        val intent = Intent(this@PopUpActivity, AddItemsActivity::class.java)
        intent.putExtra("category", category)
        intent.putExtra("imageAvailable", imageAvailable)
        intent.putExtra("name", if (name != null) name else "")
        intent.putExtra("description", if (description != null) description else "")
        startActivity(intent)
    }

    private fun getImageBytes(bitmap: Bitmap?): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}
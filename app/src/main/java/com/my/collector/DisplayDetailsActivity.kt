package com.my.collector

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.my.collector.bean.ImageSingleton

class DisplayDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_details)

        val intent = intent
        val nameTextView = findViewById<View>(R.id.textview_item_name) as TextView
        val descriptionTextView = findViewById<View>(R.id.description_details) as TextView
        val imageView = findViewById<View>(R.id.imageView) as ImageView
        val id = intent.getIntExtra("id", 0)
        nameTextView.text = "Name: " + intent.getStringExtra("name")
        descriptionTextView.text = "Description: " + intent.getStringExtra("description")
        var imageSingleton = ImageSingleton.instance
        val imageBlob: ByteArray = imageSingleton?.bytes ?: ByteArray(0)
        val bitmap = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.size)
        imageView.setImageBitmap(bitmap)
    }
}
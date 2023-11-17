package com.my.collector

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.my.collector.bean.ImageSingleton
import com.my.collector.database.Database
import com.my.collector.database.DatabaseHelper

class AddItemsActivity : AppCompatActivity() {

    private var item_image: ImageView? = null
    private var bytes: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_items)

        val item_name = findViewById<View>(R.id.item_name) as EditText
        val item_description = findViewById<View>(R.id.item_description) as EditText
        item_image = findViewById<View>(R.id.image) as ImageView
        val add = findViewById<View>(R.id.add_item) as Button
        val cancel = findViewById<View>(R.id.cancel) as Button

        val intent = intent
        val category = intent.getStringExtra("category") ?: ""
        val id = intent.getIntExtra("id", 0) // return id or zero if id not found.
        val imageAvailable = intent.getIntExtra("imageAvailable", 0)
        if (imageAvailable == 1) {
            var imageSingleton = ImageSingleton.instance
            bytes = imageSingleton?.bytes ?: ByteArray(0)
            item_description.setText(intent.getStringExtra("description"))
            item_name.setText(intent.getStringExtra("name"))
            item_image!!.setImageResource(R.drawable.image_selected)
            item_image!!.setBackgroundColor(Color.rgb(0, 170, 0))
        } else {
            var instance = ImageSingleton.instance
            instance!!.bytes = ByteArray(0)
            item_image = findViewById<View>(R.id.image) as ImageView
            item_image!!.setImageResource(R.drawable.upload_image)
        }
        item_image!!.setOnClickListener {
            val intent = Intent(this@AddItemsActivity, PopUpActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("category", category)
            intent.putExtra("name", item_name.text.toString())
            intent.putExtra("description", item_description.text.toString())
            startActivity(intent)
        }
        add.setOnClickListener {
            val description = item_description.text.toString()
            val name = item_name.text.toString()
            if (validate(name, description, bytes)) {
                val database = DatabaseHelper(this@AddItemsActivity) //create database instance
                database.insert(name, description, bytes, id)
                database.close()
                bytes?.let { it1 -> Database.addItem(name,description, id, category)}
                val intent = Intent(this@AddItemsActivity, ItemsActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            } else {
                Toast.makeText(this@AddItemsActivity, "One or more fields are empty", Toast.LENGTH_SHORT).show()
            }
        }
        cancel.setOnClickListener {
            val intent = Intent(this@AddItemsActivity, ItemsActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }
    }

    private fun validate(name: String, description: String, image: ByteArray?): Boolean {
        if (name == "") return false
        if (description == "") return false
        return !(image == null || image.size == 0)
    }
}
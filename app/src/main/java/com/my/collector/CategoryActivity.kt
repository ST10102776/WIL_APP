package com.my.collector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.my.collector.database.DatabaseHelper
import android.widget.AdapterView.OnItemClickListener
import com.my.collector.database.Database
import java.util.*

class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val database = DatabaseHelper(this) //create database instance
        val listView = findViewById<View>(R.id.category_list) as ListView
        val myList: MutableList<String> = ArrayList()
        val ids: MutableList<Int> = ArrayList()
        val res = database.select() //get all categories in the database
        val btn = findViewById<Button>(R.id.level_btn)
        btn.text = "Status: " + Database.getStatus()
        while (res.moveToNext()) {
            myList.add(res.getString(1))
            ids.add(res.getInt(0))
        }
        database.close() // close database connection.
        val categories: Array<String> = myList.toTypedArray()
        val adapter = ArrayAdapter(this, R.layout.category_list,
            R.id.name, categories)
        listView.adapter = adapter
        val createCategoryButton = findViewById<View>(R.id.create_category) as Button
        createCategoryButton.setOnClickListener {
            val intent = Intent(this@CategoryActivity, CreateCategoryActivity::class.java)
            startActivity(intent)
        }
        listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@CategoryActivity, ItemsActivity::class.java)
            intent.putExtra("id", ids[i])
            intent.putExtra("category", categories[i])
            startActivity(intent)
        }
    }
}
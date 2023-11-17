package com.my.collector

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.my.collector.bean.CategoryBean
import com.my.collector.bean.ImageSingleton
import com.my.collector.bean.ItemBean
import com.my.collector.database.DatabaseHelper
import android.widget.AdapterView.OnItemClickListener
import java.util.*

class ItemsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        val listView = findViewById<View>(R.id.item_list) as ListView
        val database = DatabaseHelper(this) //create database instance
        val intent = intent
        var bean: CategoryBean? = null
        val category = intent.getStringExtra("category")
        val id = intent.getIntExtra("id", 0) // return id or zero if id not found.
        val res = database.selectSingleItem(id) //get a single category
        while (res.moveToNext()) {
            val max_items = res.getInt(2)
            val categoryName = res.getString(1)
            val categoryId = res.getInt(0)
            bean = CategoryBean(categoryId, categoryName, max_items)
        }
        val iRes = database.select(id)
        val itemNames: MutableList<String> = ArrayList()
        val itemList: MutableList<ItemBean> = ArrayList()
        while (iRes.moveToNext()) {
            itemNames.add(iRes.getString(1))
            itemList.add(
                ItemBean(iRes.getInt(0), iRes.getString(1),
                iRes.getString(2), iRes.getBlob(3))
            )
        }
        val adapter = ArrayAdapter(this, R.layout.category_list, R.id.name, itemNames)
        listView.adapter = adapter
        val addItemButton = findViewById<View>(R.id.add_item) as Button
//        if (itemNames.size >= bean!!.maxItems) {
//            addItemButton.isEnabled = false
//            addItemButton.setBackgroundColor(Color.rgb(236, 236, 236))
//        }
        addItemButton.setOnClickListener {
            val intent = Intent(this@ItemsActivity, AddItemsActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("category", category)
            startActivity(intent)
        }
        listView.onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@ItemsActivity, DisplayDetailsActivity::class.java)
            intent.putExtra("id", id)
            val item = itemList[i]
            intent.putExtra("name", item.name)
            intent.putExtra("description", item.description)
            var instance = ImageSingleton.instance
            instance!!.bytes = item.image
            startActivity(intent)
        }
    }
}
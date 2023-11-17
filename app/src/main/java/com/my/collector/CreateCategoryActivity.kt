package com.my.collector

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.my.collector.database.Database
import com.my.collector.database.DatabaseHelper

class CreateCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_category)

        val name = findViewById<View>(R.id.category_name) as EditText
        val number = findViewById<View>(R.id.number_of_items) as EditText
        val add = findViewById<View>(R.id.add_category) as Button
        val cancel = findViewById<View>(R.id.cancel) as Button
        val database = DatabaseHelper(this)
        add.setOnClickListener { /* retrieve data and save it into the datababe: Table categories_table */
            val categoryName = name.text.toString()
            val numberOfItems: Int = number.text.toString().toInt()
            val success = database.insert(categoryName, numberOfItems)
            if (!success) {
                Toast.makeText(this@CreateCategoryActivity, "Failed to insert",
                    Toast.LENGTH_SHORT).show()
            } else {
                //Write to realtime database
                Database.addCategory(categoryName,numberOfItems)
                /* only redirect to CategoryActivity if sucessful */
                val intent = Intent(this@CreateCategoryActivity,
                    CategoryActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        cancel.setOnClickListener {
            val intent = Intent(this@CreateCategoryActivity,
                CategoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
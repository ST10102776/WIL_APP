package com.my.collector

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.my.collector.database.Database
import com.my.collector.login.LoginHandler

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get image by ID and set resource
        val imageView = findViewById<ImageView>(R.id.app_logo)
        imageView.setImageResource(R.drawable.app_logo)

        //Get login button, username and password by ID
        val loginButton = findViewById<View>(R.id.login) as Button
        val username = findViewById<View>(R.id.username) as EditText
        val password = findViewById<View>(R.id.password) as EditText

        //Check and ask for permission for Camera and external storage
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.CAMERA), 3)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE), 4)

//        Database.initCategories()

        //handling button click
        loginButton.setOnClickListener {
            val handler = LoginHandler()
            val success = handler.validateCredentials(username.text.toString(),
                password.text.toString())
            if (success) {
                //Create intent to change to the next activity
                val intent = Intent(this@LoginActivity, CategoryActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@LoginActivity, "Incorrect username or password",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}
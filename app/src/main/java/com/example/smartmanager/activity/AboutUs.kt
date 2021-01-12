package com.example.smartmanager.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.smartmanager.R


class AboutUs : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        title = "SmartManager 1.0"
        val backBtn = findViewById<Button>(R.id.button_back)
        backBtn.setOnClickListener {
            finish()
        }

        val formBtn = findViewById<Button>(R.id.button_form)
        formBtn.setOnClickListener {
            val uri: Uri =
                Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScW_V2qlr5c__f_Urgesr-lNVNa2oaa8_o2b3lrG6iRM8YhYg/viewform?usp=sf_link")

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

    }

}
package com.example.etoffice.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.etoffice.R

class MyPagePlaceSetting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_place_setting)
    }

    override fun onBackPressed() {
        finish()
    }
}
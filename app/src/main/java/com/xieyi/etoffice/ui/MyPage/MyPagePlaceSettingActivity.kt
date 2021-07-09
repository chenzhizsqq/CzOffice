package com.xieyi.etoffice.ui.MyPage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xieyi.etoffice.R

class MyPagePlaceSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_place_setting)
    }

    override fun onBackPressed() {
        finish()
    }
}
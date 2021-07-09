package com.xieyi.etoffice.ui.MyPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xieyi.etoffice.R

class MyPageChangeCompanyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_change_company)
    }
    override fun onBackPressed() {
        finish()
    }
}
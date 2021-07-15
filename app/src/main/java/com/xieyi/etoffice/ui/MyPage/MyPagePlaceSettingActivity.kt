package com.xieyi.etoffice.ui.MyPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xieyi.etoffice.R
//
//class MyPagePlaceSettingActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_my_page_place_setting)
//    }
//
//    override fun onBackPressed() {
//        finish()
//    }
//}

class MyPagePlaceSettingActivity : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.activity_my_page_place_setting, container, false)
        return root
    }
}
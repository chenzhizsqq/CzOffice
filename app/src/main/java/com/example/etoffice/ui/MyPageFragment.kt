package com.example.etoffice.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.etoffice.R


class MyPageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_my_page, container, false)
        var testTV:TextView= root.findViewById(R.id.user_name)
        testTV.text = "my page ok"

        val pImageView: ImageView = root.findViewById(R.id.imageView_MyPage_Logo) as ImageView
        pImageView.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, MyPagePlaceSettingActivity::class.java)
            startActivity(intent)
        })
        return root
    }



}
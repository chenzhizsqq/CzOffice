package com.xieyi.etoffice.ui.report

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.xieyi.etoffice.R


class ReportDetail : AppCompatActivity(), View.OnClickListener {
    lateinit var buttonImageButton1 : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)
        buttonImageButton1 = findViewById(R.id.imageButton1)
        buttonImageButton1.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imageButton1 -> ReportAddDialog().show(supportFragmentManager, "1'")
        }
    }
}

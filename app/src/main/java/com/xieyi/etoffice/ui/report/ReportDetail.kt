package com.xieyi.etoffice.ui.report

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.EtOfficeGetUserStatus
import java.lang.Exception


class ReportDetail : AppCompatActivity(), View.OnClickListener {
    val TAG = "ReportDetail"
    lateinit var buttonImageButton1 : ImageView
    lateinit var mLinearLayout : LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_detail)
        buttonImageButton1 = findViewById(R.id.imageButton1)
        buttonImageButton1.setOnClickListener(this)

        //report_id
        mLinearLayout = findViewById(R.id.report_id)
        val textWidth:Int = 120

        try {
            Log.e(TAG,"count:"+EtOfficeGetUserStatus.infoUserStatusListCount())


            for (i in EtOfficeGetUserStatus.infoUserStatusList()){
                Log.e(TAG,"index:$i")

                var tLinearLayout = LinearLayout(this)
                tLinearLayout.orientation = LinearLayout.VERTICAL
                tLinearLayout.gravity = Gravity.CENTER
                tLinearLayout.setPadding(10)



                var _text = TextView(this)
                _text.setBackgroundColor(Color.parseColor("#FFBCBCBC") )
                _text.width = textWidth
                _text.height = 100
                _text.text = Tools.srcContent(i.statustime,8)
                tLinearLayout.addView(_text)

                var _text2 = TextView(this)
                _text2.setBackgroundColor(Color.YELLOW)
                _text2.width = textWidth
                _text.height = 100
                _text2.text = "勤務中"
                tLinearLayout.addView(_text2)


                mLinearLayout.addView(tLinearLayout)
            }
        }catch (e:Exception){
            Log.e(TAG,e.toString())
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.imageButton1 -> ReportAddDialog().show(supportFragmentManager, "1'")
        }
    }
}

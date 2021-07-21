package com.xieyi.etoffice.ui.report


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC


class ReportDetailFragment : Fragment() {

    val TAG = "ReportDetail"
    lateinit var buttonImageButton1: ImageView
    lateinit var mLinearLayout: LinearLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_detail, container, false)
        buttonImageButton1 = view.findViewById(R.id.imageButton1)
        buttonImageButton1.setOnClickListener {

            val pReportAddDialog = ReportAddDialog()
            pReportAddDialog.setTargetFragment(this, 1)
            fragmentManager?.let { it1 -> pReportAddDialog.show(it1, "ReportAddDialog") }
        }

        //report_id
        mLinearLayout = view.findViewById(R.id.report_id)
        val textWidth: Int = 120

        try {
            Log.e(TAG, "count:" + JC.pEtOfficeGetUserStatus.infoUserStatusListCount())


            for (i in JC.pEtOfficeGetUserStatus.infoUserStatusList()) {
                Log.e(TAG, "index:$i")

                val tLinearLayout = LinearLayout(activity)
                tLinearLayout.orientation = LinearLayout.VERTICAL
                tLinearLayout.gravity = Gravity.CENTER
                tLinearLayout.setPadding(10)


                val _text = TextView(activity)
                _text.setBackgroundColor(Color.parseColor("#FFBCBCBC") )
                _text.width = textWidth
                _text.height = 100
                _text.text = Tools.srcContent(i.statustime,8)
                tLinearLayout.addView(_text)

                val _text2 = TextView(activity)
                _text2.setBackgroundColor(Color.YELLOW)
                _text2.width = textWidth
                _text.height = 100
                _text2.text = "勤務中"
                tLinearLayout.addView(_text2)


                mLinearLayout.addView(tLinearLayout)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return view
    }


}
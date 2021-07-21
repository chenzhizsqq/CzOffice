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


class ReportDetailFragment() : Fragment() {

    val TAG = "ReportDetail"
    lateinit var buttonImageButton1: ImageView
    lateinit var mLinearLayout: LinearLayout

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    //検索の日付
    var date:String=""

/*
    {
        "status": 0,
        "result": {
        "authflag": "",
        "planworktime": "",
        "worktime": "通常勤務 19:02-",
        "planworklist": [],
        "workstatuslist": [
        {
            "time": "19:02",
            "status": "会議中"
        },
        {
            "time": "19:02",
            "status": "移動中"
        }
        ],
        "reportlist": [],
        "commentlist": []
    },
        "message": ""
    }

 */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: begin")

        val bundle = arguments
        date = bundle!!.getString("date").toString()
        //データ更新
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_detail, container, false)


        Log.e(TAG, "JC.pEtOfficeGetReportInfo:"+JC.pEtOfficeGetReportInfo.lastJson )


        //検索の日付
        val record_date:TextView = view.findViewById(R.id.record_date)
        record_date.text = Tools.allDate(date)

        //予定
        val appointment:TextView = view.findViewById(R.id.appointment)
        appointment.text = JC.pEtOfficeGetReportInfo.infoJson().result.planworktime


        //実績：
        val worktime:TextView = view.findViewById(R.id.worktime)
        worktime.text = JC.pEtOfficeGetReportInfo.infoJson().result.worktime




        val size= JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist.size

//        val quotient = size / 5
//        for (j in 0 until quotient) {

            //content：
            val content:LinearLayout = view.findViewById(R.id.content)

            val remainder = size % 5
            for (i in 0 until remainder) {
                val getLinearLayout_1 = getLinearLayout()


                val time = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist[i].time

                val getTextView_time = getTextView(time)
                getLinearLayout_1.addView(getTextView_time)


                val text = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist[i].status

                val getTextView_1 = getTextView(text)
                getLinearLayout_1.addView(getTextView_1)



                content.addView(getLinearLayout_1)

            }
//        }


        //设计的代码
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
        //design code
        return view
    }



    private fun getLinearLayout(): LinearLayout {
        val r=LinearLayout(activity)

        val ll = LinearLayout.LayoutParams(0, MATCH_PARENT,1.0F)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.setBackgroundResource(R.drawable.ic_round_edge_white)

        r.orientation = LinearLayout.VERTICAL

        return r
    }

    private fun getImageView(): ImageView {
        val r=ImageView(activity)

        val ll = LinearLayout.LayoutParams( MATCH_PARENT,WRAP_CONTENT)
        r.layoutParams = ll


        r.setImageResource(R.drawable.ic_dashboard_black_24dp)
        return r
    }

    private fun getTextView(text:String): TextView {
        val r=TextView(activity)

        val ll = LinearLayout.LayoutParams( WRAP_CONTENT,WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.text=text

        r.setTextColor(Color.BLACK)

        return r
    }

}
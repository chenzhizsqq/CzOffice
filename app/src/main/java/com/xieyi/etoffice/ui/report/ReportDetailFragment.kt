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
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*


class ReportDetailFragment() : Fragment() {

    val TAG = "ReportDetailFragment"
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
        //Log.e(TAG, "onCreate: begin")

        val bundle = arguments
        date = bundle!!.getString("date").toString()

    }

    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_report_detail, container, false)


        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {

                    val r = JC.pEtOfficeGetReportInfo.post(date)

                }catch (e:Exception){
                    Log.e(TAG, "pEtOfficeGetReportInfo.post() :$e")

                }

                try {
                    doOnUiCode()
                }catch (e:Exception){
                    Log.e(TAG, "doOnUiCode :$e")

                }
            }
        }

        return mainView
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }


    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            Log.e(TAG, "JC.pEtOfficeGetReportInfo:" + JC.pEtOfficeGetReportInfo.lastJson)


            //検索の日付
            val record_date: TextView = mainView.findViewById(R.id.record_date)
            record_date.text = Tools.allDate(date)

            //予定
            val appointment: TextView = mainView.findViewById(R.id.appointment)
            appointment.text = JC.pEtOfficeGetReportInfo.infoJson().result.planworktime


            //実績：
            val worktime: TextView = mainView.findViewById(R.id.worktime)
            worktime.text = JC.pEtOfficeGetReportInfo.infoJson().result.worktime



            //content：
            val content: LinearLayout = mainView.findViewById(R.id.content)


            val sizeEachY=5

            funContent(sizeEachY, content)


            val addView: ImageView = mainView.findViewById(R.id.addView)
            addView.setOnClickListener {

                val pReportAddDialog = ReportAddDialog()

                val fragmentManager = this@ReportDetailFragment.parentFragmentManager
                fragmentManager.let { it1 -> pReportAddDialog.show(it1, "pReportAddDialog") }

            }


            //returnpHome
            val returnHome = mainView.findViewById<ImageView>(R.id.returnHome)
            returnHome.setOnClickListener {
                Navigation.findNavController(mainView)
                    .navigate(R.id.report_fragment);

            }


            //设计的代码
            //        buttonImageButton1 = view.findViewById(R.id.imageButton1)
            //        buttonImageButton1.setOnClickListener {
            //
            //            val pReportAddDialog = ReportAddDialog()
            //            pReportAddDialog.setTargetFragment(this, 1)
            //            fragmentManager?.let { it1 -> pReportAddDialog.show(it1, "ReportAddDialog") }
            //        }

            //report_id
            //        mLinearLayout = view.findViewById(R.id.report_id)
            //        val textWidth: Int = 120
            //
            //        try {
            //            Log.e(TAG, "count:" + JC.pEtOfficeGetUserStatus.infoUserStatusListCount())
            //
            //
            //            for (i in JC.pEtOfficeGetUserStatus.infoUserStatusList()) {
            //                Log.e(TAG, "index:$i")
            //
            //                val tLinearLayout = LinearLayout(activity)
            //                tLinearLayout.orientation = LinearLayout.VERTICAL
            //                tLinearLayout.gravity = Gravity.CENTER
            //                tLinearLayout.setPadding(10)
            //
            //
            //                val _text = TextView(activity)
            //                _text.setBackgroundColor(Color.parseColor("#FFBCBCBC") )
            //                _text.width = textWidth
            //                _text.height = 100
            //                _text.text = Tools.srcContent(i.statustime,8)
            //                tLinearLayout.addView(_text)
            //
            //                val _text2 = TextView(activity)
            //                _text2.setBackgroundColor(Color.YELLOW)
            //                _text2.width = textWidth
            //                _text.height = 100
            //                _text2.text = "勤務中"
            //                tLinearLayout.addView(_text2)
            //
            //
            //                mLinearLayout.addView(tLinearLayout)
            //            }
            //        } catch (e: Exception) {
            //            Log.e(TAG, e.toString())
            //        }
            //design code
        }
    }

    private fun funContent(sizeEachY: Int, content: LinearLayout) {
        val size = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist.size
        Log.e(TAG, "doOnUiCode: size:$size")
        val l_Y: Array<LinearLayout> = Array(size / sizeEachY + 1) { getLinearLayoutContent() }
        var x = 0
        var y = 0
        for (i in 0 until size) {
            x = i % sizeEachY
            y = i / sizeEachY

            val l_X = getLinearLayout()

            val time = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist[i].time

            val getTextView_time = getTextView(time)
            getTextView_time.setBackgroundColor(Color.parseColor("#E8E8E8"))
            l_X.addView(getTextView_time)


            val text = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist[i].status

            val getTextView_1 = getTextView(text)
            getTextView_1.setBackgroundColor(Color.YELLOW)
            l_X.addView(getTextView_1)


            l_Y[y].addView(l_X)

            if (x == sizeEachY - 1) {
                content.addView(l_Y[y])
            }
        }
        content.addView(l_Y[y])
    }


    private fun getLinearLayoutContent(): LinearLayout {
        val r=LinearLayout(activity)

        val ll = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.orientation = LinearLayout.HORIZONTAL

        return r
    }


    private fun getLinearLayout(): LinearLayout {
        val r=LinearLayout(activity)

        val ll = LinearLayout.LayoutParams(0, MATCH_PARENT,1.0F)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.setBackgroundResource(R.drawable.ic_round_edge_white)

        r.setPadding(20)

        r.orientation = LinearLayout.VERTICAL

        return r
    }

    private fun getTextView(text:String): TextView {
        val r=TextView(activity)

        val ll = LinearLayout.LayoutParams( WRAP_CONTENT,WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.text=text

        r.width = 200

        r.height = 100

        r.setTextColor(Color.BLACK)

        return r
    }

}
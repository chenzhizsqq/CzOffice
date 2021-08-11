package com.xieyi.etoffice.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.*
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val TAG = "HomeFragment"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        mainView = inflater.inflate(R.layout.fragment_home, container, false)

        val textCompanyTitle: TextView = mainView.findViewById(R.id.text_company_title)
        homeViewModel.companyTitle.observe(viewLifecycleOwner, Observer {
            textCompanyTitle.text = it
        })

        val textDate: TextView = mainView.findViewById(R.id.text_time)
        homeViewModel.date.observe(viewLifecycleOwner, Observer {
            textDate.text = it
        })

        val ll_inWork: LinearLayout = mainView.findViewById(R.id.in_work)
        ll_inWork.setOnClickListener {
//            Tools.showMsg(mainView,"勤務中")
//            tv_state.text = "勤務中"

            showStatusDialog("1","勤務中")
        }

        val ll_outWork: LinearLayout = mainView.findViewById(R.id.out_work)
        ll_outWork.setOnClickListener {
//            Tools.showMsg(mainView,"勤務外")
//            tv_state.text = "勤務外"

            showStatusDialog("2","勤務外")
        }

        val ll_sleep: LinearLayout = mainView.findViewById(R.id.sleep)
        ll_sleep.setOnClickListener {
//            Tools.showMsg(mainView,"休憩中")
//            tv_state.text = "休憩中"

            showStatusDialog("3","休憩中")
        }

        val ll_moving: LinearLayout = mainView.findViewById(R.id.moving)
        ll_moving.setOnClickListener {
//            Tools.showMsg(mainView,"移動中")
//            tv_state.text = "移動中"

            showStatusDialog("4","移動中")
        }

        val ll_meeting: LinearLayout = mainView.findViewById(R.id.meeting)
        ll_meeting.setOnClickListener {
//            Tools.showMsg(mainView,"会議中")
//            tv_state.text = "会議中"

            showStatusDialog("5","会議中")
        }



        //出勤記録を表示します
        val recordTableTableLayout: LinearLayout =
            mainView.findViewById(R.id.state_layout) as LinearLayout
        recordTableTableLayout.setOnClickListener {


            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@HomeFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }


        }


        //ページを更新
        refreshPage()

        return mainView
    }

    //ページを更新
    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {


                //出勤記録 データ更新
                try {
                    val r: String = JC.pEtOfficeGetUserStatus.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetUserStatus.post() :$r")


                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetUserStatus", e)

                }


                //今状態 データ更新
                doOnUiCode_NowStatus()


                //GetStatusList
                try {
                    val r: String = JC.pEtOfficeGetStatusList.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetStatusList.post() :$r")
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetStatusList.post()", e)
                }
                doOnUiCode_GetStatusList()


                //Message データ更新
                try {
                    val r: String = JC.pEtOfficeGetMessage.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetMessage.post() :$r")


                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetMessage.post()", e)

                }
                doOnUiCode_Message()
            }
        }
    }

    private fun showStatusDialog(statusvalue:String,statustext:String) {
        val mHomeStatusDialog = HomeStatusDialog(statusvalue,statustext)

        val fragmentManager = this@HomeFragment.parentFragmentManager
        fragmentManager.let { it1 -> mHomeStatusDialog.show(it1, "mHomeStatusDialog") }

        mHomeStatusDialog.setOnDialogListener(object : HomeStatusDialog.OnDialogListener{
            override fun onClick(userLocation: String, memo: String) {
                Log.e(TAG, "onDialogClick: userLocation:$userLocation memo:$memo", )

                refreshPage()
            }
        })
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }



    // GetStatusList UI更新
    private suspend fun doOnUiCode_GetStatusList() {
        withContext(Dispatchers.Main) {
            val state_layout = mainView.findViewById<LinearLayout>(R.id.state_layout)
            state_layout.removeAllViews()

            //表示量
            val srcSize = 2

            //今表示量
            var srcNum = 0

            val size = JC.pEtOfficeGetStatusList.infoJson().result.recordlist.size

            Log.e(TAG, "recordlist.size: $size")



            for (i in 0 until size) {
                val time = JC.pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustime
                val timeSrc = Tools.allDateTime(time)
                val status = JC.pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustext
                val memo = JC.pEtOfficeGetStatusList.infoJson().result.recordlist[i].memo


                val textView = TextView(activity)
                textView.text = "・$timeSrc $status $memo"
                textView.setPadding(5)


                state_layout.addView(textView)

                if(++srcNum >= srcSize){
                    break
                }
            }
        }
    }


    // 今状態 state 更新
    private suspend fun doOnUiCode_NowStatus() {
        withContext(Dispatchers.Main) {

            if(JC.pEtOfficeGetUserStatus.infoJson().result.userstatuslist.size>0)
            {
                Log.e(TAG, "doOnUiCode_NowStatus: size>0", )

                val tvState = mainView.findViewById<TextView>(R.id.state)
                val statustext = JC.pEtOfficeGetUserStatus.infoJson().result.userstatuslist[0].statustext

                Log.e(TAG, "doOnUiCode_NowStatus: statustext:$statustext", )
                tvState.text = statustext
            }

        }
    }

    // Message UI更新
    private suspend fun doOnUiCode_Message() {
        withContext(Dispatchers.Main) {


            val size = JC.pEtOfficeGetMessage.infoJson().result.messagelist.size
            Log.e(TAG, "messagelist.size: $size")
            val messageLayout = mainView.findViewById<LinearLayout>(R.id.message_layout)
            messageLayout.removeAllViews()


            for (i in 0..size - 1) {


                val title = JC.pEtOfficeGetMessage.infoJson().result.messagelist[i].title
                val updatetime = JC.pEtOfficeGetMessage.infoJson().result.messagelist[i].updatetime
                val content = JC.pEtOfficeGetMessage.infoJson().result.messagelist[i].content

                val eachLine = eachLine(title,Tools.allDateTime(updatetime),content)

                val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )

                layoutParams.setMargins(10, 10, 10, 0)

                messageLayout.addView(eachLine, layoutParams)
            }


        }
    }

    private fun eachLine(s1:String,s2:String,s3:String): LinearLayout {
        val eachLine = LinearLayout(activity)

        eachLine.orientation = LinearLayout.VERTICAL

        eachLine.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        eachLine.setBackgroundResource(R.drawable.ic_round_edge_white)



        val l1 = topLine(s1,s2)

        eachLine.addView(l1)

        l1.setPadding(20,0,20,0)

        val t3 = TextView(activity)

        t3.text = s3

        t3.setPadding(20)

        eachLine.addView(t3)


        return eachLine
    }

    private fun topLine(s1:String,s2:String): LinearLayout {
        val l1 = LinearLayout(activity)

        l1.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        val t1 = TextView(activity)

        t1.text = s1

        t1.textSize = 20.0F

        t1.setTextColor(Color.BLACK)

        l1.addView(t1)


        val t2 = TextView(activity)

        t2.text = s2

        t2.textSize = 16.0F

        t2.setTextColor(Color.BLACK)

        t2.gravity = Gravity.RIGHT

        t2.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

        l1.addView(t2)

        return l1
    }
}
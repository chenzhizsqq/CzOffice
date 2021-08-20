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
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.databinding.FragmentHomeBinding
import com.xieyi.etoffice.jsonData.*
import kotlinx.coroutines.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val TAG = javaClass.simpleName

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    private lateinit var pEtOfficeGetUserStatus : EtOfficeGetUserStatus
    private lateinit var pEtOfficeGetStatusList : EtOfficeGetStatusList
    private lateinit var pEtOfficeGetMessage : EtOfficeGetMessage
    private lateinit var mAdapter: GetMessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pEtOfficeGetUserStatus = EtOfficeGetUserStatus()
        pEtOfficeGetStatusList = EtOfficeGetStatusList()
        pEtOfficeGetMessage = EtOfficeGetMessage()
    }

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)


        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val textCompanyTitle: TextView = binding.textCompanyTitle
        homeViewModel.companyTitle.observe(viewLifecycleOwner, Observer {
            textCompanyTitle.text = it
        })

        val textDate: TextView = binding.textTime
        homeViewModel.date.observe(viewLifecycleOwner, Observer {
            textDate.text = it
        })

        val ll_inWork: LinearLayout = binding.inWork
        ll_inWork.setOnClickListener {
//            Tools.showMsg(mainView,"勤務中")
//            tv_state.text = "勤務中"

            showStatusDialog("1","勤務中")
        }

        val ll_outWork: LinearLayout = binding.outWork
        ll_outWork.setOnClickListener {
//            Tools.showMsg(mainView,"勤務外")
//            tv_state.text = "勤務外"

            showStatusDialog("2","勤務外")
        }

        val ll_sleep: LinearLayout = binding.sleep
        ll_sleep.setOnClickListener {
//            Tools.showMsg(mainView,"休憩中")
//            tv_state.text = "休憩中"

            showStatusDialog("3","休憩中")
        }

        val ll_moving: LinearLayout = binding.moving
        ll_moving.setOnClickListener {
//            Tools.showMsg(mainView,"移動中")
//            tv_state.text = "移動中"

            showStatusDialog("4","移動中")
        }

        val ll_meeting: LinearLayout = binding.meeting
        ll_meeting.setOnClickListener {
//            Tools.showMsg(mainView,"会議中")
//            tv_state.text = "会議中"

            showStatusDialog("5","会議中")
        }


        //出勤記録を表示します
        val recordTableTableLayout: LinearLayout =
            binding.stateLayout
        recordTableTableLayout.setOnClickListener {


            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@HomeFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }


        }


        //ページを更新
        refreshPage()

        return binding.root
    }

    //ページを更新
    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {


                //出勤記録 データ更新
                try {
                    val r: String = pEtOfficeGetUserStatus.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetUserStatus.post() :$r")

                    if (r=="0"){
                        //今状態 データ更新
                        doOnUiCode_NowStatus()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetUserStatus", e)

                }




                //GetStatusList
                try {
                    val r: String = pEtOfficeGetStatusList.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetStatusList.post() :$r")
                    if (r=="0"){
                        doOnUiCode_GetStatusList()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetStatusList.post()", e)
                }


                //Message データ更新
                try {
                    val r: String = pEtOfficeGetMessage.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetMessage.post() :$r")


                    if(r=="0"){
                        doOnUiCode_Message()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetMessage.post()", e)

                }
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
            val state_layout = binding.stateLayout
            state_layout.removeAllViews()

            //表示量
            val srcSize = 2

            //今表示量
            var srcNum = 0

            val size = pEtOfficeGetStatusList.infoJson().result.recordlist.size

            Log.e(TAG, "recordlist.size: $size")



            for (i in 0 until size) {
                val time = pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustime
                val timeSrc = Tools.allDateTime(time)
                val status = pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustext
                val memo = pEtOfficeGetStatusList.infoJson().result.recordlist[i].memo


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

            if(pEtOfficeGetUserStatus.infoJson().result.userstatuslist.size>0)
            {
                Log.e(TAG, "doOnUiCode_NowStatus: size>0", )

                val tvState = binding.state
                val statustext = pEtOfficeGetUserStatus.infoJson().result.userstatuslist[0].statustext

                Log.e(TAG, "doOnUiCode_NowStatus: statustext:$statustext", )
                tvState.text = statustext
            }

        }
    }

    // Message UI更新
    private suspend fun doOnUiCode_Message() {
        withContext(Dispatchers.Main) {
            Log.e(TAG, "doOnUiCode_Message: begin", )

            mAdapter=GetMessageAdapter(pEtOfficeGetMessage.infoJson().result.messagelist)
            binding.recyclerMessage.adapter = mAdapter

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
package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.util.Log
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

    private val TAG = "Frag01SelectFragment"

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

        val textTitle1: TextView = mainView.findViewById(R.id.text_title1)
        homeViewModel.title1.observe(viewLifecycleOwner, Observer {
            textTitle1.text = it
        })


        val textTitle2: TextView = mainView.findViewById(R.id.text_title2)
        homeViewModel.title2.observe(viewLifecycleOwner, Observer {
            textTitle2.text = it
        })

        val textTitle3: TextView = mainView.findViewById(R.id.text_title3)
        homeViewModel.title3.observe(viewLifecycleOwner, Observer {
            textTitle3.text = it
        })

        val textTitle4: TextView = mainView.findViewById(R.id.text_title4)
        homeViewModel.title4.observe(viewLifecycleOwner, Observer {
            textTitle4.text = it
        })

        //出勤記録を表示します
        val recordTableTableLayout: LinearLayout =
            mainView.findViewById(R.id.record_layout) as LinearLayout
        recordTableTableLayout.setOnClickListener {


            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@HomeFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }


        }

        //出勤ステータスを表示します
        val mStatusLinearLayout: LinearLayout =
            mainView.findViewById(R.id.status_linearLayout) as LinearLayout
        mStatusLinearLayout.setOnClickListener {

            val mHomeStatusDialog = HomeStatusDialog()

            val fragmentManager = this@HomeFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeStatusDialog.show(it1, "mHomeStatusDialog")  }

        }

        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r: String = JC.pEtOfficeGetStatusList.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetStatusList.post() :$r")


                }catch (e:Exception){
                    Log.e(TAG, "pEtOfficeGetStatusList.post() :$e")

                }

                doOnUiCode_GetStatus()


                //データ更新
                try {
                    val r: String = JC.pEtOfficeGetMessage.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetMessage.post() :$r")


                }catch (e:Exception){
                    Log.e(TAG, "pEtOfficeGetMessage.post() :$e")

                }
                doOnUiCode_M()
            }
        }

        return mainView
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }


//            {
//                "status": 0,
//                "result": {
//                "recordlist": [
//                {
//                    "statustime": "20210727102010",
//                    "statusvalue": "3",
//                    "statustext": "休憩中",
//                    "memo": ""
//                },
//                {
//                    "statustime": "20210727101943",
//                    "statusvalue": "1",
//                    "statustext": "勤務中",
//                    "memo": ""
//                }
//                ]
//            },
//                "message": ""
//            }
    // GetStatus UI更新
    private suspend fun doOnUiCode_GetStatus() {
    withContext(Dispatchers.Main) {

        val size = JC.pEtOfficeGetStatusList.infoJson().result.recordlist.size
        Log.e(TAG, "recordlist.size: $size")


        val recordLayout = mainView.findViewById<LinearLayout>(R.id.record_layout)
        for (i in 0..size - 1) {

            val time = JC.pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustime
            val statustext = JC.pEtOfficeGetStatusList.infoJson().result.recordlist[i].statustext

            val textView = TextView(activity)
            textView.text = "・" + Tools.allDateTime(time) + " " +statustext

            textView.setPadding(5)

            recordLayout.addView(textView)

        }
    }
}

    // GetStatus UI更新
    private suspend fun doOnUiCode_M() {
        withContext(Dispatchers.Main) {


        }
    }
}
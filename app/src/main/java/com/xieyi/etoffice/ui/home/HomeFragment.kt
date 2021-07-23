package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val TAG = "Frag01SelectFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val textTitle1: TextView = view.findViewById(R.id.text_title1)
        homeViewModel.title1.observe(viewLifecycleOwner, Observer {
            textTitle1.text = it
        })


        val textTitle2: TextView = view.findViewById(R.id.text_title2)
        homeViewModel.title2.observe(viewLifecycleOwner, Observer {
            textTitle2.text = it
        })

        val textTitle3: TextView = view.findViewById(R.id.text_title3)
        homeViewModel.title3.observe(viewLifecycleOwner, Observer {
            textTitle3.text = it
        })

        val textTitle4: TextView = view.findViewById(R.id.text_title4)
        homeViewModel.title4.observe(viewLifecycleOwner, Observer {
            textTitle4.text = it
        })

        //出勤記録を表示します
        val recordTableTableLayout: TableLayout =
            view.findViewById(R.id.record_table) as TableLayout
        recordTableTableLayout.setOnClickListener {

            //データ更新
//            Thread {
//                try {
//                    var r: String = ""
//                    r = JC.pEtOfficeGetUserStatus.post()
                    //Log.e(TAG, "pEtOfficeGetUserStatus.post() :$r")

                    val mHomeReportDialog = HomeReportDialog()
//                    mHomeReportDialog.setTargetFragment(this@HomeFragment, 1)
//                    fragmentManager?.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog") }

                    val fragmentManager = this@HomeFragment.parentFragmentManager
                    fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }

//                }catch (e:Exception){
//
//
//                }
//            }.start()


        }

        //出勤ステータスを表示します
        val mStatusLinearLayout: LinearLayout =
            view.findViewById(R.id.status_linearLayout) as LinearLayout
        mStatusLinearLayout.setOnClickListener {

            val mHomeStatusDialog = HomeStatusDialog()

//            mHomeStatusDialog.setTargetFragment(this@HomeFragment, 1)
//            fragmentManager?.let { it1 -> mHomeStatusDialog.show(it1, "mHomeStatusDialog") }

            val fragmentManager = this@HomeFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeStatusDialog.show(it1, "mHomeStatusDialog")  }

        }


        //Log.d(TAG, "ok")
        return view
    }

}
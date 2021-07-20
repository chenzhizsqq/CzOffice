package com.xieyi.etoffice.ui.report

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableRow
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC

class ReportFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: begin", )

        //データ更新
        Thread {
            try {

                val r = JC.pEtOfficeGetReportList.post()
                Log.e(TAG, "pEtOfficeGetReportList.post() :$r")


            }catch (e:Exception){
                Log.e(TAG, "pEtOfficeGetReportList.post() :$e")

            }
        }.start()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scrolling_report, container, false)


        //report_info_title
        val pTableRowInfoTitle: TableRow = view.findViewById(R.id.report_info_title_1) as TableRow
        pTableRowInfoTitle.setOnClickListener(View.OnClickListener {
//            val intent = Intent(activity, ReportDetail::class.java)
//            startActivity(intent)

            Navigation.findNavController(view).navigate(R.id.ReportDetail);        //就是用这句去转了
        })

        return view
    }

    companion object {
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }
}
package com.example.etoffice.ui.report

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.fragment.app.Fragment
import com.example.etoffice.R
import com.example.etoffice.ui.MyPage.MyPagePlaceSettingActivity

class ReportFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_scrolling_report, container, false)


        //report_info_title
        val pTableRowInfoTitle: TableRow = root.findViewById(R.id.report_info_title_1) as TableRow
        pTableRowInfoTitle.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, ReportDetail::class.java)
            startActivity(intent)
        })

        return root
    }

    companion object {
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }
}
package com.xieyi.etoffice.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R

class ReportFragment : Fragment() {

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
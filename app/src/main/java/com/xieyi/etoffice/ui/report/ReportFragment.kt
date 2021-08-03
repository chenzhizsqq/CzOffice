package com.xieyi.etoffice.ui.report

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*

class ReportFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.e(TAG, "onCreate: begin")

    }
    private lateinit var mainView: View

    private var bTouch: Boolean = false

    private lateinit var recordLinearLayout: LinearLayout

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        mainView = inflater.inflate(R.layout.fragment_scrolling_report, container, false)

        topMenu()

        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {

                    //日報一覧取得
                    val r = JC.pEtOfficeGetReportList.post()
                    Log.e(TAG, "pEtOfficeGetReportList.post() :$r")


                }catch (e:Exception){
                    Log.e(TAG, "pEtOfficeGetReportList.post() :$e")

                }

                doOnUiCode()
            }
        }


        //demo
//        val pTableRowInfoTitle: TableRow = view.findViewById(R.id.report_info_title_1) as TableRow
//        pTableRowInfoTitle.setOnClickListener(View.OnClickListener {
//
//            Navigation.findNavController(view).navigate(R.id.ReportDetail);        //就是用这句去转了
//        })

        return mainView
    }


    private fun topMenu() {
        val tv_allSelect: TextView = mainView.findViewById(R.id.all_select)
        tv_allSelect.visibility = View.INVISIBLE

        val tv_commit: TextView = mainView.findViewById(R.id.commit)
        tv_commit.visibility = View.INVISIBLE

        val iv_people: ImageView = mainView.findViewById(R.id.people)
        iv_people.visibility = View.INVISIBLE


        val tv_edit: TextView = mainView.findViewById(R.id.edit)
        tv_edit.setOnClickListener(View.OnClickListener {

            try {

                bTouch = !bTouch
                if(bTouch){

                    tv_allSelect.visibility = View.VISIBLE
                    tv_commit.visibility = View.VISIBLE
                    iv_people.visibility = View.VISIBLE

                }else{

                    tv_allSelect.visibility = View.INVISIBLE
                    tv_commit.visibility = View.INVISIBLE
                    iv_people.visibility = View.INVISIBLE

                }

            } catch (e: Exception) {
                Log.e(TAG, "tv_edit.setOnClickListener",e)

            }
        })
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            recordLinearLayout = mainView.findViewById(R.id.record_linearLayout)


            //Log.e(TAG, "JC.pEtOfficeGetReportList:"+JC.pEtOfficeGetReportList.lastJson )

            val groupSum = JC.pEtOfficeGetReportList.infoJson().result.group.size

            for (j in 0..groupSum - 1) {


                val yyyy = Tools.dateGetYear(JC.pEtOfficeGetReportList.infoJson().result.group[j].month)
                val mm = Tools.dateGetMonth(JC.pEtOfficeGetReportList.infoJson().result.group[j].month)

                val yyyymmTextView = makeTextView("$yyyy.$mm")

                yyyymmTextView.setPadding(20)
                yyyymmTextView.height = 100
                yyyymmTextView.gravity = (Gravity.CENTER or Gravity.LEFT)

                recordLinearLayout.addView(yyyymmTextView)

                val size = JC.pEtOfficeGetReportList.infoJson().result.group[j].reportlist.size

                for (i in 0..size - 1) {

                    val mLinearLayout = LinearLayout(activity)

                    mLinearLayout.orientation = LinearLayout.VERTICAL

                    mLinearLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

                    val checkBox = makeCheckBox(j, i)
                    mLinearLayout.addView(checkBox)

                    //up

                    val m1 = LinearLayout(activity)
                    m1.orientation = LinearLayout.HORIZONTAL
                    m1.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

                    val yyyymmdd =
                        JC.pEtOfficeGetReportList.infoJson().result.group[j].reportlist[i].yyyymmdd
                    //Log.e(TAG, "JC.pEtOfficeGetReportList yyyymmdd:"+yyyymmdd )
                    val y_m_d = Tools.allDate(yyyymmdd)

                    val TV_up = makeTextView("$y_m_d  ")
                    m1.addView(TV_up)

                    //承認状況
                    val approval =
                        JC.pEtOfficeGetReportList.infoJson().result.group[j].reportlist[i].approval
                    if (approval.isEmpty()) {
                        val TV_2 = makeButton("未承認")
                        m1.addView(TV_2)
                    }




                    mLinearLayout.addView(m1)


                    //down
                    val TV_down =
                        makeTextView(JC.pEtOfficeGetReportList.infoJson().result.group[j].reportlist[i].title)
                    mLinearLayout.addView(TV_down)

                    //content
                    val content =
                        makeTextView(JC.pEtOfficeGetReportList.infoJson().result.group[j].reportlist[i].content)
                    mLinearLayout.addView(content)


                    //setting
                    mLinearLayout.setBackgroundColor(Color.WHITE)
                    mLinearLayout.setPadding(30)


                    //setOnClickListener
                    mLinearLayout.setOnClickListener(View.OnClickListener {

                        try {

                            val bundle = Bundle()
                            bundle.putString("date", yyyymmdd)

                            Navigation.findNavController(mainView)
                                .navigate(R.id.ReportDetailFragment, bundle);        //ReportDetail

                        } catch (e: Exception) {
                            Log.e(TAG, "pEtOfficeGetReportInfo.post() :$e")

                        }
                    })


                    //recordLinearLayout end
                    recordLinearLayout.addView(mLinearLayout)

                    //線

                    val mLinearLayout2 = linearLayout_line()
                    recordLinearLayout.addView(mLinearLayout2)


                }

            }
        }
    }

    private fun makeCheckBox(j: Int, i: Int): CheckBox {
        val checkBox = CheckBox(activity)
        val mCheckBoxTag = checkBoxTag(j, i)
        checkBox.tag = mCheckBoxTag
        return checkBox
    }

    private fun checkBoxTag(j: Int,i: Int): String {
        return "radioButton_id_j_" + j + "_i_" + i
    }

    private fun linearLayout_line(): LinearLayout {
        val mLinearLayout2 = LinearLayout(activity)
        val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
        mLinearLayout2.layoutParams = lp2
        mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
        return mLinearLayout2
    }

    private fun makeTextView(ym: String): TextView {
        val tv = TextView(activity)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        tv.setTextColor(Color.parseColor("#000000"))
        tv.text = ym
        return tv
    }

    private fun makeButton(ym: String): TextView {
        val tv = TextView(activity)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        tv.setTextColor(Color.parseColor("#FFFFFF"))

        val lp= LinearLayout.LayoutParams(150,70)
        lp.setMargins(1, 1, 1, 1)
        tv.layoutParams = lp

        tv.setBackgroundResource(R.drawable.ic_round_edge_red)
        tv.text = ym

        tv.gravity = Gravity.CENTER
        return tv
    }

    companion object {
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }
}
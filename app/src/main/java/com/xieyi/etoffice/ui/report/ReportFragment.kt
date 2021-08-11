package com.xieyi.etoffice.ui.report

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC
import com.xieyi.etoffice.ui.home.HomeReportDialog
import kotlinx.coroutines.*
import java.util.*


class ReportFragment : Fragment() {

    private val TAG = javaClass.simpleName

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.e(TAG, "onCreate: begin")

    }
    private lateinit var mainView: View

    private var bVISIBLE: Boolean = false

    private var bAllCheck: Boolean = false

    private lateinit var recordLinearLayout: LinearLayout


    inner class checkTagYmd{
        var tag:String = ""
        var ymd:String = ""

    }
    private val arrayListTagYmd = ArrayList<checkTagYmd>()




    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.fragment_scrolling_report, container, false)

        topMenu()

        ContentUpdate()

        return mainView
    }

    private fun ContentUpdate() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {

                    //日報一覧取得
                    val r = JC.pEtOfficeGetReportList.post()
                    Log.e(TAG, "pEtOfficeGetReportList.post() :$r")

                    doOnUiCode()

                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetReportList.post()",e)

                }

            }
        }
    }


    private fun topMenu() {
        val tv_allSelect: TextView = mainView.findViewById(R.id.all_select)
        tv_allSelect.visibility = View.INVISIBLE



        val tv_commit: TextView = mainView.findViewById(R.id.commit)
        tv_commit.visibility = View.INVISIBLE

        val iv_people: ImageView = mainView.findViewById(R.id.people)
        iv_people.visibility = View.INVISIBLE
        //出勤記録を表示します
        iv_people.setOnClickListener {
            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@ReportFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }
        }


        val tv_edit: TextView = mainView.findViewById(R.id.edit)
        tv_edit.setOnClickListener(View.OnClickListener {
            tvEditSrcChange(tv_allSelect, tv_commit, iv_people)
        })


        //allSelect click
        tv_allSelect.setOnClickListener {
            if(bVISIBLE){
                try {

                    bAllCheck=!bAllCheck
                    for (tagYmd in arrayListTagYmd) {

                        val checkBox: CheckBox = mainView.findViewWithTag(tagYmd.tag) as CheckBox
                        checkBox.isChecked = bAllCheck

                    }
                }catch (e: Exception) {
                    Log.e(TAG, "tv_allSelect.setOnClickListener",e)
                }
            }
        }
        
        //commit click
        tv_commit.setOnClickListener {
            commitAlertDialog(tv_allSelect, tv_commit, iv_people)
            //commitClick(tv_allSelect, tv_commit, iv_people)

        }
    }

    private fun commitClick(
        tv_allSelect: TextView,
        tv_commit: TextView,
        iv_people: ImageView
    ) {
        if (bVISIBLE) {
            try {
                GlobalScope.launch(errorHandler) {
                    withContext(Dispatchers.IO) {
                        //指定された　発信
                        val ymdArray = ArrayList<String>()
                        for (tagYmd in arrayListTagYmd) {
                            val checkBox: CheckBox =
                                mainView.findViewWithTag(tagYmd.tag) as CheckBox
                            if (checkBox.isChecked) {

                                ymdArray.add(tagYmd.ymd)
                            }
                        }
                        var r: String = "-1"
                        r = JC.pEtOfficeSetApprovalJsk.post(ymdArray)
                        Log.e(TAG, "topMenu: r:$r")


                        //データ更新
                        try {

                            //日報一覧取得
                            val r = JC.pEtOfficeGetReportList.post()
                            Log.e(TAG, "pEtOfficeGetReportList.post() :$r")


                            doOnUiCode()
                        } catch (e: Exception) {
                            Log.e(TAG, "pEtOfficeGetReportList.post()",e)

                        }
                        tvEditSrcChange(tv_allSelect, tv_commit, iv_people)
                    }

                }

            } catch (e: Exception) {
                Log.e(TAG, "tv_commit.setOnClickListener", e)
            }
        }
    }

    //tv_Edit 表示　変更
    private fun tvEditSrcChange(
        tv_allSelect: TextView,
        tv_commit: TextView,
        iv_people: ImageView
    ) {
        try {


            bVISIBLE = !bVISIBLE
            if (bVISIBLE) {

                tv_allSelect.visibility = View.VISIBLE
                tv_commit.visibility = View.VISIBLE
                iv_people.visibility = View.VISIBLE

                for (tagYmd in arrayListTagYmd) {

                    val checkBox: CheckBox = mainView.findViewWithTag(tagYmd.tag) as CheckBox
                    checkBox.visibility = View.VISIBLE

                }


            } else {

                tv_allSelect.visibility = View.INVISIBLE
                tv_commit.visibility = View.INVISIBLE
                iv_people.visibility = View.INVISIBLE

                for (tagYmd in arrayListTagYmd) {

                    val checkBox: CheckBox = mainView.findViewWithTag(tagYmd.tag) as CheckBox
                    checkBox.visibility = View.GONE

                }

            }

        } catch (e: Exception) {
            Log.e(TAG, "tv_edit.setOnClickListener", e)

        }
    }


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            recordLinearLayout = mainView.findViewById(R.id.record_linearLayout)
            recordLinearLayout.removeAllViews()
            arrayListTagYmd.clear()
            bAllCheck = false

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
                    //each Line
                    val ll_eachLine = LinearLayout(activity)
                    ll_eachLine.orientation = LinearLayout.HORIZONTAL
                    ll_eachLine.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                    ll_eachLine.setBackgroundColor(Color.WHITE)
                    ll_eachLine.gravity = Gravity.CENTER


                    //checkBox
                    val checkBoxTag = checkBoxTag(j, i)
                    val checkBox = makeCheckBox(checkBoxTag)
                    checkBox.visibility = View.GONE
                    //arrayListTag.add(checkBoxTag)
                    ll_eachLine.addView(checkBox)


                    //message
                    val m_ll_Message = ll_Message(j, i)
                    ll_eachLine.addView(m_ll_Message)



                    recordLinearLayout.addView(ll_eachLine)

                    //線

                    val mLinearLayout2 = linearLayout_line()
                    recordLinearLayout.addView(mLinearLayout2)


                }

            }
        }
    }

    private fun ll_Message(j: Int, i: Int): LinearLayout {
        val mLinearLayout = LinearLayout(activity)

        mLinearLayout.orientation = LinearLayout.VERTICAL

        mLinearLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


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
        //Log.e(TAG, "ll_Message: approval:$approval" )
        if (approval.isEmpty()) {
            val TV_2 = makeButton("未承認")
            m1.addView(TV_2)
        }else{
            val TV_2 = makeButtonBlue("承認済み")
            m1.addView(TV_2)

            val tvApproval = makeTextView(JC.pEtOfficeGetReportList.infoJson().result.group[j].reportlist[i].approval)
            tvApproval.setPadding(10)
            m1.addView(tvApproval)
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
        mLinearLayout.setPadding(30)

        //TagYmd list
        val tagYmd = checkTagYmd()
        tagYmd.tag=checkBoxTag(j, i)
        tagYmd.ymd=yyyymmdd
        arrayListTagYmd.add(tagYmd)


        //setOnClickListener
        mLinearLayout.setOnClickListener(View.OnClickListener {

            try {

                val bundle = Bundle()
                bundle.putString("date", yyyymmdd)

                Navigation.findNavController(mainView)
                    .navigate(R.id.ReportDetailFragment, bundle);        //ReportDetail

            } catch (e: Exception) {
                Log.e(TAG, "pEtOfficeGetReportInfo.post()",e)

            }
        })
        return mLinearLayout
    }

    private fun makeCheckBox(tag:String): CheckBox {
        val checkBox = CheckBox(activity)
        checkBox.tag = tag
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

    private fun makeButton(s: String): TextView {
        val tv = TextView(activity)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        tv.setTextColor(Color.parseColor("#FFFFFF"))

        val lp= LinearLayout.LayoutParams(150,70)
        lp.setMargins(1, 1, 1, 1)
        tv.layoutParams = lp

        tv.setBackgroundResource(R.drawable.ic_round_edge_red)
        tv.text = s

        tv.gravity = Gravity.CENTER
        return tv
    }

    private fun makeButtonBlue(s: String): TextView {
        val tv = TextView(activity)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        tv.setTextColor(Color.parseColor("#FFFFFF"))

        val lp= LinearLayout.LayoutParams(200,70)
        lp.setMargins(1, 1, 1, 1)
        tv.layoutParams = lp

        tv.setBackgroundResource(R.drawable.ic_round_edge_blue)
        tv.text = s

        tv.gravity = Gravity.CENTER
        return tv
    }

    private fun commitAlertDialog(
        tv_allSelect: TextView,
        tv_commit: TextView,
        iv_people: ImageView){

        AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
            .setTitle("消息")
            .setMessage("現在選択されている情報を承認しますか？")
            .setPositiveButton("确定") { _, which ->
                commitClick(tv_allSelect, tv_commit, iv_people)
            }
            .setNegativeButton("取消") { _, which ->
            }
            .show()
    }

    companion object {
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }
}
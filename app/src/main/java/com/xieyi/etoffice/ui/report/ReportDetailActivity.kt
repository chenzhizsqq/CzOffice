package com.xieyi.etoffice.ui.report


import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.ReportResult
import com.xieyi.etoffice.databinding.ActivityReportDetailBinding
import kotlinx.coroutines.*
import java.util.*


class ReportDetailActivity : BaseActivity(), DatePickerDialog.OnDateSetListener {

    val TAG = "ReportDetailActivity"

    //検索の日付
    var date: String = ""

    //”承認”の状態
    var isApproved:Boolean = false

    private lateinit var mPlanworklistAdapter: PlanworklistAdapter

    private lateinit var mWorkstatuslistAdapter: WorkstatuslistAdapter

    private lateinit var mReportListAdapter: ReportListAdapter

    private lateinit var mCommentListAdapter: CommentListAdapter

    private lateinit var binding: ActivityReportDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

        EtOfficeGetReportInfoPost(date)

    }

    private fun init() {
        mPlanworklistAdapter = PlanworklistAdapter()
        mPlanworklistAdapter.notifyDataSetChanged(ArrayList())
        binding.recyclerViewPlanworklist.adapter = mPlanworklistAdapter

        mWorkstatuslistAdapter = WorkstatuslistAdapter()
        mWorkstatuslistAdapter.notifyDataSetChanged(ArrayList())
        binding.recyclerViewWorkstatuslist.adapter = mWorkstatuslistAdapter

        mReportListAdapter = ReportListAdapter()
        mReportListAdapter.notifyDataSetChanged(ArrayList())
        binding.recyclerViewReportlist.adapter = mReportListAdapter

        mCommentListAdapter = CommentListAdapter()
        mCommentListAdapter.notifyDataSetChanged(ArrayList())
        binding.recyclerViewCommentlist.adapter = mCommentListAdapter
        binding.recyclerViewCommentlist.isNestedScrollingEnabled = false

        val intent = intent
        date = intent.getStringExtra("ReportFragmentMessage").toString()

        //”承認”の状態を確認
        isApproved = intent.getBooleanExtra("isApproved",false)
        if (isApproved){
            binding.isApproved.visibility = View.GONE
        }
        intent.removeExtra("isApproved")



        binding.recyclerViewPlanworklist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        binding.recyclerViewWorkstatuslist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        binding.recyclerViewReportlist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )


        //ReportDetailFragment open
        binding.addView.setOnClickListener {

            val fm: FragmentManager = supportFragmentManager
    //            val dialog: ReportAddDialog =
    //                ReportAddDialog.newInstance()
    //            dialog.show(fm, "ReportAddDialog")
            //ReportAddDialog.actionStart(fm, date)
            val bundle = Bundle()
            bundle.putString("reportDate", date)
            val reportAddDialog = ReportAddDialog()
            reportAddDialog.arguments = bundle
            reportAddDialog.show(fm, "ReportAddDialog")
            reportAddDialog.setOnDialogListener(object : ReportAddDialog.OnDialogListener {
                override fun onClick() {
                    EtOfficeGetReportInfoPost(date)
                }
            })
        }


        //returnpHome
        binding.returnHome.setOnClickListener {
            val intent = Intent(this@ReportDetailActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }



        binding.messageSend.setOnClickListener {

            hideKeyboard(binding.messageEdit)

            //EtOfficeSetComment
            //データ更新
            when {
                binding.messageEdit.text.length > 150 -> {
                    Tools.showErrorDialog(this, getString(R.string.MSG17))
                }
                binding.messageEdit.text.trim().isEmpty() -> {
                    //Tools.showErrorDialog(this, getString(R.string.no_text))
                }
                else -> {
                    EtOfficeSetCommentPost(
                        date,
                        binding.messageEdit.text.toString()
                    )
                    binding.messageEdit.text.clear()
                }
            }
        }

        //record_date
        binding.recordDate.setOnClickListener {
            val year: Int = date.substring(0, 4).toInt()
            val month: Int = date.substring(4, 6).toInt()
            val day: Int = date.substring(6, 8).toInt()
            val newFragment = DatePick(year, month, day)
            newFragment.show(supportFragmentManager, "datePicker")
        }

        //出勤記録を表示します
//        binding.people.setOnClickListener {
//            val mHomeReportDialog = HomeReportDialog()
//
//            val fragmentManager = this@ReportDetailActivity.supportFragmentManager
//            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog") }
//        }

        //メンバーページに切り替えます
        binding.people.setOnClickListener {
            Tools.sharedPrePut(Config.FragKey, 2)
            val intent = Intent(this@ReportDetailActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun EtOfficeGetReportInfoPost(ymd: String) {
        Api.EtOfficeGetReportInfo(
            context = this@ReportDetailActivity,
            ymd = ymd,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetReportInfoResult(model.result)

                            EtOfficePlanworklistResult(model.result)
                            EtOfficeGetStatusListResult(model.result)
                            EtOfficeGetReportlistResult(model.result)
                            EtOfficeCommentlistResult(model.result)
                        }
                        else -> {
                            Tools.showErrorDialog(
                                this,
                                model.message)
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data")
                }
            }
        )
    }


    private fun EtOfficePlanworklistResult(result: ReportResult) {
        mPlanworklistAdapter.notifyDataSetChanged(result.planworklist)

    }


    private fun EtOfficeGetStatusListResult(result: ReportResult) {
        mWorkstatuslistAdapter.notifyDataSetChanged(result.workstatuslist)

    }

    private fun EtOfficeGetReportlistResult(result: ReportResult) {
        mReportListAdapter.notifyDataSetChanged(result.reportlist)

    }

    private fun EtOfficeCommentlistResult(result: ReportResult) {
        mCommentListAdapter.notifyDataSetChanged(result.commentlist)
    }

    private fun EtOfficeSetCommentPost(ymd: String, comment: String) {

        Api.EtOfficeSetComment(
            context = this@ReportDetailActivity,
            ymd = ymd,
            comment = comment,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetReportInfoPost(date)
                        }
                        else -> {
                            Tools.showErrorDialog(
                                this,
                                model.message)
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data")
                }
            }
        )
    }


    private fun EtOfficeGetReportInfoResult(result: ReportResult) {

        //検索の日付
        binding.recordDate.text = Tools.allDate(date)

        //予定
        binding.appointment.text = result.planworktime

        //実績：
        binding.worktime.text = result.worktime

    }

    private fun hideKeyboard(v: View) {
        val imm: InputMethodManager =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        val selectDate = getString(R.string.dateformat, year, monthOfYear + 1, dayOfMonth)
        date = selectDate
        EtOfficeGetReportInfoPost(date)
    }

}
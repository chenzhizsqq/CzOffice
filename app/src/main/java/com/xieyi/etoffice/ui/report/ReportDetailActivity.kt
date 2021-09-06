package com.xieyi.etoffice.ui.report


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
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.ReportResult
import com.xieyi.etoffice.databinding.ActivityReportDetailBinding
import kotlinx.coroutines.*


class ReportDetailActivity : BaseActivity() {

    val TAG = javaClass.simpleName


    //検索の日付
    var date: String = ""

    private lateinit var mPlanworklistAdapter: PlanworklistAdapter

    private lateinit var mWorkstatuslistAdapter: WorkstatuslistAdapter

    private lateinit var mReportListAdapter: ReportListAdapter

    private lateinit var mCommentListAdapter: CommentListAdapter

    private lateinit var binding: ActivityReportDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        date = intent.getStringExtra("ReportFragmentMessage").toString()



        binding.recyclerViewPlanworklist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        binding.recyclerViewWorkstatuslist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )

        binding.recyclerViewReportlist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL, false
        )


        EtOfficeGetReportInfoPost(date)


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
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
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
        mPlanworklistAdapter = PlanworklistAdapter(result.planworklist)
        binding.recyclerViewPlanworklist.adapter = mPlanworklistAdapter

    }


    private fun EtOfficeGetStatusListResult(result: ReportResult) {
        mWorkstatuslistAdapter = WorkstatuslistAdapter(result.workstatuslist)
        binding.recyclerViewWorkstatuslist.adapter = mWorkstatuslistAdapter

    }

    private fun EtOfficeGetReportlistResult(result: ReportResult) {
        mReportListAdapter = ReportListAdapter(result.reportlist)
        binding.recyclerViewReportlist.adapter = mReportListAdapter

    }

    private fun EtOfficeCommentlistResult(result: ReportResult) {
        mCommentListAdapter = CommentListAdapter(result.commentlist)
        binding.recyclerViewCommentlist.adapter = mCommentListAdapter

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
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
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


        //ReportDetailFragment open
        binding.addView.setOnClickListener {

            val fm: FragmentManager = supportFragmentManager
//            val dialog: ReportAddDialog =
//                ReportAddDialog.newInstance()
//            dialog.show(fm, "ReportAddDialog")
            ReportAddDialog.actionStart(fm, date)
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
            EtOfficeSetCommentPost(
                date,
                binding.messageEdit.text.toString()
            )


            binding.messageEdit.text.clear()
        }
    }

    private fun hideKeyboard(v: View) {
        val imm: InputMethodManager =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.applicationWindowToken, 0)
        }
    }

}
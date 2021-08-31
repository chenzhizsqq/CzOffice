package com.xieyi.etoffice.ui.report


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.ReportResult
import com.xieyi.etoffice.databinding.ActivityReportDetailBinding
import kotlinx.coroutines.*


class ReportDetailActivity() : BaseActivity() {

    val TAG = javaClass.simpleName
    lateinit var buttonImageButton1: ImageView

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    //検索の日付
    var date:String=""

    private lateinit var mWorkstatuslistAdapter: WorkstatuslistAdapter

    //ReportListAdapter
    private lateinit var mReportListAdapter: ReportListAdapter

    private lateinit var mCommentListAdapter: CommentListAdapter

    private lateinit var binding: ActivityReportDetailBinding
/*
    {
        "status": 0,
        "result": {
        "authflag": "",
        "planworktime": "",
        "worktime": "通常勤務 19:02-",
        "planworklist": [],
        "workstatuslist": [
        {
            "time": "19:02",
            "status": "会議中"
        },
        {
            "time": "19:02",
            "status": "移動中"
        }
        ],
        "reportlist": [],
        "commentlist": []
    },
        "message": ""
    }
 */



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        date = intent.getStringExtra("ReportFragmentMessage").toString()



        binding.recyclerViewWorkstatuslist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL
            , false)

        binding.recyclerViewReportlist.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.HORIZONTAL
            , false)


        EtOfficeGetReportInfoPost(date)



    }


    private fun EtOfficeGetReportInfoPost(ymd : String) {
        Api.EtOfficeGetReportInfo(
            context = this@ReportDetailActivity,
            ymd = ymd,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetReportInfoResult(model.result)

                            EtOfficeGetStatusListResult(model.result)
                            EtOfficeGetReportlistResult(model.result)
                            EtOfficeCommentlistResult(model.result)
                        }
                        1 -> {
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
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
                    Log.e(TAG, "onFailure:$data");
                }
            }
        )
    }


    private fun EtOfficeGetStatusListResult(result: ReportResult) {
        mWorkstatuslistAdapter= WorkstatuslistAdapter(result.workstatuslist)
        binding.recyclerViewWorkstatuslist.adapter = mWorkstatuslistAdapter

    }

    private fun EtOfficeGetReportlistResult(result: ReportResult) {
        mReportListAdapter= ReportListAdapter(result.reportlist)
        binding.recyclerViewReportlist.adapter = mReportListAdapter

    }

    private fun EtOfficeCommentlistResult(result: ReportResult) {
        mCommentListAdapter= CommentListAdapter(result.commentlist)
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
                        1 -> {
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
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
                    Log.e(TAG, "onFailure:$data");
                }
            }
        )
    }



    private fun EtOfficeGetReportInfoResult(result: ReportResult) {


        //検索の日付
        binding.recordDate.text = Tools.allDate(date)

        //予定
        binding.appointment.text = result.planworktime

        //planworklist
        planworklistFun(result)

        //reportlist
        //reportlistFun(result)


        //実績：
        binding.worktime.text = result.worktime


        //workstatuslist：
        //workstatuslistfun(5,result)

        //commentlist
        //commentlistFun(result)


        //ReportDetailFragment open
        binding.addView.setOnClickListener {

            val fm: FragmentManager = supportFragmentManager
            val dialog: ReportAddDialog =
                ReportAddDialog.newInstance()
            dialog.show(fm, "ReportAddDialog")

        }


        //returnpHome
        binding.returnHome.setOnClickListener {
            val intent: Intent = Intent(this@ReportDetailActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }



        binding.messageSend.setOnClickListener {

            hideKeyboard(binding.messageEdit)

            //EtOfficeSetComment
            //データ更新
            EtOfficeSetCommentPost(date,binding.messageEdit.text.toString())


            binding.messageEdit.text.clear()
        }
    }


    private fun planworklistFun(result: ReportResult) {
        val planworklist: LinearLayout = binding.planworklist
        planworklist.removeAllViews()
/*
        {
            "project": "[2021XY07]EtOfficeAPP開発#1",
            "wbs": "[W01]工程A(進捗:%)",
            "date": "2021/07/01-2021/07/31",
            "time": "(計画：160h)"
        }
 */
        val listSize = result.planworklist.size

        for (i in 0 until listSize) {
            val ll=ll_planworklist()

            val t1 =getTextView2(result.planworklist[i].project)
            t1.setTextColor(Color.parseColor("#000000"))
            t1.textSize = 20F

            val t2 =getTextView2(result.planworklist[i].wbs)

            val v3=result.planworklist[i].date+" "+
                    result.planworklist[i].time
            val t3 =getTextView2(v3)

            ll.addView(t1)
            ll.addView(t2)
            ll.addView(t3)

            planworklist.addView(ll)
        }


    }



    //planworklist LinearLayout
    private fun ll_planworklist(): LinearLayout {
        val r=LinearLayout(applicationContext)

        val ll = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        r.layoutParams = ll

        r.orientation = LinearLayout.VERTICAL

        return r
    }


    private fun getLinearLayout(): LinearLayout {
        val r=LinearLayout(applicationContext)

        val ll = LinearLayout.LayoutParams(0, MATCH_PARENT,1.0F)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.setBackgroundResource(R.drawable.ic_round_edge_white)

        r.setPadding(20)

        r.orientation = LinearLayout.VERTICAL

        return r
    }


    private fun getTextView2(text:String): TextView {
        val r=TextView(applicationContext)

        val ll = LinearLayout.LayoutParams( WRAP_CONTENT,WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.text=text


        r.setTextColor(Color.BLACK)

        return r
    }

    private fun hideKeyboard(v: View) {
        val imm: InputMethodManager =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0)
        }
    }

}
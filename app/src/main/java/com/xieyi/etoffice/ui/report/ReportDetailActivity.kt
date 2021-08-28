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
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.databinding.ActivityReportDetailBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetReportInfo
import kotlinx.coroutines.*


class ReportDetailActivity() : BaseActivity() {

    val TAG = javaClass.simpleName
    lateinit var buttonImageButton1: ImageView

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    //検索の日付
    var date:String=""

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



    private fun EtOfficeGetReportInfoResult(result: EtOfficeGetReportInfo.Result) {


        //検索の日付
        val record_date: TextView = binding.recordDate
        record_date.text = Tools.allDate(date)

        //予定
        val appointment: TextView = binding.appointment
        appointment.text = result.planworktime

        //planworklist
        planworklistFun(result)

        //reportlist
        reportlistFun(result)


        //実績：
        val worktime: TextView = binding.worktime
        worktime.text = result.worktime


        //workstatuslist：
        workstatuslistfun(5,result)

        //commentlist
        commentlistFun(result)


        //ReportDetailFragment open
        val addView: ImageView = binding.addView
        addView.setOnClickListener {

            val fm: FragmentManager = supportFragmentManager
            val dialog: ReportAddDialog =
                ReportAddDialog.newInstance()
            dialog.show(fm, "ReportAddDialog")

        }


        //returnpHome
        val returnHome = binding.returnHome
        returnHome.setOnClickListener {
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


    private fun reportlistFun(result: EtOfficeGetReportInfo.Result) {
        val reportlist: LinearLayout = binding.reportlist
        reportlist.removeAllViews()


        val listSize = result.reportlist.size

        for (i in 0 until listSize) {
            val ll=ll_planworklist()

            val t1 =getTextView2("プロジェクト："+result.reportlist[i].project)
            t1.setTextColor(Color.parseColor("#000000"))
            t1.textSize = 20F

            val t2 =getTextView2("作業コード："+result.reportlist[i].wbs)

            val t3 =getTextView2("工数："+result.reportlist[i].time)

            val t4 =getTextView2("報告："+result.reportlist[i].memo)


            ll.addView(t1)
            ll.addView(t2)
            ll.addView(t3)
            ll.addView(t4)

            ll.setPadding(10)

            ll.setBackgroundResource(R.drawable.ic_round_edge_grey)

            val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )

            layoutParams.setMargins(10, 5, 10, 5)

            reportlist.addView(ll,layoutParams)
        }


    }


    private fun planworklistFun(result: EtOfficeGetReportInfo.Result) {
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



    private fun workstatuslistfun(sizeEachY:Int, result: EtOfficeGetReportInfo.Result) {
        val content: LinearLayout = binding.content
        content.removeAllViews()


        val size = result.workstatuslist.size
        Log.e(TAG, "doOnUiCode: size:$size")
        val l_Y: Array<LinearLayout> = Array(size / sizeEachY + 1) { getLinearLayoutContent() }
        var x = 0
        var y = 0
        for (i in 0 until size) {
            x = i % sizeEachY
            y = i / sizeEachY

            val l_X = getLinearLayout()

            val time = result.workstatuslist[i].time

            val getTextView_time = getTextView(time)
            getTextView_time.setBackgroundColor(Color.parseColor("#E8E8E8"))
            l_X.addView(getTextView_time)


            val text = result.workstatuslist[i].status

            val getTextView_1 = getTextView(text)
            getTextView_1.setBackgroundColor(Color.YELLOW)
            l_X.addView(getTextView_1)


            l_Y[y].addView(l_X)

            if (x == sizeEachY - 1) {
                content.addView(l_Y[y])
            }
        }
    }


    private fun commentlistFun(result: EtOfficeGetReportInfo.Result) {
        val commentlist: LinearLayout = binding.commentlist
        commentlist.removeAllViews()

        val listSize = result.commentlist.size

        for (i in 0 until listSize) {
            val ll = ll_planworklist()

            val t1 =
                getTextView2("username：" + result.commentlist[i].comment)
            t1.setTextColor(Color.parseColor("#000000"))
            t1.textSize = 20F

            val username = result.commentlist[i].username
            val time = result.commentlist[i].time

            val t2_text = username + " " + Tools.allDateTime(time)

            val t2 = getTextView2(t2_text)



            ll.addView(t1)
            ll.addView(t2)
            ll.setPadding(10)

            val layoutParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )

            commentlist.addView(ll, layoutParams)

            commentlist.addView(linearLayout_line())
        }

    }


    private fun getLinearLayoutContent(): LinearLayout {
        val r=LinearLayout(applicationContext)

        val ll = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.orientation = LinearLayout.HORIZONTAL

        return r
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

    private fun getTextView(text:String): TextView {
        val r=TextView(applicationContext)

        val ll = LinearLayout.LayoutParams( WRAP_CONTENT,WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.text=text

        r.width = 200

        r.height = 100

        r.setTextColor(Color.BLACK)

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


    private fun linearLayout_line(): LinearLayout {
        val mLinearLayout2 = LinearLayout(applicationContext)
        val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
        mLinearLayout2.layoutParams = lp2
        mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
        return mLinearLayout2
    }

    private fun hideKeyboard(v: View) {
        val imm: InputMethodManager =
            v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0)
        }
    }

}
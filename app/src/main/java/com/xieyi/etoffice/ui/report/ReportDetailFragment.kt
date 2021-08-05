package com.xieyi.etoffice.ui.report


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*


class ReportDetailFragment() : Fragment() {

    val TAG = "ReportDetailFragment"
    lateinit var buttonImageButton1: ImageView

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    //検索の日付
    var date:String=""

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
        //Log.e(TAG, "onCreate: begin")

        val bundle = arguments
        date = bundle!!.getString("date").toString()

    }

    override fun onDestroyView() {
        super.onDestroyView()

        Toast.makeText(context, "onDestroyView", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "onPause")
        val scrollView: ScrollView = mainView.findViewById(R.id.scrollViewContent)
        scrollView.viewTreeObserver.removeOnScrollChangedListener {

            Toast.makeText(context, "removeOnScrollChangedListener", Toast.LENGTH_SHORT).show()
        }
    }

    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.fragment_report_detail, container, false)

        DataUpdate()

        //after scroll data update
        val scrollView: ScrollView = mainView.findViewById(R.id.scrollViewContent)
        scrollView.viewTreeObserver.addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
//            if (scrollView.scrollY == 0) {
//                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show()
//                DataUpdate()
//            }
//
//            if (scrollView.getChildAt(0).bottom
//                <= scrollView.height + scrollView.scrollY
//            ) {
//                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show()
//                DataUpdate()
//            }

            if (!scrollView.canScrollVertically(1)) {
                // bottom of scroll view
                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show()
                DataUpdate()
            }
            if (!scrollView.canScrollVertically(-1)) {
                // top of scroll view

                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show()
                DataUpdate()

            }
        })



        return mainView
    }


    private fun DataUpdate() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {

                    val r = JC.pEtOfficeGetReportInfo.post(date)
                    doOnUiCode()

                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetReportInfo.post() :$e")

                }
            }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }


    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            Log.e(TAG, "JC.pEtOfficeGetReportInfo:" + JC.pEtOfficeGetReportInfo.lastJson)


            //検索の日付
            val record_date: TextView = mainView.findViewById(R.id.record_date)
            record_date.text = Tools.allDate(date)

            //予定
            val appointment: TextView = mainView.findViewById(R.id.appointment)
            appointment.text = JC.pEtOfficeGetReportInfo.infoJson().result.planworktime

            //planworklist
            planworklistFun()

            //reportlist
            reportlistFun()


            //実績：
            val worktime: TextView = mainView.findViewById(R.id.worktime)
            worktime.text = JC.pEtOfficeGetReportInfo.infoJson().result.worktime



            //workstatuslist：
            val content: LinearLayout = mainView.findViewById(R.id.content)
            val sizeEachY=5
            funContent(sizeEachY, content)

            //commentlist
            commentlistFun()


            //ReportDetailFragment open
            val addView: ImageView = mainView.findViewById(R.id.addView)
            addView.setOnClickListener {

                val pReportAddDialog = ReportAddDialog()

                val fragmentManager = this@ReportDetailFragment.parentFragmentManager
                fragmentManager.let { it1 -> pReportAddDialog.show(it1, "pReportAddDialog") }

            }


            //returnpHome
            val returnHome = mainView.findViewById<ImageView>(R.id.returnHome)
            returnHome.setOnClickListener {
                Navigation.findNavController(mainView)
                    .navigate(R.id.report_fragment);

            }



            sendMessage()
        }
    }

    private fun sendMessage() {
        val messageSend = mainView.findViewById<Button>(R.id.message_send)
        val messageEdit = mainView.findViewById<EditText>(R.id.message_edit)
        messageSend.setOnClickListener {

            hideKeyboard(mainView)
            val messageEditText:String = messageEdit.text.toString()

            GlobalScope.launch(errorHandler) {
                withContext(Dispatchers.IO) {
                    //EtOfficeSetComment
                    //データ更新
                    try {

                        var r = "-1"
                        r = JC.pEtOfficeSetComment.post(date,messageEditText)
                        Log.e(TAG, "sendMessage: r:$r" )
                        if(r=="0"){

                            r = "-1"
                            r = JC.pEtOfficeGetReportInfo.post(date)
                            if(r=="0"){
                                commentlistFun()
                            }

                        }

                    }catch (e:Exception){
                        Log.e(TAG, "pEtOfficeSetComment.post() :$e")

                    }
                }
            }
        }
    }

    private fun planworklistFun() {
        val planworklist: LinearLayout = mainView.findViewById(R.id.planworklist)
        planworklist.removeAllViews()
/*
        {
            "project": "[2021XY07]EtOfficeAPP開発#1",
            "wbs": "[W01]工程A(進捗:%)",
            "date": "2021/07/01-2021/07/31",
            "time": "(計画：160h)"
        }
 */
        val listSize = JC.pEtOfficeGetReportInfo.infoJson().result.planworklist.size

        for (i in 0 until listSize) {
            val ll=ll_planworklist()

            val t1 =getTextView2(JC.pEtOfficeGetReportInfo.infoJson().result.planworklist[i].project)
            t1.setTextColor(Color.parseColor("#000000"))
            t1.textSize = 20F

            val t2 =getTextView2(JC.pEtOfficeGetReportInfo.infoJson().result.planworklist[i].wbs)

            val v3=JC.pEtOfficeGetReportInfo.infoJson().result.planworklist[i].date+" "+
                    JC.pEtOfficeGetReportInfo.infoJson().result.planworklist[i].time
            val t3 =getTextView2(v3)

            ll.addView(t1)
            ll.addView(t2)
            ll.addView(t3)

            planworklist.addView(ll)
        }


    }

    private fun reportlistFun() {
        val reportlist: LinearLayout = mainView.findViewById(R.id.reportlist)
        reportlist.removeAllViews()


/*
        "reportlist": [
        {
            "project": "2021XY07",
            "wbs": "W01",
            "time": "8.00",
            "memo": "Test"
        },
        {
            "project": "2021XY07",
            "wbs": "W01",
            "time": "",
            "memo": ""
        }
        ],
 */

        val listSize = JC.pEtOfficeGetReportInfo.infoJson().result.reportlist.size

        for (i in 0 until listSize) {
            val ll=ll_planworklist()

            val t1 =getTextView2("プロジェクト："+JC.pEtOfficeGetReportInfo.infoJson().result.reportlist[i].project)
            t1.setTextColor(Color.parseColor("#000000"))
            t1.textSize = 20F

            val t2 =getTextView2("作業コード："+JC.pEtOfficeGetReportInfo.infoJson().result.reportlist[i].wbs)

            val t3 =getTextView2("工数："+JC.pEtOfficeGetReportInfo.infoJson().result.reportlist[i].time)

            val t4 =getTextView2("報告："+JC.pEtOfficeGetReportInfo.infoJson().result.reportlist[i].memo)


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

    private suspend fun commentlistFun() {
        withContext(Dispatchers.Main) {
            val commentlist: LinearLayout = mainView.findViewById(R.id.commentlist)
            commentlist.removeAllViews()
/*
        "commentlist": [
        {
            "username": "ユーザー１",
            "comment": "321321",
            "time": "20210805115112"
        },
        {
            "username": "ユーザー１",
            "comment": "this is a test 2",
            "time": "20210803153923"
        },
        {
            "username": "ユーザー１",
            "comment": "this is a test",
            "time": "20210803153901"
        }
 */

            val listSize = JC.pEtOfficeGetReportInfo.infoJson().result.commentlist.size

            for (i in 0 until listSize) {
                val ll = ll_planworklist()

                val t1 =
                    getTextView2("username：" + JC.pEtOfficeGetReportInfo.infoJson().result.commentlist[i].comment)
                t1.setTextColor(Color.parseColor("#000000"))
                t1.textSize = 20F

                val username = JC.pEtOfficeGetReportInfo.infoJson().result.commentlist[i].username
                val time = JC.pEtOfficeGetReportInfo.infoJson().result.commentlist[i].time

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

    }

    private fun funContent(sizeEachY: Int, content: LinearLayout) {
        content.removeAllViews()
        val size = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist.size
        Log.e(TAG, "doOnUiCode: size:$size")
        val l_Y: Array<LinearLayout> = Array(size / sizeEachY + 1) { getLinearLayoutContent() }
        var x = 0
        var y = 0
        for (i in 0 until size) {
            x = i % sizeEachY
            y = i / sizeEachY

            val l_X = getLinearLayout()

            val time = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist[i].time

            val getTextView_time = getTextView(time)
            getTextView_time.setBackgroundColor(Color.parseColor("#E8E8E8"))
            l_X.addView(getTextView_time)


            val text = JC.pEtOfficeGetReportInfo.infoJson().result.workstatuslist[i].status

            val getTextView_1 = getTextView(text)
            getTextView_1.setBackgroundColor(Color.YELLOW)
            l_X.addView(getTextView_1)


            l_Y[y].addView(l_X)

            if (x == sizeEachY - 1) {
                content.addView(l_Y[y])
            }
        }
        content.addView(l_Y[y])
    }


    private fun getLinearLayoutContent(): LinearLayout {
        val r=LinearLayout(activity)

        val ll = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.orientation = LinearLayout.HORIZONTAL

        return r
    }

    //planworklist LinearLayout
    private fun ll_planworklist(): LinearLayout {
        val r=LinearLayout(activity)

        val ll = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        r.layoutParams = ll

        r.orientation = LinearLayout.VERTICAL

        return r
    }


    private fun getLinearLayout(): LinearLayout {
        val r=LinearLayout(activity)

        val ll = LinearLayout.LayoutParams(0, MATCH_PARENT,1.0F)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.setBackgroundResource(R.drawable.ic_round_edge_white)

        r.setPadding(20)

        r.orientation = LinearLayout.VERTICAL

        return r
    }

    private fun getTextView(text:String): TextView {
        val r=TextView(activity)

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
        val r=TextView(activity)

        val ll = LinearLayout.LayoutParams( WRAP_CONTENT,WRAP_CONTENT)
        r.layoutParams = ll

        r.gravity = Gravity.CENTER

        r.text=text


        r.setTextColor(Color.BLACK)

        return r
    }


    private fun linearLayout_line(): LinearLayout {
        val mLinearLayout2 = LinearLayout(activity)
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
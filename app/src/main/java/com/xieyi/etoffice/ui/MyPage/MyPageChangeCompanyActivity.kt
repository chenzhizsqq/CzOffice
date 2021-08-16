package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.EtOfficeGetTenant
import com.xieyi.etoffice.jsonData.EtOfficeLogin
import com.xieyi.etoffice.jsonData.EtOfficeSetTenant

import kotlinx.coroutines.*


class MyPageChangeCompanyActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener  {

    private val TAG = javaClass.simpleName

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    private val tagName: String = "ChangeCompany"


    private lateinit var pEtOfficeGetTenant : EtOfficeGetTenant
    private lateinit var pEtOfficeSetTenant : EtOfficeSetTenant
    private lateinit var pEtOfficeLogin : EtOfficeLogin


    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: GetTenantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_change_company)
        supportActionBar?.hide()

        pEtOfficeGetTenant = EtOfficeGetTenant()
        pEtOfficeSetTenant = EtOfficeSetTenant()
        pEtOfficeLogin = EtOfficeLogin()

        mSwipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = findViewById(R.id.recycler_view_get_tenant)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!recyclerView.canScrollVertically(1)){
                        Log.e(TAG, "onScrollStateChanged: more date", )
                        mSwipeRefreshLayout.isRefreshing = false

                    }
                }

            }
        })

        refreshPage()
    }

    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r = pEtOfficeGetTenant.post()                                    //Json 送信
                    Log.e(TAG, "pEtOfficeGetTenant.post() :$r")

                    doOnUiRefresh()

                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetTenant.post()",e)
                }

            }
        }
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    // UI更新
    private suspend fun doOnUiRefresh() {
        withContext(Dispatchers.Main) {
            Log.e(TAG, "doOnUiRefresh: begin", )

            //record_title
            val recordTitle = findViewById<TextView>(R.id.record_title)
            val tenantid = EtOfficeApp.TenantId
            val hpid = EtOfficeApp.HpId
            recordTitle.text = "TENANTID = $tenantid HPID = $hpid"

            //returnHome
            val returnHome = findViewById<ImageView>(R.id.returnHome)
            returnHome.setOnClickListener {

                val intent: Intent = Intent(this@MyPageChangeCompanyActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            }


            val recyclerView: RecyclerView = findViewById(R.id.recycler_view_get_tenant)
            recyclerView.adapter = GetTenantAdapter(pEtOfficeGetTenant.infoJson().result.tenantlist,this@MyPageChangeCompanyActivity)

        }
    }

    private suspend fun doOnUiCodeOld() {
        withContext(Dispatchers.Main) {
            val recordLinearLayout = findViewById<LinearLayout>(R.id.record_linearLayout)
            recordLinearLayout.removeAllViews()

            //record_title
            val recordTitle = findViewById<TextView>(R.id.record_title)
            val tenantid = EtOfficeApp.TenantId
            val hpid = EtOfficeApp.HpId
            recordTitle.text = "TENANTID = $tenantid HPID = $hpid"

            //Log.e(TAG, "pEtOfficeGetTenant:"+pEtOfficeGetTenant.lastJson )

            val size = pEtOfficeGetTenant.infoJson().result.tenantlist.size

            for (i in 0..size - 1) {

                val mLinearLayout = LinearLayout(applicationContext)

                mLinearLayout.setOrientation(LinearLayout.VERTICAL)

                mLinearLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)


                //mLinearLayout tag
                mLinearLayout.tag = tagName + "_" + i

                val ll_mm = LinearLayout(applicationContext)

                ll_mm.setOrientation(LinearLayout.HORIZONTAL)

                ll_mm.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                ll_mm.gravity = (Gravity.LEFT or Gravity.CENTER)

                val ll_left = leftLL(i)


                ll_mm.addView(ll_left)

                val ll_right = rightLL()
                ll_right.visibility = View.GONE
                ll_right.tag = logoTag(i)
                ll_mm.addView(ll_right)


                mLinearLayout.addView(ll_mm)


                mLinearLayout.setBackgroundColor(Color.WHITE)
                mLinearLayout.setPadding(30)

                //check tenantid
                if(pEtOfficeGetTenant.infoJson().result.tenantlist[i].startflg=="1"){
                    mLinearLayout.setBackgroundColor(Color.GREEN)

                    ll_right.visibility = View.VISIBLE
                }


                //データ　発信
                mLinearLayout.setOnClickListener {

                    //
                    for (j in 0..size - 1) {
                        val ll =
                            recordLinearLayout.findViewWithTag<LinearLayout>(tagName + "_" + j)
                        ll.setBackgroundColor(Color.WHITE)

                        val rightLL=
                            recordLinearLayout.findViewWithTag<LinearLayout>(logoTag(j))
                        rightLL.visibility = View.GONE
                    }

                    ll_right.visibility = View.VISIBLE

                    GlobalScope.launch(errorHandler) {
                        withContext(Dispatchers.IO) {
                            try {

                                val r: String =
                                    pEtOfficeSetTenant.post(pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantid)
                                Log.e(TAG, "tenantid post r=" + r)


                                //check tenantid
                                if (r == "0") {
                                    for (k in  0 .. pEtOfficeSetTenant.infoJson().result.tenantlist.size -1 ){

                                        if(pEtOfficeSetTenant.infoJson().result.tenantlist[k].startflg=="1"){
                                            mLinearLayout.setBackgroundColor(Color.GREEN)



                                            EtOfficeApp.TenantId =
                                                pEtOfficeSetTenant.infoJson().result.tenantlist[k].tenantid
                                            EtOfficeApp.HpId =
                                                pEtOfficeSetTenant.infoJson().result.tenantlist[k].hpid

                                            val r: String =
                                                pEtOfficeLogin.post(EtOfficeApp.uid,EtOfficeApp.password)
                                            Log.e(TAG, "pEtOfficeLogin.post r=" + r)


                                            recordTitle.text = "TENANTID = "+EtOfficeApp.TenantId+" HPID = "+EtOfficeApp.HpId
                                        }

                                    }
                                }

                            } catch (e: Exception) {
                                Log.e(TAG, "mLinearLayout.setOnClickListener: ", e)
                            }
                        }
                    }
                }
                //mLinearLayout touch end


                recordLinearLayout.addView(mLinearLayout)


                //線

                val mLinearLayout2 = LinearLayout(applicationContext)
                val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
                mLinearLayout2.layoutParams = lp2
                mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
                recordLinearLayout.addView(mLinearLayout2)

            }


            //returnHome
            val returnHome = findViewById<ImageView>(R.id.returnHome)
            returnHome.setOnClickListener {

                val intent: Intent = Intent(this@MyPageChangeCompanyActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }

    private fun logoTag(i: Int) = "logo$i"

    private fun leftLL(i: Int): LinearLayout {
        val ll_left = LinearLayout(applicationContext)

        ll_left.orientation = LinearLayout.VERTICAL
        ll_left.layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

        //up
        val textView = TextView(applicationContext)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        textView.setTextColor(Color.parseColor("#000000"))
        textView.text = pEtOfficeGetTenant.infoJson().result.tenantlist[i].tenantname
        ll_left.addView(textView)




        //down
        val textViewDown = TextView(applicationContext)
        textViewDown.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
        textViewDown.setTextColor(Color.parseColor("#000000"))
        textViewDown.text = pEtOfficeGetTenant.infoJson().result.tenantlist[i].posturl
        ll_left.addView(textViewDown)

        return ll_left
    }

    private fun rightLL(): LinearLayout {
        val ll_right = LinearLayout(applicationContext)

        ll_right.orientation = LinearLayout.VERTICAL
        ll_right.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        ll_right.gravity = (Gravity.CENTER or Gravity.RIGHT)

        ll_right.addView(makeImage(100))
        return ll_right
    }


    private fun makeImage(size:Int):ImageView {
        val imageView = ImageView(applicationContext)
        val myDrawable = ResourcesCompat.getDrawable(
            resources, R.drawable.ic_baseline_account_circle_24_blue, null
        )

        //image logo size
        val layoutParams = LinearLayout.LayoutParams(size, size)
        imageView.layoutParams = layoutParams

        //image logo add
        imageView.setImageDrawable(myDrawable)


        return imageView
    }

    override fun onRefresh() {
    }


}
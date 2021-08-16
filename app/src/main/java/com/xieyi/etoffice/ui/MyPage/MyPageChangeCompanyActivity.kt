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
    private lateinit var mAdapter:GetTenantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_change_company)
        supportActionBar?.hide()

        pEtOfficeGetTenant = EtOfficeGetTenant()
        pEtOfficeSetTenant = EtOfficeSetTenant()
        pEtOfficeLogin = EtOfficeLogin()

        refreshPage()

        mSwipeRefreshLayout= findViewById(R.id.swipeRefreshLayout)

        mRecyclerView = findViewById(R.id.recycler_view_get_tenant)
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!recyclerView.canScrollVertically(1)){
                        //Log.e(TAG, "onScrollStateChanged: more date")
                        mSwipeRefreshLayout.isRefreshing = false
                        refreshPage()

                    }
                }

            }
        })

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this);
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
            //Log.e(TAG, "doOnUiRefresh: begin")

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

            val sortedList = pEtOfficeGetTenant.infoJson().result.tenantlist.sortedWith(compareBy(
                { it.tenantid }
                , { it.tenantname }
                , { it.hpid }
                , { it.posturl }
            ))
            mAdapter=GetTenantAdapter(sortedList,this@MyPageChangeCompanyActivity)


            mAdapter.setOnAdapterListener(object : GetTenantAdapter.OnAdapterListener{
                override fun onClick(tenantid: String) {

                    GlobalScope.launch(errorHandler) {
                        withContext(Dispatchers.IO) {
                            //データ更新 SetTenant
                            try {
                                if(pEtOfficeSetTenant.post(tenantid)=="0"){
                                    if(pEtOfficeLogin.post(EtOfficeApp.uid,EtOfficeApp.password) == "0"){
                                        refreshPage()
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e(TAG, "pEtOfficeSetTenant.post()",e)
                            }

                        }
                    }

                }
            })

            recyclerView.adapter = mAdapter

        }
    }

    override fun onRefresh() {

        mSwipeRefreshLayout.isRefreshing = false;
        refreshPage()
    }


}
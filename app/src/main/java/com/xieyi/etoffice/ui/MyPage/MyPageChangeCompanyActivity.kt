package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.R
import com.xieyi.etoffice.databinding.ActivityMyPageChangeCompanyBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetTenant
import com.xieyi.etoffice.jsonData.EtOfficeLogin
import com.xieyi.etoffice.jsonData.EtOfficeSetTenant
import kotlinx.coroutines.*


class MyPageChangeCompanyActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener  {

    private val TAG = javaClass.simpleName

    private lateinit var pEtOfficeGetTenant : EtOfficeGetTenant
    private lateinit var pEtOfficeSetTenant : EtOfficeSetTenant
    private lateinit var pEtOfficeLogin : EtOfficeLogin


    //private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    //private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter:GetTenantAdapter

    private lateinit var binding: ActivityMyPageChangeCompanyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMyPageChangeCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pEtOfficeGetTenant = EtOfficeGetTenant()
        pEtOfficeSetTenant = EtOfficeSetTenant()
        pEtOfficeLogin = EtOfficeLogin()

        refreshPage()

        binding.recyclerViewGetTenant.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!recyclerView.canScrollVertically(1)){
                        //Log.e(TAG, "onScrollStateChanged: more date")
                        binding.swipeRefreshLayout.isRefreshing = false
                        refreshPage()

                    }
                }

            }
        })

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this);
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
            mAdapter=GetTenantAdapter(sortedList)


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
        binding.swipeRefreshLayout.isRefreshing = false;
        refreshPage()
    }


}
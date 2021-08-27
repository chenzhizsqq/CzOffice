package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.databinding.ActivityMyPageChangeCompanyBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetTenant
import com.xieyi.etoffice.jsonData.EtOfficeLogin
import com.xieyi.etoffice.jsonData.EtOfficeSetTenant
import kotlinx.coroutines.*


class MyPageChangeCompanyActivity : BaseActivity(),
    SwipeRefreshLayout.OnRefreshListener  {

    private val TAG = javaClass.simpleName

    private lateinit var mAdapter:GetTenantAdapter

    private lateinit var binding: ActivityMyPageChangeCompanyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMyPageChangeCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EtOfficeGetTenantPost()

        binding.recyclerViewGetTenant.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!recyclerView.canScrollVertically(1)){
                        binding.swipeRefreshLayout.isRefreshing = false
                        EtOfficeGetTenantPost()
                    }
                }
            }
        })

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this);
    }


    private fun EtOfficeGetTenantPost() {
        Api.EtOfficeGetTenant(
            context = this@MyPageChangeCompanyActivity,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetTenantResult(model.result)
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

    private fun EtOfficeSetTenantPost(tenantid: String) {
        Api.EtOfficeSetTenant(
            context = this@MyPageChangeCompanyActivity,
            tenantid= tenantid,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeSetTenantResult(model.result)
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

    // UI更新
    private  fun EtOfficeGetTenantResult(result: EtOfficeGetTenant.Result) {
        //Log.e(TAG, "doOnUiRefresh: begin")

        //record_title
        val recordTitle = binding.recordTitle
        val tenantid = EtOfficeApp.TenantId
        val hpid = EtOfficeApp.HpId
        recordTitle.text = "TENANTID = $tenantid HPID = $hpid"

        //returnHome
        val returnHome = binding.returnHome
        returnHome.setOnClickListener {

            val intent: Intent = Intent(this@MyPageChangeCompanyActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }


        val recyclerView: RecyclerView = binding.recyclerViewGetTenant

        val sortedList = result.tenantlist.sortedWith(compareBy(
            { it.tenantid }
            , { it.tenantname }
            , { it.hpid }
            , { it.posturl }
        ))
        mAdapter=GetTenantAdapter(sortedList)


        mAdapter.setOnAdapterListener(object : GetTenantAdapter.OnAdapterListener{
            override fun onClick(tenantid: String) {
                EtOfficeSetTenantPost(tenantid)
            }
        })

        recyclerView.adapter = mAdapter

    }

    private  fun EtOfficeSetTenantResult(result: EtOfficeSetTenant.Result) {
        for (i in result.tenantlist.indices){
            if (result.tenantlist[i].startflg == "1"){
                EtOfficeApp.TenantId = result.tenantlist[i].tenantid
                EtOfficeApp.HpId = result.tenantlist[i].hpid

                EtOfficeGetTenantPost()
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false;
        EtOfficeGetTenantPost()
    }


}
package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.Tools
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

    private lateinit var pEtOfficeGetTenant : EtOfficeGetTenant
    private lateinit var pEtOfficeSetTenant : EtOfficeSetTenant
    private lateinit var pEtOfficeLogin : EtOfficeLogin

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

        EtOfficeGetTenantPost()

        binding.recyclerViewGetTenant.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    if(!recyclerView.canScrollVertically(1)){
                        //Log.e(TAG, "onScrollStateChanged: more date")
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
        Api.EtOfficeGetTenantFun(
            context = this@MyPageChangeCompanyActivity,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetTenantResult(model.result)
                        }
                        1 -> {
                            Tools.msgAlertDialog(
                                this@MyPageChangeCompanyActivity,
                                model.status.toString(),
                                model.message,
                                "確認"
                            )
                        }
                        else -> {
                            Tools.msgAlertDialog(
                                this@MyPageChangeCompanyActivity,
                                model.status.toString(),
                                model.message,
                                "確認"
                            )
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data");
                    //CommonUtil.handleError(it, error, data)
                }
            }
        )

    }

    private fun EtOfficeSetTenantPost(tenantid: String) {
        Api.EtOfficeSetTenantFun(
            context = this@MyPageChangeCompanyActivity,
            tenantid= tenantid,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeSetTenantResult(model.result)
                        }
                        1 -> {
                            Tools.msgAlertDialog(
                                this@MyPageChangeCompanyActivity,
                                model.status.toString(),
                                model.message,
                                "確認"
                            )
                        }
                        else -> {
                            Tools.msgAlertDialog(
                                this@MyPageChangeCompanyActivity,
                                model.status.toString(),
                                model.message,
                                "確認"
                            )
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data");
                    //CommonUtil.handleError(it, error, data)
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
        EtOfficeLoginPost(EtOfficeApp.uid,EtOfficeApp.password)
    }

    private fun EtOfficeLoginPost(uid:String, password:String) {
        Api.EtOfficeLogin(
            context = this@MyPageChangeCompanyActivity,
            uid = uid,
            password = password,
            registrationid = "6",
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {

                            //data save
                            EtOfficeApp.TenantId = model.result.tenantid
                            EtOfficeApp.HpId = model.result.hpid
                            EtOfficeApp.uid = uid
                            EtOfficeApp.password = password
                            EtOfficeApp.userid = model.result.userid
                            EtOfficeGetTenantPost()
                        }
                        1 -> {
                            Tools.msgAlertDialog(
                                this@MyPageChangeCompanyActivity,
                                model.status.toString(),
                                model.message,
                                "確認"
                            )
                        }
                        else -> {
                            Tools.msgAlertDialog(
                                this@MyPageChangeCompanyActivity,
                                model.status.toString(),
                                model.message,
                                "確認"
                            )
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data");
                    //CommonUtil.handleError(it, error, data)
                }
            }
        )

    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false;
        EtOfficeGetTenantPost()
    }


}
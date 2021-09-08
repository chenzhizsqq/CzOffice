package com.xieyi.etoffice.ui.MyPage

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.TenantResult
import com.xieyi.etoffice.databinding.ActivityMyPageChangeCompanyBinding
import kotlinx.coroutines.*
import java.util.*


class MyPageChangeCompanyActivity : BaseActivity(),
    SwipeRefreshLayout.OnRefreshListener {

    private val TAG = "MyPageChangeCompanyActivity"

    private lateinit var mAdapter: GetTenantAdapter

    private lateinit var binding: ActivityMyPageChangeCompanyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageChangeCompanyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EtOfficeGetTenantPost()

        binding.recyclerViewGetTenant.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                        binding.swipeRefreshLayout.isRefreshing = false
                        EtOfficeGetTenantPost()
                    }
                }
            }
        })

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this)
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

    private fun EtOfficeSetTenantPost(tenantid: String) {
        Api.EtOfficeSetTenant(
            context = this@MyPageChangeCompanyActivity,
            tenant = tenantid,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeSetTenantResult(model.result)
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

    // UI更新
    private fun EtOfficeGetTenantResult(result: TenantResult) {

        //record_title
        val recordTitle = binding.recordTitle

        val prefs = EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        val tenantid: String? = prefs.getString("tenantid", "")
        val hpid: String? = prefs.getString("hpid", "")
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
            { it.tenantid }, { it.tenantname }, { it.hpid }, { it.posturl }
        ))
        Collections.reverse(sortedList)
        mAdapter = GetTenantAdapter(sortedList)


        mAdapter.setOnAdapterListener(object : GetTenantAdapter.OnAdapterListener {
            override fun onClick(tenantid: String) {
                EtOfficeSetTenantPost(tenantid)
            }
        })

        recyclerView.adapter = mAdapter

    }

    private fun EtOfficeSetTenantResult(result: TenantResult) {
        for (i in result.tenantlist.indices) {
            if (result.tenantlist[i].startflg == "1") {
                val userInfo = getSharedPreferences(Config.EtOfficeUser, MODE_PRIVATE)
                val changeListener =
                    SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
                    }
                userInfo.registerOnSharedPreferenceChangeListener(changeListener)

                val editor = userInfo.edit()
                editor.apply {
                    putString("tenantid", result.tenantlist[i].tenantid)
                    putString("hpid", result.tenantlist[i].hpid)
                }.apply()

                //会社选中之后直接跳转到【我的】页面
                val intent: Intent = Intent(this@MyPageChangeCompanyActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
        EtOfficeGetTenantPost()
    }


}
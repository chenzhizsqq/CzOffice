package com.xieyi.etoffice

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class EtOfficeApp : Application() {
    companion object {
        lateinit var Token :String    // 用户Token
        lateinit var TenantId :String // 用户tenantid
        lateinit var HpId :String     // 用户hpid

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val prefs = context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        val isLogin = prefs.getBoolean("isLogin",false)
        if (isLogin){
            Token = prefs.getString("token", "").toString()
            TenantId = prefs.getString("tenantid", "").toString()
            HpId = prefs.getString("hpid", "").toString()
        }
    }
}
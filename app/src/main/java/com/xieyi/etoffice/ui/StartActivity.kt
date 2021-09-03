package com.xieyi.etoffice.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.LoginResultInfo
import com.xieyi.etoffice.databinding.ActivityStartBinding
import com.xieyi.etoffice.ui.login.LoginActivity
import okhttp3.*

class StartActivity : BaseActivity() {
    val TAG: String = "StartActivity"
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 读取用户信息，如果已经登录了，直接跳转到Main画面
        val prefs = getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        val isLogin = prefs.getBoolean("isLogin", false)

        if (isLogin) {
            val intent = Intent(this@StartActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            gotoLogin()
        }
    }

    /**
     * ログイン画面へ遷移
     */
    private fun gotoLogin() {
        val intent = Intent(this@StartActivity, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

}
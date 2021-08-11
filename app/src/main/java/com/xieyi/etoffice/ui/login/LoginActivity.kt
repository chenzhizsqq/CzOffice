package com.xieyi.etoffice.ui.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.EtOfficeApp.Companion.context
import com.xieyi.etoffice.MainActivity
import com.xieyi.etoffice.R
import com.xieyi.etoffice.common.HttpUtil
import com.xieyi.etoffice.databinding.ActivityLoginBinding
import com.xieyi.etoffice.enum.ResultType
import com.xieyi.etoffice.jsonData.JC
import okhttp3.*
import org.json.JSONObject
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity(),View.OnClickListener {
    val TAG: String = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        if(Config.isCode) {
            binding.userName.setText("demo1@xieyi.co.jp")
            binding.password.setText("pass")
        }

        binding.btnLogin.setOnClickListener(this)

        // 读取用户信息，如果已经登录了，直接跳转到Main画面
        val prefs = getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        val isLogin = prefs.getBoolean("isLogin",false)

        if (isLogin) {
            Thread {
                try {
                    if(Config.isCode) {
                        JC.pEtOfficeLogin.post("demo1@xieyi.co.jp", "pass")
                    }
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: Exception) {
                    Log.e(TAG, "JC.pEtOfficeLogin.post", e)
                }
            }.start()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login->{
                if (isValid()) {
                    Snackbar.make(view, "登録中....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    sendLoginRequest()
                }
            }
        }
    }

    override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
        // 点击屏幕空白区域，隐藏软键盘
        if (currentFocus != null) {
            val inputMethodManager: InputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(motionEvent)
    }

    // 最新メッセージ一覧取得
    private fun sendLoginRequest() {
        thread {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeLogin")
            jsonObject.put("uid", binding.userName.text.toString())
            jsonObject.put("password", binding.password.text.toString())
            jsonObject.put("registrationid", "6")
            jsonObject.put("device",Config.Device)
            // 通信処理
            HttpUtil.callAsyncHttp(
                context = EtOfficeApp.context,
                url = Config.LoginUrl,
                parameter = jsonObject,
                classType = LoginResponse::class.java as Class<Any>,
                authToken = true,
                fcmToken = true,
                onSuccess = ::onSuccess,
                onFailure = ::onFailure
            )
        }
    }

    // 成功結果処理
    private fun onSuccess(data: Any){
        if (data is LoginResponse)
            when(data.status){
                "0"-> {
                    if(Config.isCode) {
                        JC.pEtOfficeLogin.post("demo1@xieyi.co.jp", "pass")
                    }
                    saveUserInfo(data.result)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                "1"-> {
                    Snackbar.make(binding.userName, R.string.login_failed_msg1, Snackbar.LENGTH_LONG)
                        .show()
                }
                else ->{
                    Snackbar.make(binding.userName, R.string.login_failed_msg2, Snackbar.LENGTH_LONG)
                        .show()
                }
            }
    }

    // 通信失敗時、結果処理
    private fun onFailure(error: ResultType, data: Any){
        Log.e(TAG, "onFailure:$data");
    }

    private fun isValid() : Boolean{
        val username: String = binding.userName.text.toString()
        val password: String = binding.password.text.toString()
        if (username.isNullOrEmpty()){
            Snackbar.make(binding.userName, R.string.username_require, Snackbar.LENGTH_LONG)
                .show()
            return false
        }
        if (password.isNullOrEmpty()){
            Snackbar.make(binding.password, R.string.password_require, Snackbar.LENGTH_LONG)
                .show()
            return false
        }
        return true
    }

    /**
     * 存储用户信息
     */
    private fun saveUserInfo(user:Result) {
        val userInfo = getSharedPreferences(Config.EtOfficeUser, MODE_PRIVATE)
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            }
        userInfo.registerOnSharedPreferenceChangeListener(changeListener)

        val editor = userInfo.edit()
        editor.apply(){
            putString("tenantid", user.tenantid)
            putString("hpid", user.hpid)
            putString("token", user.token)
            putString("username", user.username)
            putString("userid", user.userid)
            putBoolean("isLogin", true)
        }.apply()
        EtOfficeApp.Token = user.token
        EtOfficeApp.HpId = user.hpid
        EtOfficeApp.TenantId = user.tenantid
    }

}
package com.xieyi.etoffice.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.*
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.LoginResultInfo
import com.xieyi.etoffice.databinding.ActivityLoginBinding
import okhttp3.*
import android.text.Editable
import android.text.TextWatcher


class LoginActivity : BaseActivity(), View.OnClickListener {
    val TAG: String = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        if (Config.isCode) {
//            binding.userName.setText("demo1@xieyi.co.jp")
//            binding.password.setText("pass")
//        }
        // 画面初期化
        initView()

        if (!isNetworkConnected()) {
            Tools.showMsg(binding.root, getString(R.string.network_no))
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_login -> {
                if (isValid()) {
                    Snackbar.make(view, getString(R.string.login_registering), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    login()
                }
            }
        }
    }

    /**
     * ビュー初期化
     */
    private fun initView() {
        binding.btnLogin.setOnClickListener(this)

        // 入力監視
        binding.userName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                judgeLoginEnable()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                judgeLoginEnable()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
            }
        })

        judgeLoginEnable()
    }

    /**
     * ログイン可能かを判断
     */
    private fun judgeLoginEnable() {
        if (binding.userName.text.toString().trim().isEmpty() || binding.password.text.toString()
                .trim().isEmpty()
        ) {
            binding.btnLogin.isEnabled = false
        } else {
            binding.btnLogin.isEnabled = true
        }
    }

    // ログイン処理
    private fun login() {
        Api.EtOfficeLogin(
            context = this@LoginActivity,
            uid = binding.userName.text.toString(),
            password = binding.password.text.toString(),
            registrationid = "6",
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {

                            saveUserInfo(model.result)
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        1 -> {
                            Snackbar.make(
                                binding.userName,
                                R.string.login_failed_msg1,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            Snackbar.make(
                                binding.userName,
                                R.string.login_failed_msg2,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data");
                    Snackbar.make(
                        binding.userName,
                        R.string.login_failed_msg2,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        )

    }

    private fun isValid(): Boolean {
        val username: String = binding.userName.text.toString()
        val password: String = binding.password.text.toString()
        if (username.isNullOrEmpty()) {
            return false
        }
        if (password.isNullOrEmpty()) {
            return false
        }
        return true
    }

    /**
     * 存储用户信息
     */
    private fun saveUserInfo(user: LoginResultInfo) {
        val userInfo = getSharedPreferences(Config.EtOfficeUser, MODE_PRIVATE)
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            }
        userInfo.registerOnSharedPreferenceChangeListener(changeListener)

        val editor = userInfo.edit()
        editor.apply() {
            putString("tenantid", user.tenantid)
            putString("hpid", user.hpid)
            putString("token", user.token)
            putString("username", user.username)
            putString("userid", user.userid)
            putBoolean("isLogin", true)
        }.apply()
    }

}
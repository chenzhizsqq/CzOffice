package com.xieyi.etoffice.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.*
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.LoginResultInfo
import com.xieyi.etoffice.common.setupClearButtonWithAction
import com.xieyi.etoffice.databinding.ActivityLoginBinding
import kotlinx.coroutines.*
import okhttp3.*


class LoginActivity : BaseActivity(), View.OnClickListener {
    val TAG: String = "LoginActivity"
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Config.isCode) {
            binding.userName.setText("demo1@xieyi.co.jp")
            binding.password.setText("pass")
        }
        // 画面初期化
        initView()

        if (!isNetworkConnected()) {
            Tools.showErrorDialog(this, getString(R.string.MSG05))
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


        //EditText，编辑框(EditText)右侧追加一个自动清除按钮，输入内容后删除按钮表示，可以清除内容。
        binding.userName.setupClearButtonWithAction()
        binding.password.setupClearButtonWithAction()

    }

//    private fun addRightCancelDrawable(editText: EditText) {
//        val cancel = ContextCompat.getDrawable(this, R.drawable.ic_cancel_black_24dp)
//        cancel?.setBounds(0,0, cancel.intrinsicWidth, cancel.intrinsicHeight)
//        editText.setCompoundDrawables(null, null, cancel, null)
//    }

    /**
     * ログイン可能かを判断
     */
    private fun judgeLoginEnable() {
        binding.btnLogin.isEnabled =
            !(binding.userName.text.toString().trim().isEmpty() || binding.password.text.toString()
                .trim().isEmpty())
    }

    // ログイン処理
    private fun login() {
        lifecycleScope.launch {
            Api.EtOfficeLogin(
                context = this@LoginActivity,
                uid = binding.userName.text.toString(),
                password = binding.password.text.toString(),
                registrationid = "6",
                onSuccess = { model ->
                    lifecycleScope.launch {
                        when (model.status) {
                            0 -> {
                                saveUserInfo(model.result)
                                val intent =
                                    Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                Tools.showErrorDialog(
                                    this@LoginActivity,
                                    getString(R.string.MSG03)
                                )
                            }
                        }
                    }

                },
                onFailure = { error, data ->
                    lifecycleScope.launch {
                        Log.e(TAG, "onFailure:$data")
                        Tools.showErrorDialog(
                            this@LoginActivity,
                            getString(R.string.login_failed_msg2)
                        )
                    }

                }
            )
        }


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
        editor.apply {
            putString("tenantid", user.tenantid)
            putString("hpid", user.hpid)
            putString("token", user.token)
            putString("username", user.username)
            putString("userid", user.userid)
            putBoolean("isLogin", true)
        }.apply()
    }

}
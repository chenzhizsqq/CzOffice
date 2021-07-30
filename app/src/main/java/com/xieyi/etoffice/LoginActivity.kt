package com.xieyi.etoffice

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.jsonData.JC
import okhttp3.*

class LoginActivity : AppCompatActivity() {
    val TAG: String = "LoginActivity"
    private lateinit var editTextTextPersonNameLogin: EditText
    private lateinit var editTextTextPasswordLogin: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        editTextTextPersonNameLogin =
            findViewById<View>(R.id.editTextTextPersonNameLogin) as EditText
        editTextTextPasswordLogin = findViewById<View>(R.id.editTextTextPasswordLogin) as EditText

        if(Config.isCode) {
            editTextTextPersonNameLogin.setText("demo1@xieyi.co.jp")
            editTextTextPasswordLogin.setText("pass")
        }

        userInfo()
    }

    //EtCampLogin、json登録
    private fun postRequest(view: View) {
        val mEditTextTextEmailAddress: String = editTextTextPersonNameLogin.text.toString()
        val mEditTextTextPassword: String = editTextTextPasswordLogin.text.toString()
        Thread {

            try {

                val r =
                    JC.pEtOfficeLogin.post(mEditTextTextEmailAddress, mEditTextTextPassword)
                Log.e("$TAG Login :", JC.pEtOfficeLogin.infoLoginResult().toString())


                if (r == "0") {
                    saveUserInfo(JC.pEtOfficeLogin.infoLoginResult().tenantid ,
                        JC.pEtOfficeLogin.infoLoginResult().hpid)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else if (r == "6") {
                    Snackbar.make(view, "入力してください", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                } else if (r == "1") {

                    Snackbar.make(view, "「アカウント」や「パスウード」が間違います。", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                } else {
                    Snackbar.make(view, "Error:$r", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .show()
                }
            } catch (e: Exception) {
                Snackbar.make(view, "Error:$e", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
                Log.e(TAG, "TAG", e)
                if(Config.isTest){
                    val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }


    /**
     * 存储用户信息
     */
    private fun saveUserInfo(tenantid:String,hpid:String) {
        val userInfo = getSharedPreferences(Config.appName, MODE_PRIVATE)
        val changeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { preferences, key ->
            }
        userInfo.registerOnSharedPreferenceChangeListener(changeListener)

        val editor = userInfo.edit()
        editor.putString("tenantid", tenantid)
        editor.putString("hpid", hpid)
        editor.commit()
    }

    /**
     * 读取用户信息
     */
    private fun userInfo(){
        val userInfo = getSharedPreferences(Config.appName, MODE_PRIVATE)
        val tenantid = userInfo.getString("tenantid", Config.tenantid)
        val hpid = userInfo.getString("hpid", Config.hpid)
        Log.e(TAG, "读取用户信息")
        Log.e(
            TAG,
            "tenantid:$tenantid， hpid:$hpid"
        )
    }


    fun login(view: View) {

        Snackbar.make(view, "登録中....", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        postRequest(view)
    }
}
package com.xieyi.etoffice

import android.content.Intent
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
    private var editTextTextPersonNameLogin: EditText? = null
    private var editTextTextPasswordLogin: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextTextPersonNameLogin =
            findViewById<View>(R.id.editTextTextPersonNameLogin) as EditText
        editTextTextPasswordLogin = findViewById<View>(R.id.editTextTextPasswordLogin) as EditText

        if(Config.isCode) {
            editTextTextPersonNameLogin!!.setText("demo1@xieyi.co.jp")
            editTextTextPasswordLogin!!.setText("pass")
        }
    }

    //EtCampLogin、json登録
    private fun postRequest(view: View) {
        var mEditTextTextEmailAddress: String = editTextTextPersonNameLogin?.text.toString()
        var mEditTextTextPassword: String = editTextTextPasswordLogin?.text.toString()
        Thread {

            try {

                val r =
                    JC.pEtOfficeLogin.post(mEditTextTextEmailAddress, mEditTextTextPassword)
                Log.e("$TAG Login :", JC.pEtOfficeLogin.infoLoginResult().toString())


                if (r == "0") {
                    val intent: Intent = Intent(this@LoginActivity, MainActivity::class.java)
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


    fun login(view: View) {

        Snackbar.make(view, "登録中....", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        postRequest(view)
    }
}
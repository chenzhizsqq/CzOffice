package com.xieyi.etoffice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    val TAG:String ="LoginActivity"
    private var editTextTextPersonNameLogin: EditText? = null
    private var editTextTextPasswordLogin: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextTextPersonNameLogin = findViewById<View>(R.id.editTextTextPersonNameLogin) as EditText
        editTextTextPasswordLogin = findViewById<View>(R.id.editTextTextPasswordLogin) as EditText

        //test-chen:テスト使い物
        editTextTextPersonNameLogin!!.setText("demo1@xieyi.co.jp")
        editTextTextPasswordLogin!!.setText("pass")
        //test-chen:テスト使い物
    }

    //EtCampLogin、json登録
    private fun postRequest(view: View) {
        var mEditTextTextEmailAddress: String = editTextTextPersonNameLogin?.text.toString()
        var mEditTextTextPassword: String = editTextTextPasswordLogin?.text.toString()
        Thread {

            val r:String=JsonCenter.loginPost(mEditTextTextEmailAddress,mEditTextTextPassword)
            Log.e(TAG,JsonCenter.lastLoginResultJson)
            Log.e(TAG+" token",JsonCenter.loginResult("token"))

            if(r=="0"){
                val intent: Intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }else if(r=="6") {
                Snackbar.make(view, "入力してください", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }else if(r=="1"){

                Snackbar.make(view, "「アカウント」や「パスウード」が間違います。", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }else{
                Snackbar.make(view, "Error", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
            }
        }.start()
    }


    fun login(view: View) {

        Snackbar.make(view, "登録中....", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show()
        postRequest(view)
    }
}
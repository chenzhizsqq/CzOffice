package com.example.etoffice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private var editTextTextPersonNameLogin: EditText? = null
    private var editTextTextPasswordLogin: EditText? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextTextPersonNameLogin = findViewById<View>(R.id.editTextTextPersonNameLogin) as EditText
        editTextTextPasswordLogin = findViewById<View>(R.id.editTextTextPasswordLogin) as EditText
    }

    //EtCampLogin、json登録
    private fun postRequest(view: View) {
        val mEditTextTextEmailAddress: String = editTextTextPersonNameLogin?.text.toString()
        val mEditTextTextPassword: String = editTextTextPasswordLogin?.text.toString()
        Thread {
//            if (DataCenter.pData.LoginInit(mEditTextTextEmailAddress, mEditTextTextPassword)) {
//
//                // 「pref_data」という設定データファイルを読み込み
//                val prefData = getSharedPreferences(SharedPreferences_Login, MODE_PRIVATE)
//                val editor = prefData.edit()
//
//                // 入力されたログインIDとログインパスワード
//                editor.putString("account", editTextTextEmailAddress.getText().toString())
//
//                // 保存
//                editor.apply()
//                DataCenter.UpdateData()
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
//            } else {
//                Snackbar.make(view, "「アカウント」や「パスウード」が間違います。", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .show()
//            }
        }.start()
    }

    fun login(view: View) {

        Snackbar.make(view, "登録中....", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        postRequest(view)
//        val intent: Intent = Intent(this@LoginActivity,
//                MainActivity::class.java)
//        startActivity(intent)
//        finish()
    }
}
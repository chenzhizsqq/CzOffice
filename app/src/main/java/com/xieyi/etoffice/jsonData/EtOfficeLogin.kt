package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject


//EtOfficeLogin ログイン処理、ユーザー情報取得
class EtOfficeLogin {

    data class LoginResult(
        val hpid: String,
        val mail: String,
        val phone: String,
        val tenantid: String,
        val token: String,
        val usercode: String,
        val userid: String,
        val userkana: String,
        val username: String
    )

    data class JsonClass(
        val message: String,
        val result: LoginResult,
        val status: Int
    )

}


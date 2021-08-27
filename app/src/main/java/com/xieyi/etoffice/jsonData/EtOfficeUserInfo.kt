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


//EtOfficeUserInfo ユーザー情報取得
class EtOfficeUserInfo {
    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )

    data class Result(
        val userid: String,     //ユーザー識別ID
        val usercode: String,   //社員コード
        val username: String,    //username
        val userkana: String,   //userkana
        val mail: String,       //mail
        val phone: String,      //phone
    )
}

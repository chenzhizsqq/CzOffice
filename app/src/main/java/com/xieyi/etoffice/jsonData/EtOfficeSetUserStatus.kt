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


//EtOfficeSetUserStatus ユーザー勤務状態の設定
class EtOfficeSetUserStatus {

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )
    data class Result(
        val userstatuslist: List<Userstatuslist>
    )
    data class Userstatuslist(
        val location: String,
        val memo: String,
        val statustext: String,
        val statustime: String,
        val statusvalue: String,
        val usercode: String,
        val userid: String,
        val userkana: String,
        val username: String
    )
}


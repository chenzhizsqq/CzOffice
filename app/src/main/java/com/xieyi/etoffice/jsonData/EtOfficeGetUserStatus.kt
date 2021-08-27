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


//EtOfficeGetUserStatus ユーザー最新勤務状態の一覧取得
class EtOfficeGetUserStatus {

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )
    data class Result(
        val userstatuslist: List<Userstatuslist>
    )

    data class Userstatuslist(
        val userid: String,         //ユーザー識別ID
        val usercode: String,       //社員コード
        val username: String,       //氏名
        val userkana: String,       //カナ
        val location: String,       //最新勤務状態更新場所
        val memo: String,           //備考
        val statustext: String,     //最新勤務状態文字列
        val statustime: String,     //最新勤務状態更新時刻
        val statusvalue: String,    //最新勤務状態値
    )

}


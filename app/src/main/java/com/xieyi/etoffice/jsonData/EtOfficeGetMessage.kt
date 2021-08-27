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


//EtOfficeGetMessage   最新メッセージ一覧取得
class EtOfficeGetMessage {

    data class Messagelist(
        val content: String,
        val subid: String,
        val title: String,
        val updatetime: String
    )

    data class Recordlist(
        val memo: String,
        val statustext: String,
        val statustime: String,
        val statusvalue: String
    )

    data class Result(
        val messagelist: List<Messagelist>,
        val recordlist: List<Recordlist>
    )

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )
}


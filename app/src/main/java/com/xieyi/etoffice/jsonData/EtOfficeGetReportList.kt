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


//EtOfficeGetReportList
class EtOfficeGetReportList {

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )

    data class Group(
    val month: String,
    val reportlist: ArrayList<Reportlist>
)

    data class Reportlist(
    val approval: String,
    val content: String,
    val holiday: String,
    val itemid: String,
    val title: String,
    val warning: String,
    val yyyymmdd: String
)
    data class Result(
    val authflag: String,
    val group: ArrayList<Group>
)

}


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


//EtOfficeGetReportInfo 日報詳細取得
class EtOfficeGetReportInfo {

    data class Commentlist(
        val comment: String,
        val time: String,
        val username: String
    )

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )

    data class Planworklist(
        val date: String,
        val project: String,
        val time: String,
        val wbs: String
    )

    data class Reportlist(
        val memo: String,
        val project: String,
        val time: String,
        val wbs: String
    )

    data class Result(
        val authflag: String,
        val commentlist: List<Commentlist>,
        val planworklist: List<Planworklist>,
        val planworktime: String,
        val reportlist: List<Reportlist>,
        val workstatuslist: List<Workstatuslist>,
        val worktime: String
    )

    data class Workstatuslist(
        val status: String,
        val time: String
    )

}

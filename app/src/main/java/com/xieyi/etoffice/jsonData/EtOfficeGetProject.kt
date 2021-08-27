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


//EtOfficeGetProject プロジェクト一覧
class EtOfficeGetProject {

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )

    data class Projectlist(
        val projectcd: String,
        val projectname: String,
        val wbslist: List<Wbslist>
    )

    data class Result(
        val projectlist: List<Projectlist>
    )

    data class Wbslist(
        val date: String,
        val time: String,
        val wbscd: String,
        val wbsname: String
    )
}

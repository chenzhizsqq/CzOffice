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


//EtOfficeGetStuffList   社員一覧取得
class EtOfficeGetStuffList {

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )

    data class Result(
        val sectionlist: ArrayList<SectionList>
    )

    data class SectionList(
        val sectioncd: String,
        val sectionname: String,
        var stufflist:List<StuffList>
    )

    data class StuffList(
        val tenant: String,
        val hpid: String,
        val userid: String,
        val username: String,
        val userkana: String,
        val phone: String,
        val mail: String,
    )

}


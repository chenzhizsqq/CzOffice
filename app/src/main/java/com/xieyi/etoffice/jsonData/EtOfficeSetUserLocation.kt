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


//EtOfficeSetUserLocation   ユーザー勤務場所設定
class EtOfficeSetUserLocation {

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )
    data class Result(
        val locationlist: List<EtOfficeSetUserLocationlist>
    )
    data class EtOfficeSetUserLocationlist(
        val latitude: String,
        val location: String,
        val longitude: String
    )
}


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

    val TAG = javaClass.simpleName
    var lastJson: String = ""
    val app: String = "EtOfficeGetReportList"

    /*
        {"app":"EtOfficeGetReportList"
            ,"token":"202107141727590980000000090010001502491490940587"
            ,"device":"android"
            ,"tenant":"3"
            ,"hpid":"6"
            ,"startym":""
            ,"months":""}
     */
    fun post(): String {
        var status: String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url: String = Config.LoginUrl

        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", app)
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", "android")
            jsonObject.put("startym", "")
            jsonObject.put("months", "")
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(body).build()

            val response: Response = client.newCall(request).execute();
            if (response.isSuccessful) {

                val json: String = response.body!!.string()
                lastJson = json
                val mJsonResult = JSONObject(json)
                //Log.e(TAG, "mJsonResult:$mJsonResult")

                status = mJsonResult.getString("status")


                return status
            } else {
                Log.e(TAG, "postRequest: false")
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return status
    }


    /*
    {"status":0,"result":{"sectionlist":[]},"message":""}
     */


    fun infoJson(): JsonClass {
            val gson = Gson()
            val mJson: JsonClass =
                gson.fromJson(lastJson, JsonClass::class.java)
            return mJson
    }
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


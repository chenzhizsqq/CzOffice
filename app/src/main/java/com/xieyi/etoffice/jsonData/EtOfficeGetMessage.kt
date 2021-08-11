package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.Tools
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject


//EtOfficeGetMessage   最新メッセージ一覧取得
class EtOfficeGetMessage {

    val TAG = "EtOfficeGetMessage"
    var lastJson: String = ""
    val app: String = "EtOfficeGetMessage"

        /*
            {
              "app": "EtOfficeGetMessage",
              "token": "202011291352391050000000090010000000000000010125",
              "device": "android",
              "tenant": "1",
              "hpid": "8",
              "count": "50",
              "lasttime": "",
              "lastsubid": ""
            }
         */
    fun post(): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl

        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", app)
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("device", "android")
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("count", "50")
            jsonObject.put("lasttime", "")
            jsonObject.put("lastsubid", "")
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(body).build()

            val response: Response = client.newCall(request).execute();
            if (response.isSuccessful) {

                val json:String = response.body!!.string()
                lastJson = json
                val mJsonResult = JSONObject(json)
                //Log.e(TAG, "mJsonResult:$mJsonResult" )
                //Tools.logE(TAG,"mJsonResult:$mJsonResult")

                status = mJsonResult.getString("status")


                return status
            }else{
                Log.e(TAG, "postRequest: false" )
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
        return status
    }

    fun infoJson(): JsonClass {
        val gson = Gson()
        val mJson: JsonClass =
            gson.fromJson(lastJson, JsonClass::class.java)
        return mJson
    }

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


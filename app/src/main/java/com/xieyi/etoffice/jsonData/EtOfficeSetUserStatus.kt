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

    val TAG = javaClass.simpleName
    var lastJson: String = ""
    val app: String = "EtOfficeSetUserStatus"

    fun post(
        longitude:Double,
        latitude:Double,
        location:String,
         statusvalue:String,
        statustext:String
         ,memo:String
    ): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl

        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", app)
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", "android")
            jsonObject.put("statusvalue", statusvalue)
            jsonObject.put("statustext", statustext)
            jsonObject.put("location", location)
            jsonObject.put("longitude", longitude.toString())
            jsonObject.put("latitude", latitude.toString())
            jsonObject.put("memo", memo)
            Log.e(TAG, "post: jsonObject:$jsonObject")

            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(body).build()

            val response: Response = client.newCall(request).execute();
            if(response.isSuccessful){

                val json:String = response.body!!.string()
                lastJson = json
                val mJsonResult = JSONObject(json)
                Log.e(TAG, "mJsonResult:$mJsonResult" )

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


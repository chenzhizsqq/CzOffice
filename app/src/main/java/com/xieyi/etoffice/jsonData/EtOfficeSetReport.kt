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


//EtOfficeSetReport 日報登録
class EtOfficeSetReport {

    private val TAG = "EtOfficeSetReport"
    private var lastJson: String = ""
    val app: String = "EtOfficeSetReport"

    fun post(): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl

        /*
        {"app":"EtOfficeSetReport",
        "token":"202011291352391050000000090010000000000000010125",
        "device":"ios",
        "tenant":"1",
        "hpid":"8",
        "userid":"2025",
        "ymd":"20210305",
        "projectcd":"ETHP",
        "wbscd":"E202103",
        "totaltime":"0800"}
         */
        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", app)
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("device", "android")
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("userid", JC.pEtOfficeLogin.infoLoginResult().userid)
            jsonObject.put("ymd", "20210305")
            jsonObject.put("projectcd", "ETHP")
            jsonObject.put("wbscd", "E202103")
            jsonObject.put("totaltime", "0800")
            Log.e(TAG, jsonObject.toString(), )

            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(body).build()

            val response: Response? = client.newCall(request).execute();
            if (response != null) {
                if (response.isSuccessful) {

                    var json: String = response.body!!.string()
                    lastJson = json
                    var mJsonResult = JSONObject(json)
                    Log.e(TAG, "mJsonResult: :$mJsonResult" )

                    status = mJsonResult.getString("status")


                    return status
                }else{
                    Log.e(TAG, "postRequest: false" )
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.toString())
        }
        return status
    }

    fun infoJson(): JsonClass? {
        try {
            val gson = Gson()
            return gson.fromJson(lastJson, JsonClass::class.java)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return null
    }

    data class JsonClass(
        val message: String,
        val result: String,
        val status: Int
    )
}

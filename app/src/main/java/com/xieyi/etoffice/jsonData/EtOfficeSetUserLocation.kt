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

    val TAG = javaClass.simpleName
    var lastJson: String = ""
    val app: String = "EtOfficeSetUserLocation"

    fun post(longitude: String, latitude:String, location:String): String {
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
//                jsonObject.put("longitude", "140.00468200000000")
//                jsonObject.put("latitude", "35.70148346348169")
            //jsonObject.put("location", "船橋事務所")
            jsonObject.put("longitude", longitude)
            jsonObject.put("latitude", latitude)
            jsonObject.put("location", location)
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
                Log.e(TAG, "postRequest: false")
            }
        } catch (e: Exception) {
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
        val locationlist: List<EtOfficeSetUserLocationlist>
    )
    data class EtOfficeSetUserLocationlist(
        val latitude: String,
        val location: String,
        val longitude: String
    )
}


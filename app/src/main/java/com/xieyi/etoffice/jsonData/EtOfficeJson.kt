package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//EtOfficeJson
class EtOfficeJson {

    val TAG = "EtOfficeJson"
    var lastJson:String = ""
    val app:String = "EtOfficeJson"

    fun post(): String {
        var status:String = "-1"
        val jsonObject = JSONObject()
        jsonObject.put("app", EtOfficeUserInfo.app)
        jsonObject.put("token", EtOfficeLogin.infoLoginResult().token)
        jsonObject.put("tenant", EtOfficeLogin.infoLoginResult().tenantid)
        jsonObject.put("hpid", EtOfficeLogin.infoLoginResult().hpid)
        jsonObject.put("device","android")
        status = post(jsonObject)
        return status
    }

    fun post(jsonObject:JSONObject): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {
                val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response? = client.newCall(request).execute();
                if (response != null) {
                    if(response.isSuccessful){

                        var json:String = response.body!!.string()
                        lastJson = json
                        var mJsonResult = JSONObject(json)
                        Log.e(TAG, "mJsonResult:$mJsonResult" )

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


    fun getResult(): Any {
        val gson = Gson()
        val mJson : Json = gson.fromJson(lastJson, Json::class.java)
        return mJson.result
    }
    fun getJson(): Json {
        val gson = Gson()
        return gson.fromJson(lastJson, Json::class.java)
    }

    data class Json(
        val message: String,
        val result: Any,
        val status: Int
    )

}


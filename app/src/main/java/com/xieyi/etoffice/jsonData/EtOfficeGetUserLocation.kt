package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//EtOfficeGetUserLocation   ユーザー勤務場所一覧取得
class EtOfficeGetUserLocation {

    companion object {
        val TAG = "EtOfficeGetUserLocation"
        var lastJson:String = ""
        const val app:String = "EtOfficeGetUserLocation"

        /*
            {
              "app": "EtOfficeGetUserLocation",
              "token": "202011291352391050000000090010000000000000010125",
              "tenant": "1",
              "hpid": "6",
              "device": "android"
            }
         */
        fun post(): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {
                val jsonObject = JSONObject()
                jsonObject.put("app", app)
                jsonObject.put("token", EtOfficeLogin.infoLoginResult().token)
                jsonObject.put("tenant",EtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid", EtOfficeLogin.infoLoginResult().hpid)
                jsonObject.put("device","android")
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


        fun infoUserstatuslist(index:Int): Locationlist {
            val gson = Gson()
            val mJson : EtOfficeGetUserLocationJson = gson.fromJson(lastJson, EtOfficeGetUserLocationJson::class.java)
            return mJson.result.locationlist[index]
        }
    }


    data class EtOfficeGetUserLocationJson(
        val message: String,
        val result: EtOfficeGetUserLocationResult,
        val status: Int
    )

    data class EtOfficeGetUserLocationResult(
        val locationlist: List<Locationlist>
    )

    data class Locationlist(
        val latitude: String,
        val location: String,
        val longitude: String
    )
}


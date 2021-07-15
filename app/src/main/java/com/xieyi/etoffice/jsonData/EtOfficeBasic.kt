package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject


//EtOfficeBasic
open class EtOfficeBasic {

    private val TAG = "EtOfficeBasic"
    open var lastJson: String = ""
    open val app: String = "EtOfficeBasic"

    fun post(jsonObject : JSONObject): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl



        try {
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

}

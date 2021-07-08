package com.example.etoffice

import android.util.Log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class JsonCenter {

    companion object {
        val TAG = "JsonCenter"
        var lastLoginResultJson:String = ""

        //ログイン画面で、登録
        fun loginPost(uid:String,password:String): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeLogin")
            jsonObject.put("uid", uid)
            jsonObject.put("password", password)
            jsonObject.put("registrationid", "pass")
            jsonObject.put("device","android")
            val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(body).build()

            val response: Response? = client.newCall(request).execute();
            if (response != null) {
                if(response.isSuccessful){

                    var getResult:String = response.body!!.string()
                    lastLoginResultJson = getResult
                    var mJsonResult = JSONObject(getResult)

                    val status = mJsonResult.getString("status")
                    Log.e(TAG, "status:$status" )

                    val token = mJsonResult.getJSONObject("result").getString("token")
                    Log.e(TAG, "token:$token" )

                    Log.e(TAG, "postRequest: mJsonObjLogin:$mJsonResult" )
                    return status
                }else{
                    Log.e(TAG, "postRequest: false" )
                }
            }
            return status
        }


        //ログイン画面で、ログイン後の特定記録
        fun loginResult(select:String):String {
            val mJsonResult = JSONObject(lastLoginResultJson)
            val r = mJsonResult.getJSONObject("result").getString(select)
            return r
        }
    }





}
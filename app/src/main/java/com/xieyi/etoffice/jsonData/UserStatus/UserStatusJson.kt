package com.xieyi.etoffice.jsonData.UserStatus

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.Gson.GetUserStatus.GetUserStatusJson
import com.xieyi.etoffice.Gson.GetUserStatus.Userstatuslist
import com.xieyi.etoffice.jsonData.EtOfficeLogin
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

//ユーザー最新勤務状態の一覧取得
class UserStatusJson {

    companion object {
        val TAG = "UserStatusJson"
        var lastJson:String = ""




        /*
        {"app":"EtOfficeGetUserStatus"
        , "token":"202011291352391050000000090010000000000000010125"
        ,"tenant":"1"
        , "hpid":"6"
        , "device":"android"}
         */
        fun post(): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {
                val jsonObject = JSONObject()
                jsonObject.put("app", "EtOfficeGetUserStatus")
                jsonObject.put("token", EtOfficeLogin.token)
                jsonObject.put("tenant",EtOfficeLogin.tenantid)
                jsonObject.put("hpid", EtOfficeLogin.hpid)
                jsonObject.put("device","android")
                val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response? = client.newCall(request).execute();
                if (response != null) {
                    if(response.isSuccessful){

                        var json:String = response.body!!.string()
                        lastJson = json
                        var mJsonResult = JSONObject(json)
                        Log.e(TAG, "postRequest: userStatusPost:$mJsonResult" )

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

        fun infoUserStatusList(index:Int): Userstatuslist {
            val gson = Gson()
            val mGetUserStatusJson : GetUserStatusJson = gson.fromJson(lastJson, GetUserStatusJson::class.java)
            return mGetUserStatusJson.result.userstatuslist[index]
        }
    }





}
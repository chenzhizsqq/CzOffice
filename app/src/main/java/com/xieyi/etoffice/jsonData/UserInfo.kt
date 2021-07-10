package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//EtOfficeGetUserStatus ユーザー最新勤務状態の一覧取得
class UserInfo {

    companion object {
        private const val TAG = "UserInfo"
        private var lastJson:String = ""
        const val app:String = "EtOfficeUserInfo"

        /*
        {"app":"EtOfficeUserInfo"
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
                jsonObject.put("app", app)
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

        //result    一覧
        fun infoUserStatusList(): Result {
            val gson = Gson()
            val mUserInfoJson : UserInfoJson = gson.fromJson(lastJson, UserInfoJson::class.java)
            return mUserInfoJson.result
        }
    }

    data class UserInfoJson(
        val message: String,
        val result: Result,
        val status: Int
    )

    data class Result(
        val userid: String,     //ユーザー識別ID
        val usercode: String,   //社員コード
        val username: String,    //username
        val userkana: String,   //userkana
        val mail: String,       //mail
        val phone: String,      //phone
    )


}
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


//EtOfficeUserInfo ユーザー情報取得
class EtOfficeUserInfo {

        private val TAG = "EtOfficeUserInfo"
    private var lastJson: String = ""
    val app: String = "EtOfficeUserInfo"

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
                jsonObject.put("token", EtOfficeApp.Token)
                jsonObject.put("tenant", EtOfficeApp.TenantId)
                jsonObject.put("hpid", EtOfficeApp.HpId)
                jsonObject.put("device", "android")
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

        //result    一覧
        fun infoUserStatusList(): Result? {
            try {

                val gson = Gson()
                val mUserInfoJson: JsonClass = gson.fromJson(lastJson, JsonClass::class.java)
                return mUserInfoJson.result
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return null
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
        val userid: String,     //ユーザー識別ID
        val usercode: String,   //社員コード
        val username: String,    //username
        val userkana: String,   //userkana
        val mail: String,       //mail
        val phone: String,      //phone
    )


}

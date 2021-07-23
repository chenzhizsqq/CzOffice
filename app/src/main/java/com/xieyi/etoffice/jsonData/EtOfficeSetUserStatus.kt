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


//EtOfficeSetUserStatus ユーザー勤務状態の設定
class EtOfficeSetUserStatus {

        val TAG = "EtOfficeSetUserStatus"
        var lastJson: String = ""
    val app: String = "EtOfficeSetUserStatus"


        fun post(): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {
                /*
                    {
                      "app": "EtOfficeSetUserStatus",
                    }
                 */
                val jsonObject = JSONObject()
                jsonObject.put("app", app)
                jsonObject.put("token", JC.pEtOfficeLogin.infoLoginResult().token)
                jsonObject.put("tenant", JC.pEtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid", JC.pEtOfficeLogin.infoLoginResult().hpid)
                jsonObject.put("device", "android")
                jsonObject.put("longitude", "140.00468200000000")
                jsonObject.put("latitude", "35.70148346348169")
                jsonObject.put("location", "船橋事務所")
                val body = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

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

        //userstatuslist    一覧
        fun infoUserStatusList(index: Int): Userstatuslist? {
            try {

                val gson = Gson()
                val mEtOfficeGetUserStatusJson: JsonClass =
                    gson.fromJson(lastJson, JsonClass::class.java)
                return mEtOfficeGetUserStatusJson.result.userstatuslist[index]
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


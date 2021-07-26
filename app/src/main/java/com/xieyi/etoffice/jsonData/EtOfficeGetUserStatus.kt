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


//EtOfficeGetUserStatus ユーザー最新勤務状態の一覧取得
class EtOfficeGetUserStatus {

        val TAG = "EtOfficeGetUserStatus"
        var lastJson: String = ""
    val app: String = "EtOfficeGetUserStatus"

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
                jsonObject.put("app", app)
                jsonObject.put("token", JC.pEtOfficeLogin.infoLoginResult().token)
                jsonObject.put("tenant", JC.pEtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid", JC.pEtOfficeLogin.infoLoginResult().hpid)
//                jsonObject.put("tenant", "3")
//                jsonObject.put("hpid", "6")
                jsonObject.put("device", "android")
                //Log.e(TAG, "jsonObject:$jsonObject")
                val body = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response? = client.newCall(request).execute();
                if (response != null) {
                    if (response.isSuccessful) {

                        val json:String = response.body!!.string()
                        lastJson = json
                        val mJsonResult = JSONObject(json)
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
                val mGetUserStatusJson: JsonClass =
                    gson.fromJson(lastJson, JsonClass::class.java)
                return mGetUserStatusJson.result.userstatuslist[index]
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return null
        }

        fun infoUserStatusList(): List<Userstatuslist> {
            val gson = Gson()
            val mGetUserStatusJson : JsonClass = gson.fromJson(lastJson, JsonClass::class.java)
            return mGetUserStatusJson.result.userstatuslist
        }

        //userstatuslist    一覧
        fun infoUserStatusListCount(): Int {
            val gson = Gson()
            val mGetUserStatusJson : JsonClass = gson.fromJson(lastJson, JsonClass::class.java)
            return mGetUserStatusJson.result.userstatuslist.count()
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
        val userid: String,         //ユーザー識別ID
        val usercode: String,       //社員コード
        val username: String,       //氏名
        val userkana: String,       //カナ
        val location: String,       //最新勤務状態更新場所
        val memo: String,           //備考
        val statustext: String,     //最新勤務状態文字列
        val statustime: String,     //最新勤務状態更新時刻
        val statusvalue: String,    //最新勤務状態値
    )

}


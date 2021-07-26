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


//EtOfficeLogin ログイン処理、ユーザー情報取得
class EtOfficeLogin {

    val TAG = "EtOfficeLogin"

    //入力後、取得した結果
    var lastJson: String = ""


    val app: String = "EtOfficeLogin"

    /*
    {"app":"EtOfficeGetUserStatus"
    , "token":"202011291352391050000000090010000000000000010125"
    ,"tenant":"1"
    , "hpid":"6"
    , "device":"android"}
     */
    //入力
    fun post(uid: String, password: String): String {
        var status: String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url: String = Config.LoginUrl

        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeLogin")
            jsonObject.put("uid", uid)
            jsonObject.put("password", password)
            jsonObject.put("registrationid", "pass")
            jsonObject.put("device", "android")
            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(body).build()

            val response: Response? = client.newCall(request).execute()
            if (response != null) {
                if (response.isSuccessful) {

                    val json: String = response.body!!.string()
                    lastJson = json
                    /*{
                          "status": 0,
                          "result": {
                            "token": "202107121212507840000000090010001502491490940935",
                            "tenantid": "3",
                            "hpid": "6",
                            "userid": "9001",
                            "usercode": "demo1",
                            "username": "ユーザー１",
                            "userkana": "カタカナ１",
                            "mail": "demo1@xieyi.co.jp",
                            "phone": "07473626478"
                          },
                          "message": ""
                        }
                     */

                    val mJsonResult = JSONObject(json)
                    Log.e(TAG, "mJsonResult:$mJsonResult")

                    status = mJsonResult.getString("status")


                    return status
                } else {
                    Log.e(TAG, "postRequest: false")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return status
    }

    //出力    result
    fun infoLoginResult(): LoginResult {
        val gson = Gson()
        val mJsonClass: JsonClass = gson.fromJson(lastJson, JsonClass::class.java)
        return mJsonClass.result
    }

    fun infoJson(): JsonClass? {
        try {
            val gson = Gson()
            val mJsonClass: JsonClass =
                gson.fromJson(lastJson, JsonClass::class.java)


            if(Config.isTest) {
//            "tenantid": "3",
//            "hpid": "6",
                mJsonClass.result.tenantid = "3"
                mJsonClass.result.hpid = "6"
            }
            return mJsonClass
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
        return null
    }

    data class LoginResult(
        var hpid: String,
        val mail: String,
        val phone: String,
        var tenantid: String,
        val token: String,
        val usercode: String,
        val userid: String,
        val userkana: String,
        val username: String
    )

    data class JsonClass(
        val message: String,
        val result: LoginResult,
        val status: Int
    )

}


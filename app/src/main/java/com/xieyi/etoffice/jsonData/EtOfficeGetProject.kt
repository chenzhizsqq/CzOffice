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
import javax.security.auth.login.LoginException


//EtOfficeGetProject プロジェクト一覧
class EtOfficeGetProject {

    private val TAG = "EtOfficeGetProject"
    private var lastJson: String = ""
    val app: String = "EtOfficeGetProject"

    fun post(): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl

        /*
        {"app":"EtOfficeGetProject"
        ,"token":"202011291352391050000000090010000000000000010125"
        ,"device":"ios"
        ,"tenant":"1"
        ,"hpid":"8"
        ,"userid":"2025"
        ,"ymd":"20210305"}
         */
        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", app)
            jsonObject.put("token", JC.pEtOfficeLogin.infoLoginResult().token)
            jsonObject.put("device", "android")
            jsonObject.put("tenant", JC.tenantid)
            jsonObject.put("hpid", JC.hpid)
            jsonObject.put("userid", JC.pEtOfficeLogin.infoLoginResult().userid)
            jsonObject.put("ymd", "20210305")
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

    fun infoResult(): Result? {
        try {
            val gson = Gson()
            val mJson: JsonClass =
                gson.fromJson(lastJson, JsonClass::class.java)
            return mJson.result
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

    data class Projectlist(
        val projectcd: String,
        val projectname: String,
        val wbslist: List<Wbslist>
    )

    data class Result(
        val projectlist: List<Projectlist>
    )

    data class Wbslist(
        val date: String,
        val time: String,
        val wbscd: String,
        val wbsname: String
    )
}

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


//EtOfficeGetReportInfo 日報詳細取得
class EtOfficeGetReportInfo {

    private val TAG = "EtOfficeGetReportInfo"
    private var lastJson: String = ""
    val app: String = "EtOfficeGetReportInfo"

    fun post(): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl

        /*
        {"app":"EtOfficeGetReportInfo"
        ,"token":"202103111358234640000000020250001921680001650132"
        , "device":"ios"
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
            jsonObject.put("tenant", JC.pEtOfficeLogin.infoLoginResult().tenantid)
            jsonObject.put("hpid", JC.pEtOfficeLogin.infoLoginResult().hpid)
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

    data class Commentlist(
        val comment: String,
        val time: String,
        val username: String
    )

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )

    data class Planworklist(
        val date: String,
        val project: String,
        val time: String,
        val wbs: String
    )

    data class Reportlist(
        val memo: String,
        val project: String,
        val time: String,
        val wbs: String
    )

    data class Result(
        val authflag: String,
        val commentlist: List<Commentlist>,
        val planworklist: List<Planworklist>,
        val planworktime: String,
        val reportlist: List<Reportlist>,
        val workstatuslist: List<Workstatuslist>,
        val worktime: String
    )

    data class Workstatuslist(
        val status: String,
        val time: String
    )

}

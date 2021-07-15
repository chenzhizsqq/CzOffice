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


//EtOfficeSetApprovalJsk 勤務実績承認
class EtOfficeSetApprovalJsk {

    private val TAG = "EtOfficeSetApprovalJsk"
    private var lastJson: String = ""
    val app: String = "EtOfficeSetApprovalJsk"

    fun post(): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl

        /*
        {
          "app": "EtOfficeSetApprovalJsk",
          "token": "202107141803570090000000090010001502491490940307",
          "device": "android",
          "tenant": "3",
          "hpid": "6",
          "userid": "9001",
          "updateymd": ["20210301","20210302"]
        }
         */
        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", app)
            jsonObject.put("token", JC.pEtOfficeLogin.infoLoginResult().token)
            jsonObject.put("tenant", JC.pEtOfficeLogin.infoLoginResult().tenantid)
            jsonObject.put("hpid", JC.pEtOfficeLogin.infoLoginResult().hpid)
            jsonObject.put("device", "android")
            jsonObject.put("userid", "9001")
            jsonObject.put("updateymd", "[\"20210301\",\"20210302\"]")
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

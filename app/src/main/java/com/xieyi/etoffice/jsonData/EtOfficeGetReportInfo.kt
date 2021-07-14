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


//EtOfficeGetReportInfo   日報詳細取得
class EtOfficeGetReportInfo: EtOfficeJson() {

    companion object {
        val TAG = "EtOfficeGetReportInfo"

        /*
           {"app":"EtOfficeGetReportInfo",
           "token":"202103111358234640000000020250001921680001650132",
            "device":"ios",
            "tenant":"1",
            "hpid":"8",
            "userid":"2025",
            "ymd":"20210305"}
         */
        fun post(): String {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetReportInfo")
            jsonObject.put("token", EtOfficeLogin.infoLoginResult().token)
            jsonObject.put("device","android")
            jsonObject.put("tenant",EtOfficeLogin.infoLoginResult().tenantid)
            jsonObject.put("hpid",EtOfficeLogin.infoLoginResult().hpid)
            jsonObject.put("userid",EtOfficeLogin.infoLoginResult().userid)
            jsonObject.put("updateymd","20210305")
            Log.e(TAG,jsonObject.toString())
            return post(jsonObject)
        }
    }


}


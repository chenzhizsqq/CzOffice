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


//EtOfficeGetReportList   日報一覧取得
class EtOfficeGetReportList: EtOfficeJson() {

    companion object {
        val TAG = "EtOfficeGetReportList"

        /*
            {"app":"EtOfficeGetReportList"
            ,"token":"202107141727590980000000090010001502491490940587"
            ,"device":"android"
            ,"tenant":"3"
            ,"hpid":"6"
            ,"startym":""
            ,"months":""}
         */
        fun post(): String {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetReportList")
            jsonObject.put("token", EtOfficeLogin.infoLoginResult().token)
            jsonObject.put("device","android")
            jsonObject.put("tenant",EtOfficeLogin.infoLoginResult().tenantid)
            jsonObject.put("hpid",EtOfficeLogin.infoLoginResult().hpid)
            jsonObject.put("startym","")
            jsonObject.put("months","")
            Log.e(TAG,jsonObject.toString())
            return post(jsonObject)
        }
    }


}


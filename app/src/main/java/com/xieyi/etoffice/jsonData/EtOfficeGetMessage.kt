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


//EtOfficeGetMessage   最新メッセージ一覧取得
class EtOfficeGetMessage {

        val TAG = "EtOfficeGetMessage"
        var lastJson: String = ""
    val app: String = "EtOfficeGetMessage"

        /*
            {
              "app": "EtOfficeGetMessage",
              "token": "202011291352391050000000090010000000000000010125",
              "device": "android",
              "tenant": "1",
              "hpid": "8",
              "count": "50",
              "lasttime": "",
              "lastsubid": ""
            }
         */
        fun post(): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {
                val jsonObject = JSONObject()
                jsonObject.put("app", app)
                jsonObject.put("token", jsonCenter.pEtOfficeLogin.infoLoginResult().token)
                jsonObject.put("device", "android")
                jsonObject.put("tenant", jsonCenter.pEtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid", jsonCenter.pEtOfficeLogin.infoLoginResult().hpid)
                jsonObject.put("count", "50")
                jsonObject.put("lasttime", "")
                jsonObject.put("lastsubid", "")
                val body = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response? = client.newCall(request).execute();
                if (response != null) {
                    if (response.isSuccessful) {

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



//        fun infoTenantList(index:Int): Tenantlist {
//            val gson = Gson()
//            val mJson : EtOfficeGetTenantJson = gson.fromJson(lastJson, EtOfficeGetTenantJson::class.java)
//            return mJson.result.tenantlist[index]
//        }


        /*
        {
          "status": 0,
          "result": {
            "recordlist": [
              {
                "statustime": "20210712181908",
                "statusvalue": "",
                "statustext": "",
                "memo": ""
              },
              {
                "statustime": "20210712181104",
                "statusvalue": "",
                "statustext": "",
                "memo": ""
              }
            ],
            "messagelist": []
          },
          "message": ""
        }
         */


        fun infoRecordlist(index: Int): Recordlist? {
            try {
                val gson = Gson()
                val mJson: EtOfficeGetMessageJson =
                    gson.fromJson(lastJson, EtOfficeGetMessageJson::class.java)
                return mJson.result.recordlist[index]
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return null
        }

        fun getResult(): EtOfficeGetMessageResult? {
            try {
                val gson = Gson()
                val mJson: EtOfficeGetMessageJson =
                    gson.fromJson(lastJson, EtOfficeGetMessageJson::class.java)
                return mJson.result
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return null
        }


    data class Recordlist(
        val memo: String,
        val statustext: String,
        val statustime: String,
        val statusvalue: String
    )
    data class EtOfficeGetMessageResult(
        val messagelist: List<Any>,
        val recordlist: List<Recordlist>
    )
    data class EtOfficeGetMessageJson(
        val message: String,
        val result: EtOfficeGetMessageResult,
        val status: Int
    )

}


package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.Tools
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//EtOfficeSetMessage   最新メッセージ一覧set
class EtOfficeSetMessage {

    companion object {
        val TAG = "EtOfficeSetMessage"
        var lastJson:String = ""
        const val app:String = "EtOfficeSetMessage"

        /*
            {
              "app": "EtOfficeSetMessage",
              "token": "202011291352391050000000090010000000000000010125",
              "device": "ios",
              "tenant": "1",
              "hpid": "8",
              "count": "50",
              "updateid": [
                "2020111320070768",
                "2020111320065968",
                "2020111319510968"
              ],
              "readflg": "1"
            }
         */
        fun post(arrayString: Array<String>): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            var updateid:String = Tools.jsonArray2String(arrayString)

            try {
                val jsonObject = JSONObject()
                jsonObject.put("app", app)
                jsonObject.put("token", EtOfficeLogin.infoLoginResult().token)
                jsonObject.put("device","android")
                jsonObject.put("tenant",EtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid",EtOfficeLogin.infoLoginResult().hpid)
                jsonObject.put("count","50")
                jsonObject.put("updateid",updateid)
                jsonObject.put("readflg","1")
                Log.e(TAG, "post: jsonObject:"+jsonObject )
                val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

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

        fun getResult(): String {
            val gson = Gson()
            val mJson : EtOfficeGetMessageJson = gson.fromJson(lastJson, EtOfficeGetMessageJson::class.java)
            return mJson.result
        }
    }

    data class EtOfficeGetMessageJson(
        val message: String,
        val result: String,
        val status: Int
    )

}


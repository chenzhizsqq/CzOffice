package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.Tools
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
                jsonObject.put("token", JC.pEtOfficeLogin.infoLoginResult().token)
                jsonObject.put("device", "android")
                jsonObject.put("tenant", JC.tenantid)
                jsonObject.put("hpid", JC.hpid)
                jsonObject.put("count", "50")
                jsonObject.put("lasttime", "")
                jsonObject.put("lastsubid", "")
                val body = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response? = client.newCall(request).execute();
                if (response != null) {
                    if (response.isSuccessful) {

                        val json:String = response.body!!.string()
                        lastJson = json
                        val mJsonResult = JSONObject(json)
                        //Log.e(TAG, "mJsonResult:$mJsonResult" )
                        //Tools.logE(TAG,"mJsonResult:$mJsonResult")

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
        "statustime": "20210727102010",
        "statusvalue": "3",
        "statustext": "休憩中",
        "memo": ""
      },
      {
        "statustime": "20210727101943",
        "statusvalue": "1",
        "statustext": "勤務中",
        "memo": ""
      }
    ],
    "messagelist": [
      {
        "title": "勤務実績変更",
        "content": "写易花子さんが202107の勤務実績「通常勤務」09:00-18:00を一括変更しました",
        "updatetime": "20210720181659",
        "subid": "1"
      },
      {
        "title": "勤務実績登録",
        "content": "写易花子さんが20210720の勤務実績「通常勤務」09:00-18:00を登録しました",
        "updatetime": "20210720180628",
        "subid": "1"
      },

      {
        "title": "勤務実績変更",
        "content": "写易花子さんが202106の勤務実績「通常勤務」09:00-18:00を一括変更しました",
        "updatetime": "20210716081659",
        "subid": "1"
      }
    ]
  },
  "message": ""
}
         */





    fun infoJson(): JsonClass {
        val gson = Gson()
        val mJson: JsonClass =
            gson.fromJson(lastJson, JsonClass::class.java)
        return mJson
    }

    data class Messagelist(
        val content: String,
        val subid: String,
        val title: String,
        val updatetime: String
    )

    data class Recordlist(
        val memo: String,
        val statustext: String,
        val statustime: String,
        val statusvalue: String
    )

    data class Result(
        val messagelist: List<Messagelist>,
        val recordlist: List<Recordlist>
    )

    data class JsonClass(
        val message: String,
        val result: Result,
        val status: Int
    )
}


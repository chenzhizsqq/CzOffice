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


//EtOfficeGetStuffList   社員一覧取得
class EtOfficeGetStuffList {

        val TAG = "EtOfficeGetStuffList"
        var lastJson: String = ""
    val app: String = "EtOfficeGetStuffList"

        /*
            {
                "app":"EtOfficeGetStuffList",
                 "token":"202011291352391050000000090010000000000000010125",
                 "tenant":  ,
                 "hpid":  ,
                 "device":  ,

         */
        fun post(): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {
                val jsonObject = JSONObject()
                jsonObject.put("app", app)
                jsonObject.put("token", JC.pEtOfficeLogin.infoLoginResult().token)
//                jsonObject.put("tenant", JC.pEtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("tenant", "3")
//                jsonObject.put("hpid", JC.pEtOfficeLogin.infoLoginResult().hpid)
                jsonObject.put("hpid", "6")
                jsonObject.put("device", "android")
                //Log.e(TAG, "!!!! jsonObject:$jsonObject" )
                val body = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response? = client.newCall(request).execute();
                if (response != null) {
                    if (response.isSuccessful) {

                        var json: String = response.body!!.string()
                        lastJson = json
                        var mJsonResult = JSONObject(json)
                        //Log.e(TAG, "!!!! mJsonResult:$mJsonResult" )

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
        {"status":0,"result":{"sectionlist":[]},"message":""}
         */
    /*
    {
      "status": 0,
      "result": {
        "sectionlist": [
          {
            "sectioncd": "SYS",
            "sectionname": "システムサービス部",
            "stufflist": [
              {
                "tenant": "3",
                "hpid": "6",
                "userid": "9003",
                "username": "ユーザー３",
                "userkana": "カタカナ３",
                "phone": "05884885890",
                "mail": "demo3@xieyi.co.jp"
              },
              {
                "tenant": "3",
                "hpid": "6",
                "userid": "9004",
                "username": "ユーザー４",
                "userkana": "カタカナ４",
                "phone": "85756737734",
                "mail": "demo4@xieyi.co.jp"
              }
            ]
          },
          {
            "sectioncd": "INF",
            "sectionname": "インフラサービス部",
            "stufflist": [
              {
                "tenant": "3",
                "hpid": "6",
                "userid": "9002",
                "username": "ユーザー２",
                "userkana": "カタカナ２",
                "phone": "09684773899",
                "mail": "demo2@xieyi.co.jp"
              }
            ]
          },
          {
            "sectioncd": "GAD",
            "sectionname": "業務統括部",
            "stufflist": [
              {
                "tenant": "3",
                "hpid": "6",
                "userid": "9005",
                "username": "ユーザー５",
                "userkana": "カタカナ５",
                "phone": "90487578484",
                "mail": "demo5@xieyi.co.jp"
              }
            ]
          },
          {
            "sectioncd": "EXO",
            "sectionname": "役員",
            "stufflist": [
              {
                "tenant": "3",
                "hpid": "6",
                "userid": "9001",
                "username": "ユーザー１",
                "userkana": "カタカナ１",
                "phone": "07473626478",
                "mail": "demo1@xieyi.co.jp"
              }
            ]
          }
        ]
      },
      "message": ""
    }
     */


    fun getResult(): Result? {
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

    data class Result(
        val sectionlist: List<SectionList>
    )

    data class SectionList(
        val sectioncd: String,
        val sectionname: String,
        var stufflist:List<StuffList>
    )

    data class StuffList(
        val tenant: String,
        val hpid: String,
        val userid: String,
        val username: String,
        val userkana: String,
        val phone: String,
        val mail: String,
    )

}


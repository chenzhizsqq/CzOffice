package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//EtOfficeGetTenant   所属会社一覧
class EtOfficeGetTenant {

    companion object {
        val TAG = "EtOfficeGetTenant"
        var lastJson:String = ""
        const val app:String = "EtOfficeGetTenant"

        /*
            {
              "app": "EtOfficeGetTenant",
              "token": "202011291352391050000000090010000000000000010125",
              "device": "ios"
            }
         */
        fun post(): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {
                val jsonObject = JSONObject()
                jsonObject.put("app", app)
                jsonObject.put("token", EtOfficeLogin.infoLoginResult().token)
                jsonObject.put("device","android")
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
            "tenantlist": [
              {
                "tenantid": "3",
                "startflg": "1",
                "tenantname": "株式会社テスト3",
                "hpid": "6",
                "posturl": "https:\/\/ssl.ethp.net\/EthpPost.aspx"
              },
              {
                "tenantid": "1",
                "startflg": "",
                "tenantname": "株式会社写易",
                "hpid": "8",
                "posturl": "https:\/\/ssl.ethp.net\/EthpPost.aspx"
              }
            ]
          },
          "message": ""
        }
         */
//        fun infoUserstatuslist(index:Int): Locationlist {
//            val gson = Gson()
//            val mJson : EtOfficeGetUserLocationJson = gson.fromJson(lastJson, EtOfficeGetUserLocationJson::class.java)
//            return mJson.result.locationlist[index]
//        }


        fun infoTenantList(index:Int): Tenantlist {
            val gson = Gson()
            val mJson : EtOfficeGetTenantJson = gson.fromJson(lastJson, EtOfficeGetTenantJson::class.java)
            return mJson.result.tenantlist[index]
        }
    }


    data class EtOfficeGetTenantJson(
        val message: String,
        val result: EtOfficeGetTenantJsonResult,
        val status: Int
    )
    data class EtOfficeGetTenantJsonResult(
        val tenantlist: List<Tenantlist>
    )
    data class Tenantlist(
        val hpid: String,
        val posturl: String,
        val startflg: String,
        val tenantid: String,
        val tenantname: String
    )
}


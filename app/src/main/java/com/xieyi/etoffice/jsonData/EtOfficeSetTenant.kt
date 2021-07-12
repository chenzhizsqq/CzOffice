package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//EtOfficeSetTenant   起動会社設定登録
class EtOfficeSetTenant {

    companion object {
        val TAG = "EtOfficeSetTenant"
        var lastJson:String = ""
        const val app:String = "EtOfficeSetTenant"

        /*
            {
              "app": "EtOfficeSetTenant",
              "token": "202011291352391050000000090010000000000000010125",
              "device": "ios",
              "tenant": "1"
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
                jsonObject.put("tenantid","3")
                val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                Log.e(TAG, "post: $jsonObject", )

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



//        fun infoTenantList(index:Int): Tenantlist {
//            val gson = Gson()
//            val mJson : EtOfficeGetTenantJson = gson.fromJson(lastJson, EtOfficeGetTenantJson::class.java)
//            return mJson.result.tenantlist[index]
//        }
    }



}


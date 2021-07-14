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

    companion object {
        val TAG = "EtOfficeGetStuffList"
        var lastJson:String = ""
        const val app:String = "EtOfficeGetStuffList"

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
                jsonObject.put("token", EtOfficeLogin.infoLoginResult().token)
                jsonObject.put("tenant",EtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid",EtOfficeLogin.infoLoginResult().hpid)
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
        {"status":0,"result":{"sectionlist":[]},"message":""}
         */


        fun getResult(): Result? {
            try {
                val gson = Gson()
                val mJson: EtOfficeGetStuffListJson =
                    gson.fromJson(lastJson, EtOfficeGetStuffListJson::class.java)
                return mJson.result
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
            return null
        }
    }
    data class EtOfficeGetStuffListJson(
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


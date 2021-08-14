package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.Tools
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject


//EtOfficeSetMessage   最新メッセージ一覧set
class EtOfficeSetMessage {

    val TAG = javaClass.simpleName
    var lastJson: String = ""
    val app: String = "EtOfficeSetMessage"

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

            val updateid:String = Tools.jsonArray2String(arrayString)

            try {
                val jsonObject = JSONObject()
                jsonObject.put("app", app)
                jsonObject.put("token", EtOfficeApp.Token)
                jsonObject.put("device", "android")
                jsonObject.put("tenant", EtOfficeApp.TenantId)
                jsonObject.put("hpid", EtOfficeApp.HpId)
                jsonObject.put("count", "50")
                jsonObject.put("updateid", updateid)
                jsonObject.put("readflg", "1")
                Log.e(TAG, "post: jsonObject:" + jsonObject)
                val body = jsonObject.toString()
                    .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response = client.newCall(request).execute();
                if(response.isSuccessful){

                    val json:String = response.body!!.string()
                    lastJson = json
                    val mJsonResult = JSONObject(json)
                    Log.e(TAG, "mJsonResult:$mJsonResult" )

                    status = mJsonResult.getString("status")


                    return status
                }else{
                    Log.e(TAG, "postRequest: false" )
                }
            }catch (e: Exception){
                Log.e(TAG, e.toString())
            }
            return status
        }


    fun infoJson(): JsonClass {
        val gson = Gson()
        val mJson: JsonClass =
            gson.fromJson(lastJson, JsonClass::class.java)
        return mJson
    }

    data class JsonClass(
        val message: String,
        val result: String,
        val status: Int
    )

}


package com.xieyi.etoffice.jsonData

import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.Config
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


//EtOfficeSetUserLocation   ユーザー勤務場所設定
class EtOfficeSetUserLocation {

    companion object {
        val TAG = "EtOfficeSetUserLocation"
        var lastJson:String = ""
        const val app:String = "EtOfficeSetUserLocation"

        /*
            {
              "app": "EtOfficeSetUserLocation",
              "token": "202011291352391050000000090010000000000000010125",
              "tenant": "1",
              "hpid": "6",
              "device": "ios",
              "longitude": "140.00468200000000",
              "latitude": "35.70148346348169",
              "location": "船橋事務所"
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
                jsonObject.put("tenant",EtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid", EtOfficeLogin.infoLoginResult().hpid)
                jsonObject.put("device","android")
                jsonObject.put("longitude", "140.00468200000000")
                jsonObject.put("latitude", "35.70148346348169")
                jsonObject.put("location", "船橋事務所")
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


        fun infoLocationList(index:Int): EtOfficeSetUserLocationlist {
            val gson = Gson()
            val mJson : EtOfficeSetUserLocationJson = gson.fromJson(lastJson, EtOfficeSetUserLocationJson::class.java)
            return mJson.result.locationlist[index]
        }
    }
    data class EtOfficeSetUserLocationJson(
        val message: String,
        val result: EtOfficeSetUserLocationResult,
        val status: Int
    )
    data class EtOfficeSetUserLocationResult(
        val locationlist: List<EtOfficeSetUserLocationlist>
    )
    data class EtOfficeSetUserLocationlist(
        val latitude: String,
        val location: String,
        val longitude: String
    )
}


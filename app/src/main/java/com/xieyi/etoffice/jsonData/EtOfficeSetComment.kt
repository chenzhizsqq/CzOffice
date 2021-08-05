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
import javax.security.auth.login.LoginException


//EtOfficeSetComment    コメント登録
class EtOfficeSetComment {

    private val TAG = "EtOfficeSetComment"
    private var lastJson: String = ""
    val app: String = "EtOfficeSetComment"

    fun post(ymd:String,comment:String): String {
        var status:String = "-1"
        val client: OkHttpClient = OkHttpClient()
        val url:String = Config.LoginUrl

        /*
        app
        EtOfficeSetComment

        token
        共 仕様.入力引数.tokenを参照

        tenant
        共 仕様.入力引数.tenantを参照

        hpid
        共 仕様.入力引数.hpidを参照

        device
        共 仕様.入力引数.tokenを参照

        userid
        更新対象:ユーザーID

        ymd
        更新対象:年月日

        comment
        更新対象:コメント内容
         */
        try {
            val jsonObject = JSONObject()
            jsonObject.put("app", app)
            jsonObject.put("token", JC.pEtOfficeLogin.infoLoginResult().token)
            jsonObject.put("device", "android")
            jsonObject.put("tenant", JC.pEtOfficeLogin.infoLoginResult().tenantid)
            jsonObject.put("hpid", JC.pEtOfficeLogin.infoLoginResult().hpid)
            jsonObject.put("userid", JC.pEtOfficeLogin.infoLoginResult().userid)
//            jsonObject.put("ymd", "20210305")
//            jsonObject.put("comment", "comment  fdsafdsa")
            jsonObject.put("ymd", ymd)
            jsonObject.put("comment", comment)
            Log.e(TAG, jsonObject.toString(), )

            val body = jsonObject.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder().url(url).post(body).build()

            val response: Response = client.newCall(request).execute();
            if (response.isSuccessful) {

                val json: String = response.body!!.string()
                lastJson = json
                val mJsonResult = JSONObject(json)
                Log.e(TAG, "mJsonResult: :$mJsonResult" )

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
        val result: Result,
        val status: Int
    )

    data class Result(
        val commentlist: List<Commentlist>
    )

    data class Commentlist(
        val comment: String,
        val time: String,
        val username: String
    )
}

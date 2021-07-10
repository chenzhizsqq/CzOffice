package com.xieyi.etoffice

import android.util.Log
import com.xieyi.etoffice.jsonData.EtOfficeLogin
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class JsonCenter {

    companion object {
        val TAG = "JsonCenter"
        var lastLoginResultJson:String = ""

        /*
        {"app":"EtOfficeLogin"
        , "uid":"demo1@xieyi.co.jp"
        , "password":"pass"
        ,"registrationid":"190e35f7e0e0eaeef7f"
        ,"device":"android"}
         */
        //ログイン画面で、登録
        fun loginPost(uid:String,password:String): String {
            var status:String = "-1"
            val client: OkHttpClient = OkHttpClient()
            val url:String = Config.LoginUrl

            try {

                val jsonObject = JSONObject()
                jsonObject.put("app", "EtOfficeLogin")
                jsonObject.put("uid", uid)
                jsonObject.put("password", password)
                jsonObject.put("registrationid", "pass")
                jsonObject.put("device","android")
                val body = jsonObject.toString().toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

                val request = Request.Builder().url(url).post(body).build()

                val response: Response? = client.newCall(request).execute();
                if (response != null) {
                    if(response.isSuccessful){

                        var getResult:String = response.body!!.string()
                        lastLoginResultJson = getResult
                        var mJsonResult = JSONObject(getResult)

                        status = mJsonResult.getString("status")


                        EtOfficeLogin.token =  loginResult("token")
                        EtOfficeLogin.tenantid =  loginResult("tenantid")
                        EtOfficeLogin.hpid =  loginResult("hpid")
                        EtOfficeLogin.userid =  loginResult("userid")
                        EtOfficeLogin.usercode =  loginResult("usercode")
                        EtOfficeLogin.username =  loginResult("username")
                        EtOfficeLogin.userkana =  loginResult("userkana")
                        EtOfficeLogin.mail =  loginResult("mail")
                        EtOfficeLogin.phone =  loginResult("phone")
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
        出力
status      共仕様.処理結果ステータスを参照
result
    token       ログイン成功した場合、tokenを戻ります。
    tenantid    会社識別ID
    hpid        WEBサイト識別ID
    userid      ユーザー識別ID
    usercode    社員コード
    username    氏名
    userkana    カナ
    mail        メールアドレス
    phone       話番号
message     処理結果メッセージ
         */


        //ログイン画面で、ログイン後の特定記録
        fun loginResult(select:String):String {
            val mJsonResult = JSONObject(lastLoginResultJson)
            val r = mJsonResult.getJSONObject("result").getString(select)
            return r
        }



    }





}
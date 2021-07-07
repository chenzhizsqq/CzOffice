package com.xieyi.etoffice.common

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.xieyi.etoffice.enum.RequestMethod
import com.xieyi.etoffice.enum.ResultType
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class HttpUtil {

    companion object {

        // スプラッシュ表示時間（msec）
        const val SPLASH_TIME = 2000L

        // API接続タイムアウト（秒）
        const val API_CONNECT_TIMEOUT = 60L

        // API READタイムアウト（秒）
        const val API_READ_TIMEOUT = 60L

        // API WRITEタイムアウト（秒）
        const val API_WRITE_TIMEOUT = 60L

        // MediaType JSON定義
        private val JSON_TYPE = "application/json; charset=utf-8".toMediaTypeOrNull()


        /**
         * HTTP同期コール
         *
         * @param context:          Context
         * @param url:              URL
         * @param method:           リクエストメソッド
         * @param parameter:        パラメータ
         * @param imageParameter:   イメージパラメータ(ファイル名：ファイルパスのキーバリュー形式)
         *                          ファイルパスは絶対パス
         *                          Key-Value設定例: photo1:/data/user/0/cache/temp/aaa.jpg
         * @param body:             JSON BODY
         * @param header:           ヘッダへ設定するパラメータ情報
         * @param authToken:        認証トークン要否
         * @param fcmToken          FCMトークン要否
         * @param isHeader:         ヘッダ取得
         * @param classType:        クラスタイプ
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        fun callSyncHttp(
            context: Context,
            url: String,
            method: RequestMethod? = RequestMethod.POST,
            parameter: Any? = null,
            imageParameter: HashMap<String, String>? = null,
            body: String? = null,
            header: HashMap<String, String>? = null,
            isHeader: Boolean? = false,
            classType: Class<Any>? = null,
            onSuccess: onSuccess<Any>,
            onFailure: onFailure<ResultType, Any>
        ) {
            Log.d("callSyncHttp", url)

            val apiUrl = url
            val client = getHttpClient()
            val request = getRequest(
                context,
                apiUrl,
                method,
                parameter,
                imageParameter,
                body,
                header,
            )

            try {
                client.newCall(request).execute().use { response ->
                    // API呼出結果処理
                    handleResult(context, response, isHeader,
                        classType,
                        onSuccess = { data ->
                            onSuccess(data)
                        },
                        onFailure = { error, data ->
                            Log.d("callSyncHttp", data.toString())
                            onFailure(error, data)
                        }
                    )
                }
            } catch (e: Exception) {
                Log.d("callSyncHttp", "Error", e)
                onFailure(ResultType.FAILED_NETWORK, e.toString())
            }
        }

        /**
         * HTTP非同期コール
         *
         * @param context:          Context
         * @param url:              URL
         * @param method:           リクエストメソッド
         * @param parameter:        パラメータ
         * @param imageParameter:   イメージパラメータ(ファイル名：ファイルパスのキーバリュー形式)
         *                          ファイルパスは絶対パス
         *                          Key-Value設定例: photo1:/data/user/0/cache/temp/aaa.jpg
         * @param body:             JSON BODY
         * @param header:           ヘッダへ設定するパラメータ情報
         * @param authToken:        認証トークン要否
         * @param fcmToken:         FCMトークン
         * @param isHeader:         ヘッダ取得
         * @param classType:        クラスタイプ
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        fun callAsyncHttp(
            context: Context,
            url: String,
            method: RequestMethod? = RequestMethod.POST,
            parameter: Any? = null,
            imageParameter: HashMap<String, String>? = null,
            body: String? = null,
            header: HashMap<String, String>? = null,
            authToken: Boolean,
            fcmToken: Boolean,
            isHeader: Boolean? = false,
            classType: Class<Any>? = null,
            onSuccess: onSuccess<Any>,
            onFailure: onFailure<ResultType, Any>
        ) {
            Log.d("callAsyncHttp", url)

            val apiUrl = url
            val client = getHttpClient()
            val request = getRequest(
                context,
                apiUrl,
                method,
                parameter,
                imageParameter,
                body,
                header
            )

            try {
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d("callAsyncHttp", "Error", e)
                        onFailure(ResultType.FAILED_NETWORK, e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        // API呼出結果処理
                        handleResult(
                            context,
                            response,
                            isHeader,
                            classType,
                            onSuccess = { data ->
                                onSuccess(data)
                            },
                            onFailure = { error, data ->
                                Log.d("callAsyncHttp", data.toString())
                                onFailure(error, data)
                            }
                        )
                    }
                })
            } catch (e: Exception) {
                Log.d("callAsyncHttp", "Error", e)
                onFailure(ResultType.FAILED_NETWORK, e.toString())
            }
        }

        /**
         * API呼出結果処理
         *
         * @param context:      Context
         * @param response:     レスポンス
         * @param isHeader:     ヘッダ取得
         * @param onSuccess:    成功の場合のコールバック
         * @param onFailure:    失敗の場合のコールバック
         */
        private fun handleResult(
            context: Context,
            response: Response,
            isHeader: Boolean? = false,
            classType: Class<Any>? = null,
            onSuccess: onSuccess<Any>,
            onFailure: onFailure<ResultType, Any>
        ) {
            when (response.code) {
                200 -> {        // 正常

                    if (isHeader == true) { // ヘッダー
                        onSuccess(response.headers)
                    } else if (classType == null) { // バイナリー、ピクチャーなど
                        onSuccess(response.body!!)
                    } else if (classType == String.javaClass) { // 単純文字列
                        onSuccess(response.body!!.string())
                    } else {    // JSON Model
                        val json = response.body!!.string()
                        val model = Gson().fromJson(json, classType)

                        // クラスタイプ異常
                        if (model == null) {
                            onFailure(ResultType.FAILED_OTHER, "class type error")
                        } else {
                            onSuccess(model)
                        }
                    }

                }
                401 -> {    // 認証エラー
                    onFailure(ResultType.FAILED_AUTH, response.body!!)
                }
                else -> {   // その他
                    onFailure(ResultType.FAILED_OTHER, response.body!!)
                }
            }
        }

        /**
         * HTTP非同期コール
         *
         * @param context:      Context
         * @param request:      リクエスト
         * @param isHeader:     ヘッダ取得
         * @param onSuccess:    成功の場合のコールバック
         * @param onFailure:    失敗の場合のコールバック
         */
        private fun callAsyncHttp(
            context: Context,
            request: Request,
            isHeader: Boolean? = false,
            onSuccess: onSuccess<Any>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val client = getHttpClient()

            try {
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println(e)
                        onFailure(ResultType.FAILED_NETWORK, e.toString())
                    }

                    override fun onResponse(call: Call, response: Response) {
                        println(response)

                        // API呼出結果処理
                        handleResult(context, response, isHeader,
                            onSuccess = { data ->
                                onSuccess(data)
                            },
                            onFailure = { error, data ->
                                onFailure(error, data)
                            }
                        )
                    }
                })
            } catch (e: Exception) {
                println(e)
                onFailure(ResultType.FAILED_NETWORK, e.toString())
            }
        }

        /**
         * Httpクライアント作成
         *
         * @return OkHttpClientインスタンス
         */
        private fun getHttpClient(): OkHttpClient {
            val clientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
            clientBuilder.connectTimeout(API_CONNECT_TIMEOUT, TimeUnit.SECONDS)
            clientBuilder.readTimeout(API_READ_TIMEOUT, TimeUnit.SECONDS)
            clientBuilder.writeTimeout(API_WRITE_TIMEOUT, TimeUnit.SECONDS)

            return clientBuilder.build()
        }

        /**
         * リクエスト作成
         *
         * @param context:          コンテキスト
         * @param url:              URL
         * @param method:           リクエストメソッド
         * @param parameter:        パラメータ
         * @param imageParameter:   イメージパラメータ(ファイル名：ファイルパスのキーバリュー形式)
         *                          ファイルパスは絶対パス
         *                          Key-Value設定例: photo1:/data/user/0/cache/temp/aaa.jpg
         * @param body:             JSON BODY
         * @param header:           ヘッダへ設定するパラメータ情報
         * @param authToken:        認証トークン要否
         * @param fcmToken:         FCMトークン要否
         */
        private fun getRequest(
            context: Context,
            url: String,
            method: RequestMethod? = RequestMethod.POST,
            parameter: Any? = null,
            imageParameter: HashMap<String, String>? = null,
            body: String? = null,
            header: HashMap<String, String>? = null,
        ): Request {
            val builder: Request.Builder = Request.Builder()
            builder.url(url)

            // パラメータ作成
            if (imageParameter == null) {
                makeParameter(url, parameter, body, method, builder)
            } else {
                makeMultiPartParameter(
                    context,
                    parameter as HashMap<String, String>,
                    imageParameter,
                    method,
                    builder
                )
            }

            // ヘッダー作成
            makeHeader(header, builder)

            return builder.build()
        }

        /**
         * パラメータ作成
         *
         * @param url:          URL
         * @param parameter:    パラメータ
         * @param body:         JSON BODY
         * @param method:       リクエストメソッド
         * @param builder:      リクエストビルダー
         */
        private fun makeParameter(
            url: String,
            parameter: Any?,
            body: String?,
            method: RequestMethod? = RequestMethod.POST,
            builder: Request.Builder
        ) {
            var requestBody: RequestBody = "".toRequestBody(JSON_TYPE)
            val bodyBuilder: FormBody.Builder = FormBody.Builder()

            // パラメータ
            if (parameter != null) {
                if (parameter is HashMap<*, *>) {
                    if (method == RequestMethod.GET) {
                        val httpBuilder = url.toHttpUrlOrNull()!!.newBuilder()
                        for ((key, value) in parameter.entries) {
                            httpBuilder.addQueryParameter(key.toString(), value.toString())
                        }
                        builder.url(httpBuilder.build())
                    } else {
                        for ((key, value) in parameter.entries) {
                            bodyBuilder.add(key.toString(), value.toString())
                        }
                        requestBody = bodyBuilder.build()
                    }
                }
            }

            // JSON BODY
            if (body != null) {
                if (body.isNotEmpty()) {
                    requestBody = body.toRequestBody(JSON_TYPE)
                }
            }

            // リクエストメソッド
            when (method) {
                RequestMethod.GET -> {
                    builder.get()
                }
                RequestMethod.POST -> {
                    builder.post(requestBody)
                }
                RequestMethod.PUT -> {
                    builder.put(requestBody)
                }
                else -> {
                }
            }
        }

        /**
         * Multipartパラメータ作成
         *
         * @param context:          コンテキスト
         * @param formParameter:    Formパラメータ(key/value Hashmap)
         * @param imageParameter:   イメージパラメータ(ファイル名：ファイルパスのキーバリュー形式)
         *                          ファイルパスは絶対パス
         *                          Key-Value設定例: photo1:/data/user/0/cache/temp/aaa.jpg
         * @param method:           リクエストメソッド
         * @param builder:          リクエストビルダー
         */
        private fun makeMultiPartParameter(
            context: Context,
            formParameter: HashMap<String, String>?,
            imageParameter: HashMap<String, String>? = null,
            method: RequestMethod? = RequestMethod.POST,
            builder: Request.Builder
        ) {

            // Formパラメータ
            val boundary = System.currentTimeMillis().toString()
            val bodyBuilder = MultipartBody.Builder(boundary).setType(MultipartBody.FORM)
            if (formParameter != null) {
                for (key in formParameter.keys) {
                    bodyBuilder.addFormDataPart(key, formParameter[key]!!)
                }
            }

            // Imageファイルパラメータ
            if (imageParameter != null) {
                for (imageKey in imageParameter.keys) {
                    val imageFile = File(imageParameter[imageKey])
                    bodyBuilder.addFormDataPart(
                        imageKey, "${imageKey}.png",
                        imageFile.asRequestBody("image/png".toMediaTypeOrNull())
                    )
                }
            }

            val body: RequestBody = bodyBuilder.build()

            // リクエストメソッド
            when (method) {
                RequestMethod.GET -> {
                    builder.get()
                }
                RequestMethod.POST -> {
                    builder.post(body)
                }
                RequestMethod.PUT -> {
                    builder.put(body)
                }
                else -> {

                }
            }
        }

        /**
         * リクエストヘッダー作成
         *
         * @param header:       ヘッダへ設定するパラメータ情報
         * @param builder:      リクエストビルダー
         */
        private fun makeHeader(header: HashMap<String, String>?, builder: Request.Builder) {
            if (header != null) {
                for (key in header.keys) {
                    if (header.containsKey(key)) {
                        builder.addHeader(key, header[key]!!)
                    }
                }
            }
        }

    }
}
package com.xieyi.etoffice.common

import android.content.Context
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.common.model.LoginResultModel
import com.xieyi.etoffice.enum.RequestMethod
import com.xieyi.etoffice.enum.ResultType
import com.xieyi.etoffice.jsonData.EtOfficeGetTenant
import com.xieyi.etoffice.jsonData.EtOfficeGetUserLocation
import com.xieyi.etoffice.jsonData.EtOfficeSetTenant
import com.xieyi.etoffice.jsonData.EtOfficeSetUserLocation
import org.json.JSONObject

class Api {
    companion object {

        /**
         * ログイン処理、ユーザー情報取得
         *
         * @param context:          コンテキスト
         * @param uid:              uid
         * @param password:         password
         * @param registrationid:   registrationid
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeLogin(
            context: Context,
            uid: String,
            password: String,
            registrationid: String,
            onSuccess: onSuccess<LoginResultModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeLogin")
            jsonObject.put("uid", uid)
            jsonObject.put("password", password)
            jsonObject.put("registrationid", registrationid)
            jsonObject.put("device", Config.Device)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = LoginResultModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as LoginResultModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * EtOfficeGetTenant
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetTenant(
            context: Context,
            onSuccess: onSuccess<EtOfficeGetTenant.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetTenant")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("device", Config.Device)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeGetTenant.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetTenant.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * EtOfficeSetTenant
         *
         * @param context:          コンテキスト
         * @param tenantid:         tenantid
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetTenant(
            context: Context,
            tenantid: String,
            onSuccess: onSuccess<EtOfficeSetTenant.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetTenant")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("device", "android")
            jsonObject.put("tenantid",tenantid)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeSetTenant.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeSetTenant.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


        /**
         * EtOfficeGetUserLocation
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetUserLocation(
            context: Context,
            onSuccess: onSuccess<EtOfficeGetUserLocation.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetUserLocation")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeGetUserLocation.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetUserLocation.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }




        /**
         * EtOfficeSetUserLocation
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetUserLocation(
            context: Context,
            location : String,
            latitude : String,
            longitude : String,
            onSuccess: onSuccess<EtOfficeSetUserLocation.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetUserLocation")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)
            jsonObject.put("location", location)
            jsonObject.put("latitude", latitude)
            jsonObject.put("longitude", longitude)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeSetUserLocation.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeSetUserLocation.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


    }
}
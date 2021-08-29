package com.xieyi.etoffice.common

import android.content.Context
import android.util.Log
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.common.model.LoginResultModel
import com.xieyi.etoffice.enum.RequestMethod
import com.xieyi.etoffice.enum.ResultType
import com.xieyi.etoffice.jsonData.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.ArrayList

class Api {
    companion object {
        private val TAG = javaClass.simpleName

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
         * EtOfficeGetTenant        所属会社一覧
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
         * EtOfficeSetTenant        起動会社設定登録
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
         * EtOfficeGetUserLocation  ユーザー勤務場所一覧取得
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
         * EtOfficeSetUserLocation  ユーザー勤務場所設定
         *
         * @param context:          コンテキスト
         * @param location:         location
         * @param latitude:         latitude
         * @param longitude:        longitude
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



        /**
         * EtOfficeGetReportInfo    日報詳細取得
         *
         * @param context:          コンテキスト
         * @param ymd:              ymd
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetReportInfo(
            context: Context,
            ymd : String,
            onSuccess: onSuccess<EtOfficeGetReportInfo.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetReportInfo")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)
            jsonObject.put("userid", EtOfficeApp.userid)
            jsonObject.put("ymd", ymd)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeGetReportInfo.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetReportInfo.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


        /**
         * EtOfficeSetComment    コメント登録
         *
         * @param context:          コンテキスト
         * @param ymd:              ymd
         * @param comment:          comment
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetComment(
            context: Context,
            ymd : String,
            comment : String,
            onSuccess: onSuccess<EtOfficeSetComment.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetComment")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)
            jsonObject.put("userid", EtOfficeApp.userid)
            jsonObject.put("ymd", ymd)
            jsonObject.put("comment", comment)
            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeSetComment.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeSetComment.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


        /**
         * EtOfficeGetStuffList     社員一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetStuffList(
            context: Context,
            onSuccess: onSuccess<EtOfficeGetStuffList.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetStuffList")
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
                classType = EtOfficeGetStuffList.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetStuffList.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


        /**
         * EtOfficeGetReportList    レポート
         *
         * @param context:          コンテキスト
         * @param startym:          startym,
         * @param months:           months,
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetReportListPost(
            context: Context,
            startym:String,
            months:String,
            onSuccess: onSuccess<EtOfficeGetReportList.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetReportList")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)
            jsonObject.put("startym", startym)
            jsonObject.put("months", months)
            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeGetReportList.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetReportList.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * EtOfficeSetApprovalJsk 勤務実績承認
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetApprovalJskPost(
            context: Context,
            ymdArray: ArrayList<String>,
            onSuccess: onSuccess<EtOfficeSetApprovalJsk.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetApprovalJsk")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)
            jsonObject.put("userid", EtOfficeApp.userid)
            val ymdJSONArray = JSONArray()
            for ( ymd in ymdArray){
                ymdJSONArray.put(ymd)
            }
            jsonObject.put("updateymd",ymdJSONArray)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeSetApprovalJsk.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeSetApprovalJsk.JsonClass

                    Log.e(TAG, "EtOfficeSetApprovalJskPost: EtOfficeSetApprovalJsk:$data", )
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


        /**
         * EtOfficeUserInfo ユーザー情報取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeUserInfo(
            context: Context,
            onSuccess: onSuccess<EtOfficeUserInfo.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeUserInfo")
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
                classType = EtOfficeUserInfo.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeUserInfo.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


        /**
         * EtOfficeGetUserStatus ユーザー最新勤務状態の一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetUserStatusPost(
            context: Context,
            onSuccess: onSuccess<EtOfficeGetUserStatus.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetUserStatus")
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
                classType = EtOfficeGetUserStatus.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetUserStatus.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


        /**
         * EtOfficeGetStatusList   出勤状態一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetStatusListPost(
            context: Context,
            onSuccess: onSuccess<EtOfficeGetStatusList.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetStatusList")
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
                classType = EtOfficeGetStatusList.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetStatusList.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }



        /**
         * EtOfficeGetMessage   最新メッセージ一覧取得
         *
         * @param context:          コンテキスト
         * @param count:            count
         * @param lasttime:         lasttime
         * @param lastsubid:        lastsubid
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetMessagePost(
            context: Context,
            count:String,
            lasttime:String,
            lastsubid:String,
            onSuccess: onSuccess<EtOfficeGetMessage.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetMessage")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)
            jsonObject.put("count", count)
            jsonObject.put("lasttime", lasttime)
            jsonObject.put("lastsubid", lastsubid)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeGetMessage.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeGetMessage.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }



        /**
         * EtOfficeSetUserStatus
         *
         * @param context:          コンテキスト
         * @param location:         String,
         * @param longitude:        Double,
         * @param latitude:         Double,
         * @param statusvalue:      String,
         * @param statustext:       String,
         * @param memo:             String,
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetUserStatusPost(
            context: Context,
            location:String,
            longitude:Double,
            latitude:Double,
            statusvalue:String,
            statustext:String,
            memo:String,
            onSuccess: onSuccess<EtOfficeSetUserStatus.JsonClass>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetUserStatus")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device", Config.Device)

            jsonObject.put("statusvalue", statusvalue)
            jsonObject.put("statustext", statustext)
            jsonObject.put("location", location)
            jsonObject.put("longitude", longitude.toString())
            jsonObject.put("latitude", latitude.toString())
            jsonObject.put("memo", memo)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = EtOfficeSetUserStatus.JsonClass::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as EtOfficeSetUserStatus.JsonClass
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

    }
}
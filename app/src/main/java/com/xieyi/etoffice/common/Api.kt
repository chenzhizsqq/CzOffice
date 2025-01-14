package com.xieyi.etoffice.common

import android.content.Context
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.common.model.*
import com.xieyi.etoffice.enum.RequestMethod
import com.xieyi.etoffice.enum.ResultType
import org.json.JSONArray
import org.json.JSONObject

class Api {
    companion object {
        /**
         * 共通パラメータを設定
         */
        private fun setCommonParam(jsonObject: JSONObject) {
            val prefs =
                EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            val token: String? = prefs.getString("token", "")
            val tenantid: String? = prefs.getString("tenantid", "")
            val hpid: String? = prefs.getString("hpid", "")

            jsonObject.put("token", token)
            jsonObject.put("tenant", tenantid)
            jsonObject.put("hpid", hpid)
            jsonObject.put("device", Config.Device)
        }

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
         * ユーザー最新勤務状態の一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetUserStatus(
            context: Context,
            onSuccess: onSuccess<UserStatusModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetUserStatus")
            setCommonParam(jsonObject)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = UserStatusModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as UserStatusModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * ユーザー勤務状態の設定
         *
         * @param context:          コンテキスト
         * @param longitude:        最新勤務状態更新場所経度
         * @param latitude:         最新勤務状態更新場所緯度
         * @param location:         最新勤務状態更新場所名称
         * @param statusvalue:      最新勤務状態値
         * @param statustext:       最新勤務状態文字列
         * @param memo:             備考
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetUserStatus(
            context: Context,
            longitude: Double,
            latitude: Double,
            location: String,
            statusvalue: String,
            statustext: String,
            memo: String,
            onSuccess: onSuccess<UserStatusModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetUserStatus")
            setCommonParam(jsonObject)

            jsonObject.put("statusvalue", statusvalue)
            jsonObject.put("statustext", statustext)
            jsonObject.put("location", location)
            jsonObject.put("longitude", String.format("%.6f", longitude))
            jsonObject.put("latitude", String.format("%.6f", latitude))
            jsonObject.put("memo", memo)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = UserStatusModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as UserStatusModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * ユーザー最新勤務状態の一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetUserLocation(
            context: Context,
            onSuccess: onSuccess<UserLocationModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetUserLocation")
            setCommonParam(jsonObject)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = UserLocationModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as UserLocationModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * ユーザー勤務場所設定
         *
         * @param context:          コンテキスト
         * @param longitude:        勤務場所経度
         * @param longitude:        勤務場所緯度
         * @param location:         勤務場所名称
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetUserLocation(
            context: Context,
            longitude: Double,
            latitude: Double,
            location: String,
            onSuccess: onSuccess<UserLocationModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetUserLocation")
            setCommonParam(jsonObject)

            jsonObject.put("longitude", String.format("%.6f", longitude))
            jsonObject.put("latitude", String.format("%.6f", latitude))
            jsonObject.put("location", location)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = UserLocationModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as UserLocationModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 所属会社一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetTenant(
            context: Context,
            onSuccess: onSuccess<TenantModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetTenant")
            setCommonParam(jsonObject)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = TenantModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as TenantModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 起動会社設定登録
         *
         * @param context:          コンテキスト
         * @param tenant:           起動会社会社識別ID
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetTenant(
            context: Context,
            tenant: String,
            onSuccess: onSuccess<TenantModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl


            val prefs =
                EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            val token: String? = prefs.getString("token", "")

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetTenant")
            jsonObject.put("token", token)
            jsonObject.put("device", Config.Device)
            jsonObject.put("tenantid", tenant)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = TenantModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as TenantModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 最新メッセージ一覧取得
         *
         * @param context:          コンテキスト
         * @param count:            取得件数
         * @param lasttime:         前回取得した最後のupdatetime (2回目以後追加取得用)
         * @param lastsubid:        前回取得した最後のsubid (2回目以後追加取得用)
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetMessage(
            context: Context,
            count: Int,
            lasttime: String? = null,
            lastsubid: String? = null,
            onSuccess: onSuccess<MessageModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetMessage")
            setCommonParam(jsonObject)

            jsonObject.put("count", count.toString())

            if (lasttime != null) {
                jsonObject.put("lasttime", lasttime!!)
                jsonObject.put("lastsubid", lastsubid!!)
            }

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = MessageModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as MessageModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * メッセージ既読、未読設定
         *
         * @param context:          コンテキスト
         * @param updateid:         更新対象: 要素[連結文字列updatetime+subid]の配列
         * @param readflg:          1:既読 2:削除
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetMessage(
            context: Context,
            updateid: JSONArray,
            readflg: String,
            onSuccess: onSuccess<SetMessageModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetMessage")
            setCommonParam(jsonObject)

            jsonObject.put("updateid", updateid)
            jsonObject.put("readflg", readflg)


            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = SetMessageModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as SetMessageModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 出勤状態一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetStatusList(
            context: Context,
            onSuccess: onSuccess<StatusModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetStatusList")
            setCommonParam(jsonObject)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = StatusModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as StatusModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 日報一覧取得
         *
         * @param context:          コンテキスト
         * @param userid:           ユーザーID
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetReportList(
            context: Context,
//            startym: String,
//            months: String,
            userid: String,
            onSuccess: onSuccess<ReportListModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetReportList")
            setCommonParam(jsonObject)

//            jsonObject.put("startym", startym)
//            jsonObject.put("months", months)

            //userid: String
            jsonObject.put("userid", userid)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = ReportListModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as ReportListModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 社員一覧取得
         *
         * @param context:          コンテキスト
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetStuffList(
            context: Context,
            onSuccess: onSuccess<StuffListModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetStuffList")
            setCommonParam(jsonObject)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = StuffListModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as StuffListModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 勤務実績承認
         *
         * @param context:          コンテキスト
         * @param ymdArray:         更新対象: 要素年月日[ymd]の配列
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetApprovalJsk(
            context: Context,
            ymdArray: List<String>,
            onSuccess: onSuccess<SetApprovalJskModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetApprovalJsk")
            setCommonParam(jsonObject)


            val prefs =
                EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            val userid: String? = prefs.getString("userid", "")

            jsonObject.put("userid", userid)
            val ymdJsonArray = JSONArray()
            for (ymd in ymdArray) {
                ymdJsonArray.put(ymd)
            }
            jsonObject.putOpt("updateymd", ymdJsonArray)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = SetApprovalJskModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as SetApprovalJskModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 日報詳細取得
         *
         * @param context:          コンテキスト
         * @param ymd:              対象年月日
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetReportInfo(
            context: Context,
            ymd: String,
            onSuccess: onSuccess<ReportModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetReportInfo")
            setCommonParam(jsonObject)

            val prefs =
                EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            val userid: String? = prefs.getString("userid", "")

            jsonObject.put("userid", userid)
            jsonObject.put("ymd", ymd)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = ReportModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as ReportModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * プロジェクト一覧
         *
         * @param context:          コンテキスト
         * @param ymd:              対象年月日
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeGetProject(
            context: Context,
            ymd: String,
            onSuccess: onSuccess<ProjectModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetProject")
            setCommonParam(jsonObject)

            val prefs =
                EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            val userid: String? = prefs.getString("userid", "")

            jsonObject.put("userid", userid)
            jsonObject.put("ymd", ymd)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = ProjectModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as ProjectModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * 日報登録
         *
         * @param context:          コンテキスト
         * @param ymd:              対象年月日
         * @param projectcd:        更新対象: プロジェクトCD
         * @param wbscd:            更新対象: 作業CD
         * @param totaltime:        更新対象: 工数（4桁、例：0800）
         * @param starttime:        更新対象: 開始時間（4桁、例：0900）
         * @param endtime:          更新対象: 終了時間（4桁、例：1800）
         * @param place:            更新対象: 場所
         * @param memo:             更新対象: 備考、報告
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetReport(
            context: Context,
            ymd: String,
            projectcd: String,
            wbscd: String,
            totaltime: String,
            starttime: String,
            endtime: String,
            place: String,
            memo: String,
            onSuccess: onSuccess<SetReportModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetReport")
            setCommonParam(jsonObject)

            val prefs =
                EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            val userid: String? = prefs.getString("userid", "")

            jsonObject.put("userid", userid)
            jsonObject.put("ymd", ymd)
            jsonObject.put("projectcd", projectcd)
            jsonObject.put("wbscd", wbscd)
            jsonObject.put("totaltime", totaltime)
            jsonObject.put("starttime", starttime)
            jsonObject.put("endtime", endtime)
            jsonObject.put("place", place)
            jsonObject.put("memo", memo)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = SetReportModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as SetReportModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }

        /**
         * プロジェクト一覧
         *
         * @param context:          コンテキスト
         * @param ymd:              対象年月日
         * @param comment:          更新対象:コメント内容
         * @param onSuccess:        成功コールバック
         * @param onFailure:        失敗コールバック
         */
        @Suppress("UNCHECKED_CAST")
        fun EtOfficeSetComment(
            context: Context,
            ymd: String,
            comment: String,
            onSuccess: onSuccess<CommentModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetComment")
            setCommonParam(jsonObject)


            val prefs =
                EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            val userid: String? = prefs.getString("userid", "")

            jsonObject.put("userid", userid)
            jsonObject.put("ymd", ymd)
            jsonObject.put("comment", comment)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = CommentModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as CommentModel
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
            onSuccess: onSuccess<UserInfoModel>,
            onFailure: onFailure<ResultType, Any>
        ) {
            val url: String = Config.ApiUrl

            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeUserInfo")
            setCommonParam(jsonObject)

            HttpUtil.callAsyncHttp(
                context = context,
                url = url,
                method = RequestMethod.POST,
                parameter = jsonObject,
                authToken = false,
                fcmToken = false,
                classType = UserInfoModel::class.java as Class<Any>,
                onSuccess = { data ->
                    val model = data as UserInfoModel
                    onSuccess(model)
                },
                onFailure = { error, data ->
                    onFailure(error, data)
                }
            )
        }


    }
}
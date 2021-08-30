package com.xieyi.etoffice.common.model

data class UserStatusInfo(
    val userid: String,         //ユーザー識別ID
    val usercode: String,       //社員コード
    val username: String,       //氏名
    val userkana: String,       //カナ
    val location: String,       //最新勤務状態更新場所
    val memo: String,           //備考
    val statustext: String,     //最新勤務状態文字列
    val statustime: String,     //最新勤務状態更新時刻
    val statusvalue: String,    //最新勤務状態値
)

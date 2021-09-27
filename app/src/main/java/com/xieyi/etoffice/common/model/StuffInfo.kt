package com.xieyi.etoffice.common.model


data class StuffInfo(
    var tenant: String,                         // 会社識別ID
    var hpid: String,                           // WEBサイト識別ID
    var userid: String,                         // 社員ID
    var username: String,                       // 社員名
    var userkana: String,                       // カタカナ
    var phone: String,                          // 電話番号
    var mail: String,                           // メール
)

package com.xieyi.etoffice.common.model

data class TenantInfo(
    var tenantid: String,   // 会社識別ID
    var startflg: String,   // 1:ログイン後の起動会社
    var tenantname: String, // 会社名
    var hpid: String,       // 会社識別ID
    var posturl: String,    // posturl
)

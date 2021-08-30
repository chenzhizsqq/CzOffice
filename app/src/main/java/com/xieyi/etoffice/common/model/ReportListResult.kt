package com.xieyi.etoffice.common.model


data class ReportListResult(
    var authflag: String,           // 承認権限有無標識（1：有、その他：無）
    var group: List<GroupInfo>,     // グループ一覧
)


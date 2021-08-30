package com.xieyi.etoffice.common.model

data class GroupInfo(
    var month: String,                      // グループ年月
    var reportlist: List<ReportListnfo>     // 日報一覧
)
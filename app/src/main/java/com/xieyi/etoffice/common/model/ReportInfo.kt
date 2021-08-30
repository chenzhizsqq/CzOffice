package com.xieyi.etoffice.common.model

data class ReportInfo(
    var project: String,    // 項目名
    var wbs: String,        // 作業名
    var time: String,       // 作業時間
    var memo: String,       // 報告、備考
)

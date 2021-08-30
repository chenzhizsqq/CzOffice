package com.xieyi.etoffice.common.model

data class PlanWorkInfo(
    var project: String,    // 項目名
    var wbs: String,        // 作業名
    var date: String,       // 時間帯
    var time: String,       // 計画時間
)

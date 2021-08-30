package com.xieyi.etoffice.common.model

data class StatusInfo(
    var statustime: String,     // 時間戳
    var statusvalue: String,    // 状態（id）
    var statustext: String,     // 状態（文字）
    var memo: String,           // 備考
)

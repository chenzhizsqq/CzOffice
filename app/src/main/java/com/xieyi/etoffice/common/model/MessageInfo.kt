package com.xieyi.etoffice.common.model

data class MessageInfo(
    val title: String,          // タイトル
    val content: String,        // 内容
    val updatetime: String,     // 日時yyyyMMddHHmmss
    val subid: String,          // 同一日時に複数件の場合、連番(1～)
)

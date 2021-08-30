package com.xieyi.etoffice.common.model

data class ReportListnfo(
    var yyyymmdd: String,       // 年月日
    var holiday: String,        // 休日名称(空白の場合、平日)
    var title: String,          // 件名
    var approval: String,       // 承認状況
    var content: String,        // 概要内容(複数行)
    var itemid: String,
    var warning: String,        // 警告フラグ
)

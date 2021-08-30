package com.xieyi.etoffice.common.model


data class ReportResult(
    var authflag: String,                       // 承認権限有無標識（1：有、その他：無）
    var planworktime: String,                   // 出勤予定（例：通常勤務 09:00-18:00(8.00h)）
    var worktime: String,                       // 出勤実績（例：通常勤務 09:00-18:00(8.00h)）
    var planworklist: List<PlanWorkInfo>,       // 作業予定一覧
    var workstatuslist: List<WorkStatusInfo>,   // 状態一覧
    var reportlist: List<ReportInfo>,           // 日報一覧
    var commentlist: List<CommentInfo>,         // コメント一覧
)


package com.xieyi.etoffice.common.model

data class ProjectInfo(
    var projectcd: String,          // プロジェクトCD
    var projectname: String,        // プロジェクト名
    var wbslist: List<WbsInfo>,     // 作業一覧
)

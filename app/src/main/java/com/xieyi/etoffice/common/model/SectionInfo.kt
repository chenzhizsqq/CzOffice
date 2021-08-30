package com.xieyi.etoffice.common.model

data class SectionInfo(
    var sectioncd: String,          // 部署CD
    var sectionname: String,        // 部署名
    var stufflist: List<StuffInfo>,     // 社員一覧
)

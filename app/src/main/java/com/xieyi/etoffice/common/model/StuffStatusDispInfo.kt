package com.xieyi.etoffice.common.model


data class StuffStatusDispInfo(
    var sectionCode: String,                    // 部門コード
    var sectionName: String,                    // 部門名
    var stuffInfo: StuffInfo?,                  // 社員情報
    var userStatusInfo: UserStatusInfo?,        // 社員最新出勤情報
)

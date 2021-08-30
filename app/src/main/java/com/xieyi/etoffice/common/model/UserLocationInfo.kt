package com.xieyi.etoffice.common.model

data class UserLocationInfo(
    var longitude: String,      // 勤務場所経度
    var latitude: String,       // 勤務場所緯度
    var location: String,       // 勤務場所名称
)

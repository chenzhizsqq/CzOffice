package com.xieyi.etoffice.common.model

data class LoginResultModel(
    var message: String,
    var result: LoginResultInfo,
    var status: Int
)
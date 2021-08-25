package com.xieyi.etoffice.common.model

data class LoginResultModel(
    val message: String,
    val result: LoginResultInfo,
    val status: Int
)
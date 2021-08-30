package com.xieyi.etoffice.common.model

data class UserInfoResult(
    val userid: String,     //ユーザー識別ID
    val usercode: String,   //社員コード
    val username: String,    //username
    val userkana: String,   //userkana
    val mail: String,       //mail
    val phone: String,      //phone
)
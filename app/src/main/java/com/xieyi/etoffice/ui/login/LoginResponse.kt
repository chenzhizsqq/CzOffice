package com.xieyi.etoffice.ui.login

data class LoginResponse(val status:String, val result:Result, val message:String)
data class Result(
    val token: String,
    var tenantid: String,
    var hpid: String,
    val userid: String,
    val usercode: String,
    val username: String,
    val userkana: String,
    val mail: String,
    val phone: String
)
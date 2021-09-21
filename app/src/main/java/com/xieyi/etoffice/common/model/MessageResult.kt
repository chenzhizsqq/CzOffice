package com.xieyi.etoffice.common.model

data class MessageResult(
    var messagelist: List<MessageInfo>,
    val recordlist: List<Recordlist>
)


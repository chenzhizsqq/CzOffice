package com.xieyi.etoffice.ui.notifications

data class NotificationsResponse(val status:String, val result:Result, val message:String)
data class Result(val messagelist:ArrayList<Message>)
data class Message(val title:String, val content:String,val updatetime:String, val subid:String)

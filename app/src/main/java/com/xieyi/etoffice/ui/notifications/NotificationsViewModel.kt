package com.xieyi.etoffice.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "メッセージ"
    }
    val text: LiveData<String> = _text
    var messageList = ArrayList<Message>()
    var lasttime = ""
    var lastsubid = ""
    fun appendMessage(messageListNew: ArrayList<Message>){
        messageList.addAll(messageListNew)
    }
}
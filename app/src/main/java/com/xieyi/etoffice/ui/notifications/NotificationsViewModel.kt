package com.xieyi.etoffice.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xieyi.etoffice.common.model.MessageInfo

class NotificationsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "メッセージ"
    }
    val text: LiveData<String> = _text
    var messageList = ArrayList<MessageInfo>()
    var lasttime = ""
    var lastsubid = ""
    fun appendMessage(messageListNew: List<MessageInfo>){
        messageList.addAll(messageListNew)
    }
    var selectFlag:Boolean = false
    var checkedPosition:Int = -1
}
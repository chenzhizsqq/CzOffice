package com.xieyi.etoffice.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xieyi.etoffice.jsonData.JC

class HomeViewModel : ViewModel() {

    private val _title1 = MutableLiveData<String>().apply {
        value = "株式会社"
    }
    val title1: LiveData<String> = _title1

    private val _title2 = MutableLiveData<String>().apply {
        value = "共通機能"
    }
    val title2: LiveData<String> = _title2

    private val _title3 = MutableLiveData<String>().apply {
        value = "出勤記録"
    }
    val title3: LiveData<String> = _title3

    private val _title4 = MutableLiveData<String>().apply {


        value = "Message"
        Thread {
            if (JC.pEtOfficeUserInfo.post() == "0") {
                postValue(JC.pEtOfficeUserInfo.infoUserStatusList()?.mail)
            }
        }.start()
    }
    val title4: LiveData<String> = _title4
}
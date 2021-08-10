package com.xieyi.etoffice.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xieyi.etoffice.Tools

class HomeViewModel : ViewModel() {

    private val _companyTitle = MutableLiveData<String>().apply {
        value = "株式会社写易"
    }
    val companyTitle: LiveData<String> = _companyTitle


    private val _date = MutableLiveData<String>().apply {


        value = Tools.getDate(".")


//        Thread {
//            if (JC.pEtOfficeUserInfo.post() == "0") {
//                postValue(JC.pEtOfficeUserInfo.infoUserStatusList()?.mail)
//            }
//        }.start()
    }
    val date: LiveData<String> = _date
}
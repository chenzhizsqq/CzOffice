package com.xieyi.etoffice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val reportFragTitle = MutableLiveData<String>().apply {
        value = EtOfficeApp.context.getString(R.string.REPORT_TITLE)
    }
}
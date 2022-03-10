package com.xieyi.etoffice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val reportFragTitle = MutableLiveData<String>().apply {
        value = R.string.REPORT_TITLE.toString()
    }
}
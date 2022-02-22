package com.xieyi.etoffice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {

    val reportFragTitle = MutableLiveData<String>().apply {
        value = "日报"
    }
}
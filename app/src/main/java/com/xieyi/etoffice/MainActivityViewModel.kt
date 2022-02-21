package com.xieyi.etoffice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xieyi.etoffice.Tools

class MainActivityViewModel : ViewModel() {

    val reportFragTitle = MutableLiveData<String>().apply {
        value = "日报"
    }
}
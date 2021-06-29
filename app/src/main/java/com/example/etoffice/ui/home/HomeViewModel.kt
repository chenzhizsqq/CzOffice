package com.example.etoffice.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "ホーム"
    }
    val text: LiveData<String> = _text

    private val _title1 = MutableLiveData<String>().apply {
        value = "_title1"
    }
    val title1: LiveData<String> = _title1

    private val _title2 = MutableLiveData<String>().apply {
        value = "_title2"
    }
    val title2: LiveData<String> = _title2

    private val _title3 = MutableLiveData<String>().apply {
        value = "_title3"
    }
    val title3: LiveData<String> = _title3

    private val _title4 = MutableLiveData<String>().apply {
        value = "_title4"
    }
    val title4: LiveData<String> = _title4
}
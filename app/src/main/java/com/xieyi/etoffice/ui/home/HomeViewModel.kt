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
    }
    val date: LiveData<String> = _date

    //绑定loading状态
    val mLoading = MutableLiveData(false)
    val liveDataLoading: LiveData<Boolean> = mLoading
}
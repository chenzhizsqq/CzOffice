package com.xieyi.etoffice.ui.report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportMemberViewModel : ViewModel() {
    //绑定loading状态
    val mLoading = MutableLiveData(false)
    val liveDataLoading: LiveData<Boolean> = mLoading
}
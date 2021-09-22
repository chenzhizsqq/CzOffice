package com.xieyi.etoffice.ui.member

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MemberViewModel : ViewModel() {
    //绑定loading状态
    val mLoading = MutableLiveData(false)
    val liveDataLoading: LiveData<Boolean> = mLoading
}
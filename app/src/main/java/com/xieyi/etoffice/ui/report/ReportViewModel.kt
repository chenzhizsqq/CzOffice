package com.xieyi.etoffice.ui.report

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel : ViewModel() {
    private val mAllSelect = MutableLiveData(false)
    private val mVisibility = MutableLiveData(View.GONE)

    val allSelect: LiveData<Boolean> = mAllSelect
    val visibility: LiveData<Int> = mVisibility


    fun allSelectChange() {
        if (mAllSelect.value == true) {
            mAllSelect.value = false
        } else if (mAllSelect.value == false) {
            mAllSelect.value = true
        }
    }

    fun allSelectChangeFalse() {
        mAllSelect.value = false
    }


    fun visibilityChange() {
        if (mVisibility.value == View.VISIBLE) {
            mVisibility.value = View.GONE
        } else if (mVisibility.value == View.GONE) {
            mVisibility.value = View.VISIBLE
        }
    }

    fun isAllSelect(): Boolean? {
        return mAllSelect.value
    }

    //绑定loading状态
    val mLoading = MutableLiveData(false)
    val liveDataLoading: LiveData<Boolean> = mLoading

    //滚动状态
    val mIsScrolled = MutableLiveData(false)
    private val _reportState = ReportState()
    val mLiveDataReportState = MutableLiveData(_reportState)

}

//滚动状态
class ReportState {
    var mScrolledY = 0
    var mScrolledName = ""
    var mPosition = -1
}
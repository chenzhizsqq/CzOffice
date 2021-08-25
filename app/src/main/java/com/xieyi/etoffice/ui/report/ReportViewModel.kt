package com.xieyi.etoffice.ui.report

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel: ViewModel()  {
    private val TAG = javaClass.simpleName
    private val mAllSelect = MutableLiveData(false)
    private val mVisibility = MutableLiveData(View.GONE)

    val allSelect: LiveData<Boolean> = mAllSelect
    val visibility: LiveData<Int> = mVisibility


    fun allSelectChange() {
        if (mAllSelect.value == true){
            mAllSelect.value =false
        }else if (mAllSelect.value == false){
            mAllSelect.value =true
        }
    }

    fun allSelectChangeFalse() {
            mAllSelect.value =false
    }


    fun visibilityChange() {
        if (mVisibility.value == View.VISIBLE){
            mVisibility.value =View.GONE
        }else if (mVisibility.value == View.GONE){
            mVisibility.value =View.VISIBLE
        }
    }

    fun isAllSelect(): Boolean? {
        return mAllSelect.value
    }
}
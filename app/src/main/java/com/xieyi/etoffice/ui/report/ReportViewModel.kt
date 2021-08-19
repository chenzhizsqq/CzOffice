package com.xieyi.etoffice.ui.report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReportViewModel: ViewModel()  {
    private val TAG = javaClass.simpleName
    private val mAllSelect = MutableLiveData(false)
    private val mIsHidden = MutableLiveData(false)

    val allSelect: LiveData<Boolean> = mAllSelect
    val isHidden: LiveData<Boolean> = mIsHidden


    fun allSelectChange() {
        if (mAllSelect.value == true){
            mAllSelect.value =false
        }else if (mAllSelect.value == false){
            mAllSelect.value =true
        }
    }

    fun isHiddenChange() {
        if (mIsHidden.value == true){
            mIsHidden.value =false
        }else if (mIsHidden.value == false){
            mIsHidden.value =true
        }
    }
}
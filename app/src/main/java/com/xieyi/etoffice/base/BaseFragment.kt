package com.xieyi.etoffice.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.chepsi.callbackdemo.Variables
import com.xieyi.etoffice.MainActivityViewModel
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools

/**
 * ベースフラグメント
 * フラグメント共通処理をここで処理する。
 */
open class BaseFragment  : Fragment() {

    //与MainActivity共同的ViewModel
    private val sharedVM: MainActivityViewModel by activityViewModels()

    /**
     * Network判断
     * @return
     */
    open fun isNetworkConnected(): Boolean {
        return Variables.isNetworkConnected
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isNetworkConnected()){
            Tools.showErrorDialog(requireActivity(),getString(R.string.MSG05))
        }

        sharedVM.reportFragTitle.value = "日报"
    }

}
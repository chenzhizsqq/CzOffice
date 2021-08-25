package com.xieyi.etoffice.base

import android.content.BroadcastReceiver
import androidx.fragment.app.Fragment

/**
 * ベースフラグメント
 * フラグメント共通処理をここで処理する。
 */
open class BaseFragment  : Fragment() {
    lateinit var broadcastReceiver: BroadcastReceiver
}
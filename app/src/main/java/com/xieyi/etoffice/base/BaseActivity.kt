package com.xieyi.etoffice.base

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.xieyi.etoffice.networkMonitor.NetworkMonitor
import com.xieyi.etoffice.networkMonitor.Variables
import com.xieyi.etoffice.Config.Companion.DOUBLE_CLICK_INTERVAL

/**
 * ベースアクティビティ
 * アクティビティ共通処理をここで処理する。
 */
open class BaseActivity : AppCompatActivity() {

    // ボタン連打防止用
    protected var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 縦画面
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // タイトルバー非表示
        supportActionBar?.hide()

        //network callback
        NetworkMonitor(this).startNetworkCallback()
    }

    /**
     * タッチイベントを受け取り
     *
     * @param ev:    イベント
     * @return 処理結果
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        super.dispatchTouchEvent(ev)
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v?.getWindowToken())
            }

            //連打判断
            if (isFastDoubleClick()) {
                return true
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * EditText以外の場所にタップされた場合、キーボードを隠す
     *
     * @param v:        View
     * @param event:    イベント
     * @return　隠す/隠さない
     */
    open fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom: Int = top + v.getHeight()
            val right: Int = left + v.getWidth()

            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }

        return false
    }

    /**
     * ソフトキーボードを隠す
     * @param token
     */
    open fun hideKeyboard(token: IBinder?) {
        if (token != null) {
            val im: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * 連打判断
     * @return
     */
    open fun isFastDoubleClick(): Boolean {
        val time = System.currentTimeMillis()
        val timeD: Long = time - lastClickTime
        if (0 < timeD && timeD < DOUBLE_CLICK_INTERVAL) {
            return true
        }
        lastClickTime = time
        return false
    }

    /**
     * Network判断
     * @return
     */
    open fun isNetworkConnected(): Boolean {
        return Variables.isNetworkConnected
    }
}
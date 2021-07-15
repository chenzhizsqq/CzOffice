package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC

class HomeReportDialog : DialogFragment() {
    private val TAG: String? = "HomeReportDialog"

    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_home_report, container)

        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        //attributes.gravity = Gravity.BOTTOM //下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes


        //ボタン　保存後に閉じる
        val btnSaveAndClose = view.findViewById<TextView>(R.id.btn_save_and_close)
        btnSaveAndClose.setOnClickListener {
            dialog!!.dismiss()
        }

        //record_linearLayout


        //这里测试了，这个Thread不能实现
        Thread {Runnable {
            try {
                var r: String = ""
                r=JC.pEtOfficeGetUserStatus.post()
                Log.e(TAG, "pEtOfficeGetUserStatus.post() :$r")

                val recordLinearLayout = view.findViewById<LinearLayout>(R.id.record_linearLayout)


                val textView = TextView(activity)
                textView.setText("Button1")
                recordLinearLayout.addView(textView)


                //TableLayout処理
                val mTableLayout = view.findViewById<TableLayout>(R.id.frag03_予約一覧)

                //状態表示
                val mTableRowState = TableRow(activity)
                mTableRowState.addView(makeTextView("検索条件：　"))
                mTableRowState.addView(makeTextView("名前：" ))

                mTableRowState.addView(
                    makeTextView(
                        "宿泊日:"
                    )
                )

                mTableRowState.addView(
                    makeTextView(
                        "予約日:"
                    )
                )
                mTableLayout.addView(mTableRowState, mTableLayout.childCount)

            } catch (e: Exception) {
                Log.e(TAG, "TAG", e)
            }}
        }.start()
        //这里测试了，这个Thread不能实现


        //这里测试了，是这样添加的
        val mTableLayout = view.findViewById<TableLayout>(R.id.frag03_予約一覧)

        //状態表示
        val mTableRowState = TableRow(activity)
        mTableRowState.addView(makeTextView("検索条件：　"))
        mTableRowState.addView(makeTextView("名前：" ))

        mTableRowState.addView(
            makeTextView(
                "宿泊日:"
            )
        )

        mTableRowState.addView(
            makeTextView(
                "予約日:"
            )
        )
        mTableLayout.addView(mTableRowState, mTableLayout.childCount)
        //这里测试了，是这样添加的

        return view
    }


    private fun makeTextView(text: String): TextView? {
        val textView = TextView(activity)
        textView.text = text
        textView.setPadding(5, 1, 5, 1)
        return textView
    }
}

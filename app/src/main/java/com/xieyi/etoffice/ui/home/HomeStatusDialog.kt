package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.R

class HomeStatusDialog(state: String) : DialogFragment() {
    val _state = state
    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_home_status, container)

        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        //attributes.gravity = Gravity.BOTTOM //下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes


        //ボタン　保存後に閉じる
        val btnSaveAndClose = view.findViewById<TextView>(R.id.btn_cancel_and_close)
        btnSaveAndClose.setOnClickListener {
            dialog!!.dismiss()
        }

        //状態表示
        val tvState:TextView= view.findViewById<TextView>(R.id.tvState)
        tvState.text = _state

        return view
    }
}

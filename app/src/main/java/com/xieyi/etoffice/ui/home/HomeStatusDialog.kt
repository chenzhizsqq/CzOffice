package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC

class HomeStatusDialog(state: String) : DialogFragment() {
    private val TAG = javaClass.simpleName
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

        //set_user_Status
        val userStatus:TextView= view.findViewById<EditText>(R.id.user_status)
        val setUserStatus:TextView= view.findViewById<TextView>(R.id.set_user_status)
        setUserStatus.setOnClickListener {
            Log.e(TAG, "setUserStatus.setOnClickListener: begin" )
            try {
                val r: String = JC.pEtOfficeSetUserStatus.post(userStatus.text as String)                   //Json 送信
                Log.e(TAG, "pEtOfficeSetUserStatus.post() :$r")

                Tools.showMsg(view,"SetUserStatus OK")
            }catch (e:Exception){
                Log.e(TAG, "pEtOfficeSetUserStatus.post() :$e")

            }
        }

        //set_user_location

        return view
    }
}

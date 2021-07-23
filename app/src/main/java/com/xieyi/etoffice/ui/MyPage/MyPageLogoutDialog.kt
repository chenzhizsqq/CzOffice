package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.LoginActivity
import com.xieyi.etoffice.R

class MyPageLogoutDialog : DialogFragment() {
    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_my_page_logout, container)

        //ボタン　保存後に閉じる
        val btnCancel = view.findViewById<TextView>(R.id.btn_Cancel)
        btnCancel.setOnClickListener {
            dialog!!.dismiss()

        }

        //ボタン　保存後に閉じる
        val btnLogout = view.findViewById<TextView>(R.id.btn_Logout)
        btnLogout.setOnClickListener {


            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
}

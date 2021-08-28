package com.xieyi.etoffice.ui.MyPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.databinding.DialogMyPageLogoutBinding
import com.xieyi.etoffice.ui.login.LoginActivity

class MyPageLogoutDialog : DialogFragment() {

    private lateinit var binding: DialogMyPageLogoutBinding

    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogMyPageLogoutBinding.inflate(inflater, container, false)

        //ボタン　保存後に閉じる
        binding.btnCancel.setOnClickListener {
            dialog!!.dismiss()

        }

        //ボタン　保存後に閉じる
        binding.btnLogout.setOnClickListener {

            //清空SharedPreferences文件中用户信息
            val prefs = activity?.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
            prefs?.edit()?.clear()?.apply();

            //消除所有的Activity
            val intent = Intent(activity, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }
}

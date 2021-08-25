package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
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


            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return binding.root
    }
}

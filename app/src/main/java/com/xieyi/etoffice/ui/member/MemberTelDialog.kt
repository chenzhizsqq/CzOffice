package com.xieyi.etoffice.ui.member

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.databinding.DialogMemberTelBinding


class MemberTelDialog : DialogFragment(){
    private val TAG: String = "MemberTelDialog"

    private lateinit var binding: DialogMemberTelBinding

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogMemberTelBinding.inflate(inflater, container, false)


        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        attributes.gravity = Gravity.BOTTOM //下方
        //attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes


        //ボタン　保存後に閉じる
        val btnSaveAndClose = binding.btnSaveAndClose
        btnSaveAndClose.setOnClickListener {
            dialog!!.dismiss()
        }

        return binding.root
    }
}

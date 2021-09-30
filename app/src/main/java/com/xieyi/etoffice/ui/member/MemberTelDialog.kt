package com.xieyi.etoffice.ui.member

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.databinding.DialogMemberTelBinding

/**
 * 電話をかける。
 * telNumber：テレフォン番号
 */
class MemberTelDialog(val telNumber: String) : DialogFragment() {
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

        binding.callTel.text = telNumber

        val REQUEST_CALL_PERMISSION = 10111 //電話　申し込む
        binding.callTel.setOnClickListener(View.OnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.CALL_PHONE
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                // CALL_PHONE 権利　ない
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf<String>(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PERMISSION
                )
            } else {
                //CALL_PHONE 権利　ある
                val uri: Uri = Uri.parse("tel:$telNumber")
                val intent = Intent(Intent.ACTION_CALL, uri)
                it.context.startActivity(intent)
            }
        })

        //ボタン　保存後に閉じる
        val btnSaveAndClose = binding.btnSaveAndClose
        btnSaveAndClose.setOnClickListener {
            dialog!!.dismiss()
        }

        return binding.root
    }
}

package com.example.etoffice.ui.home

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.example.etoffice.R
import java.util.*

class HomeReportDialog : DialogFragment() {
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

        return view
    }
}

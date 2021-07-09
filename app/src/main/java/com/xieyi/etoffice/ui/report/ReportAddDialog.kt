package com.xieyi.etoffice.ui.report

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.R
import java.util.*

class ReportAddDialog : DialogFragment() {
    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.dialog_report_add, container)

        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        //attributes.gravity = Gravity.BOTTOM //下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes

        //時間選択を表示
        val textView = view.findViewById<TextView>(R.id.time_open)
        textView.setOnClickListener {
            val myCalender = Calendar.getInstance()
            val hour = myCalender[Calendar.HOUR_OF_DAY]
            val minute = myCalender[Calendar.MINUTE]
            val myTimeListener =
                OnTimeSetListener { view, hourOfDay, minute ->
                    if (view.isShown) {
                        myCalender[Calendar.HOUR_OF_DAY] = hourOfDay
                        myCalender[Calendar.MINUTE] = minute
                    }
                }
            val timePickerDialog = TimePickerDialog(
                    activity,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    myTimeListener,
                    hour,
                    minute,
                    true
            )

            selectTime(timePickerDialog)
        }

        //ボタン　保存後に閉じる
        val btnSaveAndClose = view.findViewById<TextView>(R.id.btn_save_and_close)
        btnSaveAndClose.setOnClickListener {
            dialog!!.dismiss()
        }

        return view
    }

    //時間選択を表示　ボタンから表示します
    private fun selectTime(timePickerDialog: TimePickerDialog) {
        timePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val window = timePickerDialog.window
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.BOTTOM
        lp.windowAnimations = R.style.BottomDialogAnimation
        window!!.attributes = lp
        window!!.setBackgroundDrawable(ColorDrawable())
        timePickerDialog.setTitle("開始時間を選択する")
        timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }
}

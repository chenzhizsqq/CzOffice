package com.example.etoffice.ui.report

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

            //去掉dialog的标题，需要在setContentView()之前
            timePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val window = timePickerDialog.window
            //去掉dialog默认的padding
            window!!.decorView.setPadding(0, 0, 0, 0)
            val lp = window!!.attributes
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            //设置dialog的位置在底部
            lp.gravity = Gravity.BOTTOM
            //设置dialog的动画
            lp.windowAnimations = R.style.BottomDialogAnimation
            window!!.attributes = lp
            window!!.setBackgroundDrawable(ColorDrawable())
            timePickerDialog.setTitle("Choose hour:")
            timePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            timePickerDialog.show()
        }

        return view
    }
}

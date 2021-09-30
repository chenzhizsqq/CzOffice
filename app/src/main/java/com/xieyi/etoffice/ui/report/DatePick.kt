package com.xieyi.etoffice.ui.report

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DatePick(var year: Int, var month: Int, var day: Int) : DialogFragment(),
    DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(
            this.context as Context,
            activity as ReportDetailActivity?,
            year,
            month - 1,
            day
        )
    }

    override fun onDateSet(
        view: android.widget.DatePicker, year: Int,
        monthOfYear: Int, dayOfMonth: Int
    ) {

    }
}
package com.xieyi.etoffice.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.GpsTracker
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.setupClearButtonWithAction
import com.xieyi.etoffice.databinding.DialogMyPagePlaceBinding

/**
 * アドレスの追加
 */
class MyPagePlaceDialog : DialogFragment() {
    private val TAG: String = "MyPagePlaceDialog"

    private lateinit var binding: DialogMyPagePlaceBinding
    private lateinit var gpsTracker: GpsTracker
    private var latitude = 0.0
    private var longitude = 0.0

    var listener: OnDialogListener? = null

    interface OnDialogListener {
        fun onClick(location: String, longitude: Double, latitude: Double)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogMyPagePlaceBinding.inflate(inflater, container, false)

        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        //attributes.gravity = Gravity.BOTTOM //下方
        //attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes

        gpsTracker = GpsTracker(activity)

        gpsCheck()

        binding.btnSave.setOnClickListener {
            if (binding.location.text.isNotEmpty()) {
                listener?.onClick(
                    binding.location.text.toString(),
                    longitude,
                    latitude
                )
                dialog!!.dismiss()
            } else {
                activity?.let { Tools.showErrorDialog(it, getString(R.string.MSG09)) }
            }

        }

        //ボタン　閉じる
        binding.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }

        //EditText，编辑框(EditText)右侧追加一个自动清除按钮，输入内容后删除按钮表示，可以清除内容。
        binding.location.setupClearButtonWithAction()

        return binding.root
    }

    private fun gpsCheck() {
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.latitude
            longitude = gpsTracker.longitude
        } else {
            gpsTracker.showSettingsAlert()
        }
    }
}

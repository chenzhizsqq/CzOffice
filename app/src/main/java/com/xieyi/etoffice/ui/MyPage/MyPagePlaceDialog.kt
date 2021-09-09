package com.xieyi.etoffice.ui.MyPage

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.GpsTracker
import com.xieyi.etoffice.databinding.DialogMyPagePlaceBinding

/**
 * アドレスの追加
 */
class MyPagePlaceDialog() : DialogFragment(){
    private val TAG: String = "MyPagePlaceDialog"

    private lateinit var binding: DialogMyPagePlaceBinding
    private lateinit var gpsTracker: GpsTracker
    private var latitude = 0.0
    private var longitude = 0.0

    var listener: OnDialogListener? = null

    interface OnDialogListener {
        fun onClick(location: String,longitude:String,latitude:String)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
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
            gpsCheck()
            listener?.onClick(
                binding.location.text.toString(),
                longitude.toString(),
                latitude.toString()
            )
            dialog!!.dismiss()

        }

        //ボタン　閉じる
        binding.btnClose.setOnClickListener {
            dialog!!.dismiss()
        }

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

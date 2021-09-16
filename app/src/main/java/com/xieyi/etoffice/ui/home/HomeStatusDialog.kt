package com.xieyi.etoffice.ui.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.GpsTracker
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.databinding.DialogHomeStatusBinding


class HomeStatusDialog(statusvalue: String, statustext: String) : DialogFragment() {

    private val TAG = "HomeStatusDialog"

    private val mStatusvalue = statusvalue

    private val mStatustext = statustext

    private var longitude = 0.0

    private var latitude = 0.0

    private lateinit var binding: DialogHomeStatusBinding

    private lateinit var gpsTracker: GpsTracker


    var listener: OnDialogListener? = null

    interface OnDialogListener {
        fun onClick(userLocation: String, memo: String)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
                // 点击屏幕空白区域，隐藏软键盘
                if (currentFocus != null) {
                    val inputMethodManager: InputMethodManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                }
                return super.dispatchTouchEvent(motionEvent)
            }
        }
    }

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogHomeStatusBinding.inflate(inflater, container, false)

        gpsCheck()

        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        //attributes.gravity = Gravity.BOTTOM //下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes


        //ボタン　保存後に閉じる
        binding.btnCancelAndClose.setOnClickListener {

            listener?.onClick(
                binding.userLocation.text.toString(),
                binding.userStatusMemo.text.toString()
            )
            dialog!!.dismiss()
        }

        //状態表示
        binding.state.text = mStatustext

        //set_user_Status

        binding.setUserStatus.setOnClickListener {
            EtOfficeSetUserStatusPost(
                longitude,
                latitude,
                binding.userLocation.text.toString(),
                mStatusvalue,
                mStatustext,
                binding.userStatusMemo.text.toString()
            )
        }

        //set_user_location
        binding.setUserLocation.setOnClickListener {

            if(binding.userLocation.text.toString().isEmpty()){
                Snackbar.make(
                    binding.root,
                    getString(R.string.dialog_home_status_please_input_location),
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            EtOfficeSetUserLocationPost(binding.userLocation.text.toString())
        }

        return binding.root
    }

    private fun EtOfficeSetUserStatusPost(
        longitude: Double,
        latitude: Double,
        location: String,
        statusvalue: String,
        statustext: String,
        memo: String
    ) {
        if (gpsTracker.canGetLocation()) {
            Api.EtOfficeSetUserStatus(
                context = requireActivity(),
                location = location,
                longitude = longitude,
                latitude = latitude,
                statusvalue = statusvalue,
                statustext = statustext,
                memo = memo,
                onSuccess = { model ->
                    Handler(Looper.getMainLooper()).post {

                        when (model.status) {
                            0 -> {
                                //Tools.showMsg(binding.root, "更新しました。")
                            }
                            else -> {
                                Snackbar.make(
                                    binding.root,
                                    model.message,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                onFailure = { error, data ->
                    Handler(Looper.getMainLooper()).post {
                        Log.e(TAG, "onFailure:$data")
                    }
                }
            )
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private fun EtOfficeSetUserLocationPost(location: String) {
        if (gpsTracker.canGetLocation()) {
            Api.EtOfficeSetUserLocation(
                context = requireActivity(),
                location = location,
                latitude = latitude,
                longitude = longitude,
                onSuccess = { model ->
                    Handler(Looper.getMainLooper()).post {

                        when (model.status) {
                            0 -> {
                                Tools.showMsg(binding.root, "地名を登録しました。")
                            }
                            else -> {
                                Snackbar.make(
                                    binding.root,
                                    model.message,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                },
                onFailure = { error, data ->
                    Handler(Looper.getMainLooper()).post {
                        Log.e(TAG, "onFailure:$data")
                    }
                }
            )
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private fun gpsCheck() {
        gpsTracker = GpsTracker(activity)
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.latitude
            longitude = gpsTracker.longitude
            binding.latitude.text = latitude.toString()
            binding.longitude.text = longitude.toString()
        } else {
            gpsTracker.showSettingsAlert()
        }
    }
}

package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.GpsTracker
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.databinding.DialogHomeStatusBinding


class HomeStatusDialog(statusvalue: String, statustext: String) : DialogFragment() {

    private val TAG = javaClass.simpleName

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
                binding.userLocation.toString(),
                mStatusvalue,
                mStatustext,
                binding.userStatusMemo.toString()
            )
        }

        //set_user_location
        binding.setUserLocation.setOnClickListener {
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
                                Tools.showMsg(binding.root, "登録します")
                            }
                            1 -> {
                                Snackbar.make(
                                    binding.root,
                                    model.message,
                                    Snackbar.LENGTH_LONG
                                ).show()
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
                latitude = latitude.toString(),
                longitude = longitude.toString(),
                onSuccess = { model ->
                    Handler(Looper.getMainLooper()).post {

                        when (model.status) {
                            0 -> {
                                Tools.showMsg(binding.root, "登録します")
                            }
                            1 -> {
                                Snackbar.make(
                                    binding.root,
                                    model.message,
                                    Snackbar.LENGTH_LONG
                                ).show()
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

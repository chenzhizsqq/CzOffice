package com.xieyi.etoffice.ui.myPage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.google.android.gms.location.*
import com.xieyi.etoffice.EtOfficeApp
//import com.xieyi.etoffice.GpsTracker
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

    //private lateinit var gpsTracker: GpsTracker
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

        //GPS check setting
        gpsInit()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for (location in p0.locations) {
                    Log.e(
                        "FullScreenDialogBaseFragment",
                        "gps onLocationResult: GPS位置：${location.latitude} , ${location.longitude}"
                    )
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        }

        return binding.root
    }


    private fun gpsInit() {
        //gps Permission check
        Tools.checkLocationPermission(requireActivity())

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback


    override fun onResume() {
        super.onResume()
        if (this::locationCallback.isInitialized) {
            startLocationUpdates()
        }
    }

    override fun onPause() {
        super.onPause()

        if (this::locationCallback.isInitialized) {
            stopLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        val locationRequest = createLocationRequest() ?: return
        if (ActivityCompat.checkSelfPermission(
                EtOfficeApp.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                EtOfficeApp.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest(): LocationRequest? {
        return LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }
}

package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.GpsTracker
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*

class HomeStatusDialog(statusvalue: String,statustext:String) : DialogFragment() {

    private val TAG = javaClass.simpleName

    val _statusvalue = statusvalue  //1

    val _statustext = statustext    //"勤務中"

    private lateinit var mainView: View

    private var longitude = 0.0

    private var latitude = 0.0


    private lateinit var gpsTracker: GpsTracker


    var listener: OnDialogListener? = null

    interface OnDialogListener  {
        fun onClick(userLocation:String, memo:String)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }


    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.dialog_home_status, container)



        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        //attributes.gravity = Gravity.BOTTOM //下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes


        //ボタン　保存後に閉じる
        val btnSaveAndClose = mainView.findViewById<TextView>(R.id.btn_cancel_and_close)
        btnSaveAndClose.setOnClickListener {

            val etUserLocation: EditText = mainView.findViewById(R.id.user_location)
            val userLocation: String = etUserLocation.text.toString()

            val etMemo: EditText = mainView.findViewById(R.id.user_status_memo)
            val memo: String = etMemo.text.toString()

            listener?.onClick(userLocation, memo)
            dialog!!.dismiss()
        }

        //状態表示
        val tvState:TextView= mainView.findViewById<TextView>(R.id.tvState)
        tvState.text = _statustext

        //set_user_Status
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                setUserStatus()
            }
        }

        //set_user_location
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                setUserLocation()
            }
        }

        gpsCheck()


        return mainView
    }

    private fun gpsCheck() {
        val tvLatitude = mainView.findViewById<TextView>(R.id.tvLatitude)
        val tvLongitude = mainView.findViewById<TextView>(R.id.tvLongitude)
        gpsTracker = GpsTracker(activity)
        if (gpsTracker.canGetLocation()) {
            latitude= gpsTracker.getLatitude()
            longitude = gpsTracker.getLongitude()
            tvLatitude.setText(latitude.toString())
            tvLongitude.setText(longitude.toString())
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private suspend fun setUserStatus() {
        withContext(Dispatchers.Main) {
            val etMemo: EditText = mainView.findViewById(R.id.user_status_memo)
            val setUserStatus: TextView = mainView.findViewById(R.id.set_user_status)
            setUserStatus.setOnClickListener {
                Log.e(TAG, "setUserStatus.setOnClickListener: begin")
                GlobalScope.launch(errorHandler) {
                    withContext(Dispatchers.IO) {
                        SetUserStatusPost(etMemo)
                    }
                }
            }
        }
    }

    private fun SetUserStatusPost(etMemo: EditText) {
        val memo: String = etMemo.text.toString()
        Log.e(TAG, "userStatus.text: $memo")
        try {
            val r: String =
                JC.pEtOfficeSetUserStatus.post(
                    longitude,
                    latitude,
                    "船橋事務所",
                    _statusvalue,
                    _statustext,
                    memo
                )                   //Json 送信
            Log.e(TAG, "pEtOfficeSetUserStatus.post() :$r")

            if (r == "0") {
                Tools.showMsg(mainView, "登録します")
            } else {
                Tools.showMsg(mainView, "pEtOfficeSetUserStatus.post() :$r")
            }
        } catch (e: Exception) {
            Log.e(TAG, "pEtOfficeSetUserStatus.post() :$e")

        }
    }

    private suspend fun setUserLocation() {
        withContext(Dispatchers.Main) {
            val userLocation: EditText = mainView.findViewById(R.id.user_location)
            val setUserLocation: TextView = mainView.findViewById<TextView>(R.id.set_user_location)
            setUserLocation.setOnClickListener {
                Log.e(TAG, "setUserLocation.setOnClickListener: begin")

                GlobalScope.launch(errorHandler) {
                    withContext(Dispatchers.IO) {
                        setUserLocationPost(userLocation)
                    }
                }

            }
        }
    }

    private fun setUserLocationPost(userLocation: EditText) {
        val s: String = userLocation.text.toString()
        Log.e(TAG, "userLocation.text: $s")
        try {
            val r: String =
                JC.pEtOfficeSetUserLocation.post(longitude, latitude, s)                   //Json 送信
            Log.e(TAG, "pEtOfficeSetUserLocation.post() :$r")

            if (r == "0") {
                Tools.showMsg(mainView, "登録します")
            } else {
                Tools.showMsg(mainView, "pEtOfficeSetUserLocation.post() :$r")
            }
        } catch (e: Exception) {
            Log.e(TAG, "pEtOfficeSetUserLocation.post() :$e")

        }
    }
}

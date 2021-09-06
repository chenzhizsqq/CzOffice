package com.xieyi.etoffice.ui.MyPage

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.xieyi.etoffice.*
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.UserLocationResult
import com.xieyi.etoffice.databinding.ActivityMyPagePlaceSettingBinding
import kotlinx.coroutines.*


class MyPagePlaceSettingActivity : BaseActivity(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = javaClass.simpleName

    private lateinit var gpsTracker: GpsTracker
    private var latitude = 0.0
    private var longitude = 0.0


    private lateinit var mAdapter: GetUserLocationAdapter
    private lateinit var binding: ActivityMyPagePlaceSettingBinding

    private fun gpsCheck() {

        gpsTracker = GpsTracker(applicationContext)
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.latitude
            longitude = gpsTracker.longitude
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPagePlaceSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        EtOfficeGetUserLocationPost()

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        gpsCheck()

    }

    private fun EtOfficeGetUserLocationPost() {
        Api.EtOfficeGetUserLocation(
            context = this@MyPagePlaceSettingActivity,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetUserLocationResult(model.result)
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
    }


    private fun EtOfficeSetUserLocationPost(location: String) {
        if (gpsTracker.canGetLocation()) {
            Api.EtOfficeSetUserLocation(
                context = this@MyPagePlaceSettingActivity,
                longitude = longitude.toString(),
                latitude = latitude.toString(),
                location = location,
                onSuccess = { model ->
                    Handler(Looper.getMainLooper()).post {

                        when (model.status) {
                            0 -> {
                                Tools.showMsg(binding.root, "登録します")
                                EtOfficeGetUserLocationPost()
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


    // EtOfficeGetUserLocationResult
    private fun EtOfficeGetUserLocationResult(result: UserLocationResult) {
        //record_title
        binding.recordTitle.text = EtOfficeApp.context.getString(R.string.REGISTERED_title)


        mAdapter = GetUserLocationAdapter(result.locationlist)
        binding.recyclerView.adapter = mAdapter

        //returnpHome
        binding.returnHome.setOnClickListener {
            val intent: Intent = Intent(this@MyPagePlaceSettingActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

        }

        //locationAlertDialog
        binding.locationAlertDialog.setOnClickListener {

            val textInputLayout = TextInputLayout(this@MyPagePlaceSettingActivity)
            val input = EditText(this@MyPagePlaceSettingActivity)
            input.maxLines = 1
            input.inputType = InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT
            textInputLayout.addView(input)

            AlertDialog.Builder(this@MyPagePlaceSettingActivity)
                .setTitle("Message")
                .setMessage("Please enter an alias for the current location.")
                .setView(textInputLayout)
                .setPositiveButton("OK") { _, which ->
                    //Log.e(TAG, "AlertDialog 确定:"+input.text.toString() )

                    val location = input.text.toString()
                    gpsCheck()
                    EtOfficeSetUserLocationPost(location)

                }
                .show()


        }

    }


    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
        EtOfficeGetUserLocationPost()
    }
}
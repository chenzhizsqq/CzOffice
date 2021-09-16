package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.*
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.UserLocationResult
import com.xieyi.etoffice.databinding.ActivityMyPagePlaceSettingBinding
import kotlinx.coroutines.*


class MyPagePlaceSettingActivity : BaseActivity(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = "MyPagePlaceSettingActivity"

    private lateinit var gpsTracker: GpsTracker
    private var latitude = 0.0
    private var longitude = 0.0

    private lateinit var mAdapter: GetUserLocationAdapter
    private lateinit var binding: ActivityMyPagePlaceSettingBinding

    private fun gpsCheck(): Boolean {

        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.latitude
            longitude = gpsTracker.longitude
            return true
        } else {
            gpsTracker.showSettingsAlert()
        }
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPagePlaceSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        gpsTracker = GpsTracker(this@MyPagePlaceSettingActivity)

        EtOfficeGetUserLocationPost()

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

    private fun EtOfficeSetUserLocationPost(location: String, longitude: Double, latitude: Double) {
        if (gpsTracker.canGetLocation()) {
            Api.EtOfficeSetUserLocation(
                context = this@MyPagePlaceSettingActivity,
                longitude = longitude,
                latitude = latitude,
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

            if (gpsCheck()) {
                val mMyPagePlaceDialog = MyPagePlaceDialog()

                val fm: FragmentManager = supportFragmentManager
                fm.let { it1 -> mMyPagePlaceDialog.show(it1, "mMyPagePlaceDialog") }

                mMyPagePlaceDialog.setOnDialogListener(object : MyPagePlaceDialog.OnDialogListener {
                    override fun onClick(location: String, longitude: Double, latitude: Double) {
                        EtOfficeSetUserLocationPost(location, longitude, latitude)
                    }
                })
            }

        }

    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
        EtOfficeGetUserLocationPost()
    }
}
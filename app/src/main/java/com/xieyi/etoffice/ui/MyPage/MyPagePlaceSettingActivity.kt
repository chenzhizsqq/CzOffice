package com.xieyi.etoffice.ui.MyPage

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputLayout
import com.xieyi.etoffice.*
import com.xieyi.etoffice.databinding.ActivityMyPagePlaceSettingBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetUserLocation
import com.xieyi.etoffice.jsonData.EtOfficeSetUserLocation

import kotlinx.coroutines.*


class MyPagePlaceSettingActivity : AppCompatActivity(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = javaClass.simpleName

    private lateinit var gpsTracker: GpsTracker
    private var longitude = 0.0
    private var latitude = 0.0

    private lateinit var pEtOfficeGetUserLocation : EtOfficeGetUserLocation
    private lateinit var pEtOfficeSetUserLocation : EtOfficeSetUserLocation


    private lateinit var mAdapter:GetUserLocationAdapter
    private lateinit var binding: ActivityMyPagePlaceSettingBinding

    private fun gpsCheck() {

        gpsTracker = GpsTracker(applicationContext)
        if (gpsTracker.canGetLocation()) {
            latitude= gpsTracker.getLatitude()
            longitude = gpsTracker.getLongitude()
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityMyPagePlaceSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pEtOfficeGetUserLocation = EtOfficeGetUserLocation()
        pEtOfficeSetUserLocation = EtOfficeSetUserLocation()

        refreshPage()

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this);

        gpsCheck()

    }

    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r: String = pEtOfficeGetUserLocation.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$r")


                    doOnUiCode()
                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$e")

                }

            }
        }
    }


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }



    // UI更新
    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {

            //record_title
            binding.recordTitle.text = "REGISTERED"


            mAdapter= GetUserLocationAdapter(pEtOfficeGetUserLocation.infoJson().result.locationlist)
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
                        postLocation(location)

                    }
                    .setNegativeButton("Cancel") { _, which ->
                    }
                    .show()


            }

        }
    }

    private fun postLocation(location: String) {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                if (gpsTracker.canGetLocation()) {
                    try {
                        val r: String =
                            pEtOfficeSetUserLocation.post(
                                longitude.toString(),
                                latitude.toString(),
                                location
                            )                   //Json 送信
                        Log.e(TAG, "pEtOfficeSetUserLocation.post() :$r")

                        if (r == "0") {
                            Tools.showMsg(binding.root, "登録します")
                        } else {
                            Tools.showMsg(
                                binding.root,
                                pEtOfficeSetUserLocation.infoJson().message
                            )
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "pEtOfficeSetUserLocation.post()", e)

                    }

                    refreshPage()
                } else {
                    gpsTracker.showSettingsAlert()
                }
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false;
        refreshPage()
    }
}
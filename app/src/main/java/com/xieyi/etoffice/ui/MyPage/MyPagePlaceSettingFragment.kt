package com.xieyi.etoffice.ui.MyPage

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import com.xieyi.etoffice.GpsTracker
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*


class MyPagePlaceSettingFragment : Fragment() {
    private val TAG = "MyPagePlaceSettingFragment"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    private val tagName: String = "PlaceSetting"

    private lateinit var gpsTracker: GpsTracker
    private var longitude = 0.0
    private var latitude = 0.0

    private fun gpsCheck() {

        gpsTracker = GpsTracker(activity)
        if (gpsTracker.canGetLocation()) {
            latitude= gpsTracker.getLatitude()
            longitude = gpsTracker.getLongitude()
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.e(TAG, "onCreate: begin")
    }
    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = inflater.inflate(R.layout.activity_my_page_place_setting, container, false)


        refreshPage()

        gpsCheck()

        return mainView
    }

    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r: String = JC.pEtOfficeGetUserLocation.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$r")


                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$e")

                }

                doOnUiCode()
            }
        }
    }


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }



    // UI更新
    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            val recordLinearLayout = mainView.findViewById<LinearLayout>(R.id.record_linearLayout)
            recordLinearLayout.removeAllViews()

            val size = JC.pEtOfficeGetUserLocation.infoJson().result.locationlist.size



            Log.e(TAG, "locationlist.size: $size")


            for (i in 0..size - 1) {
                //LinearLayout init
                val mLinearLayout = LinearLayout(activity)
                mLinearLayout.setOrientation(LinearLayout.HORIZONTAL)
                mLinearLayout.gravity = (Gravity.CENTER or Gravity.LEFT)
                mLinearLayout.layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)

                //mLinearLayout tag
                mLinearLayout.tag = tagName + "_" + i

                //location info


                //image logo
                val imageView = ImageView(activity)
                val myDrawable = ResourcesCompat.getDrawable(
                    resources, R.drawable.ic_baseline_adjust_24, null
                )

                //image logo size
                val layoutParams = LinearLayout.LayoutParams(50, 50)
                imageView.layoutParams = layoutParams

                //image logo add
                imageView.setImageDrawable(myDrawable)
                mLinearLayout.addView(imageView)



                val latitude = JC.pEtOfficeGetUserLocation.infoJson().result.locationlist[i].latitude
                val longitude = JC.pEtOfficeGetUserLocation.infoJson().result.locationlist[i].longitude

                val textView = TextView(activity)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
                textView.setTextColor(Color.parseColor("#000000"))
                textView.text =
                    JC.pEtOfficeGetUserLocation.infoJson().result.locationlist[i].location + "|latitude:"+latitude + "|longitude:"+longitude
                mLinearLayout.addView(textView)

                //design
                mLinearLayout.setBackgroundColor(Color.WHITE)
                mLinearLayout.setPadding(30)


                //mLinearLayout touch   begin
                mLinearLayout.setOnClickListener {
                    try {
                        //all linearLayouts of recordLinearLayout change WHITE
                        for (j in 0..size - 1) {
                            val ll =
                                recordLinearLayout.findViewWithTag<LinearLayout>(tagName + "_" + j)
                            ll.setBackgroundColor(Color.WHITE)
                        }

                        //!!!!!!!!!!!!!!!!!!!   set place   !!!!!!!!!!!!!!!!!!!
                        var r: String = "0"


                        //this mLinearLayout green
                        if (r == "0") {
                            mLinearLayout.setBackgroundColor(Color.GREEN)
                        }

                    } catch (e: Exception) {
                        Log.e(TAG, "mLinearLayout.setOnClickListener: ", e)
                    }
                }
                //mLinearLayout touch end

                //over to add
                recordLinearLayout.addView(mLinearLayout)


                //線
                val mLinearLayout2 = LinearLayout(activity)
                val lp2 = LinearLayout.LayoutParams(MATCH_PARENT, 1)
                mLinearLayout2.layoutParams = lp2
                mLinearLayout2.setBackgroundColor(Color.parseColor("#656565"))
                recordLinearLayout.addView(mLinearLayout2)
            }


            //returnpHome
            val returnHome = mainView.findViewById<ImageView>(R.id.returnHome)
            returnHome.setOnClickListener {
                Navigation.findNavController(mainView)
                    .navigate(R.id.MyPageFragment);

            }

            //locationAlertDialog
            val locationAlertDialog = mainView.findViewById<ImageView>(R.id.locationAlertDialog)
            locationAlertDialog.setOnClickListener {
                activity?.let { it1 ->

                    val textInputLayout = TextInputLayout(it1)
                    val input = EditText(context)
                    input.maxLines = 1
                    input.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT)
                    textInputLayout.addView(input)

                    AlertDialog.Builder(context)
                        .setTitle("Message")
                        .setMessage("Please enter an alias for the current location.")
                        .setView(textInputLayout)
                        .setPositiveButton("OK") { _, which ->
                            Log.e(TAG, "AlertDialog 确定:"+input.text.toString() )

                            val location = input.text.toString()
                            postLocation(location)

                        }
                        .setNegativeButton("Cancel") { _, which ->
                        }
                        .show()

                }

            }

        }
    }

    private fun postLocation(location: String) {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                if (gpsTracker.canGetLocation()) {
                    try {
                        val r: String =
                            JC.pEtOfficeSetUserLocation.post(
                                longitude.toString(),
                                latitude.toString(),
                                location
                            )                   //Json 送信
                        Log.e(TAG, "pEtOfficeSetUserLocation.post() :$r")

                        if (r == "0") {
                            Tools.showMsg(mainView, "登録します")
                        } else {
                            Tools.showMsg(
                                mainView,
                                JC.pEtOfficeSetUserLocation.infoJson().message
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
}
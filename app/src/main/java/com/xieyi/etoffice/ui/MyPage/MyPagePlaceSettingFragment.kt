package com.xieyi.etoffice.ui.MyPage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*


class MyPagePlaceSettingFragment : Fragment() {
    private val TAG = "MyPagePlaceSettingFragment"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    private val tagName: String = "PlaceSetting"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, "onCreate: begin", )
    }
    private lateinit var mainView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView = inflater.inflate(R.layout.activity_my_page_place_setting, container, false)


        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r: String = JC.pEtOfficeGetUserLocation.post()                   //Json 送信
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$r")


                }catch (e:Exception){
                    Log.e(TAG, "pEtOfficeGetUserLocation.post() :$e")

                }

                doOnUiCode()
            }
        }

        return mainView
    }


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }



    // UI更新
    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            val size = JC.pEtOfficeGetUserLocation.infoJson().result.locationlist.size

            //info

            val recordLinearLayout = mainView.findViewById<LinearLayout>(R.id.record_linearLayout)

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
                    resources, R.drawable.icons8_plus_50, null
                )

                //image logo size
                val layoutParams = LinearLayout.LayoutParams(50, 50)
                imageView.layoutParams = layoutParams

                //image logo add
                imageView.setImageDrawable(myDrawable)
                mLinearLayout.addView(imageView)

                val textView = TextView(activity)
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14F);
                textView.setTextColor(Color.parseColor("#000000"))
                textView.text =
                    JC.pEtOfficeGetUserLocation.infoJson().result.locationlist[i].location
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
        }
    }
}
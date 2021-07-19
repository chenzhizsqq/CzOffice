package com.xieyi.etoffice.ui.MyPage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.xieyi.etoffice.R

class MyPagePlaceSettingFragment : Fragment() {
    private val TAG = "MyPagePlaceSettingFragment"

    private val WRAP_CONTENT = LinearLayout.LayoutParams.WRAP_CONTENT
    private val MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_my_page_place_setting, container, false)


        //returnHome
        val returnHome = view.findViewById<ImageView>(R.id.returnHome)
        returnHome.setOnClickListener {
            Thread {
                try {

                    Navigation.findNavController(view)
                        .navigate(R.id.MyPageFragment);
                }catch (e:Exception){
                    Log.e(TAG, "returnHome: ",e )
                }
            }.start()

        }

        return view
    }
}
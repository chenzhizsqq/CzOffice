package com.xieyi.etoffice

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.gson.Gson
import com.xieyi.etoffice.jsonData.*

class MainActivity : AppCompatActivity() {
    val TAG:String ="MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        Thread {
            var r: String = ""

            r = EtOfficeUserInfo.post()
            Log.e(TAG, "onCreate: r==$r")
            Log.e("infoUserStatusList 0",
                EtOfficeUserInfo.infoUserStatusList().toString()
            )

            r = EtOfficeGetUserStatus.post()
            Log.e(TAG, "infoUserStatusList: r==$r")
            Log.e("infoUserStatusList 0",
                EtOfficeGetUserStatus.infoUserStatusList(0,).toString()
            )

            r = EtOfficeSetUserStatus.post()
            Log.e(TAG, "onCreate: r==$r")
            Log.e("infoUserStatusList 0",
                EtOfficeSetUserStatus.infoUserStatusList(0,).toString()
            )

            r = EtOfficeGetUserLocation.post()
            Log.e(TAG, "EtOfficeGetUserLocation.post(): r==$r")
            Log.e("infoUserstatuslist 0",
                EtOfficeGetUserLocation.infoUserstatuslist(0,).toString()
            )

            r = EtOfficeSetUserLocation.post()
            Log.e(TAG, "EtOfficeSetUserLocation.post(): r==$r")
            Log.e("infoLocationList 0",
                EtOfficeSetUserLocation.infoLocationList(0,).toString()
            )

            r = EtOfficeGetTenant.post()
            Log.e(TAG, "EtOfficeGetTenant.post(): r==$r")
            Log.e("infoTenantList 0",
                EtOfficeGetTenant.infoTenantList(0).toString()
            )

            r = EtOfficeSetTenant.post()
            Log.e(TAG, "EtOfficeSetTenant.post(): r==$r")
            Log.e("infoTenantList 0",
                EtOfficeSetTenant.infoTenantList(0).toString()
            )

            r = EtOfficeGetMessage.post()
            Log.e(TAG, "EtOfficeGetMessage.post(): r==$r")
            Log.e(TAG,"infoRecordlist 0"+
                    EtOfficeGetMessage.infoRecordlist(0).toString()
            )
            Log.e(TAG,"getResult():"+
                    EtOfficeGetMessage.getResult()
            )

            r = EtOfficeGetStuffList.post()
            Log.e(TAG, "EtOfficeGetStuffList post(): r==$r")
            Log.e(TAG,"EtOfficeGetStuffList result:"
                    +EtOfficeGetStuffList.getResult().toString()
            )


            //EtOfficeSetMessage test
            var arrayString: Array<String> = emptyArray()
            arrayString += "2020111320070768"
            arrayString += "2020111320065968"
            arrayString += "2020111319510968"

            r = EtOfficeSetMessage.post(arrayString)
            Log.e(TAG, "EtOfficeSetMessageJson post(Tools.array2String(arrayString)): r==$r")
            Log.e(TAG,"EtOfficeSetMessageJson result:"
                    + EtOfficeSetMessage.getResult()
            )

        }.start()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home
            , R.id.navigation_notifications
            , R.id.member_fragment
            ,R.id.ScrollingFragmentReport
            ,R.id.MyPageFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    data class Bean(
        val code:Int,
        val msg:String
    )

    fun Any.toJson(): String = Gson().toJson(this)
}
package com.xieyi.etoffice

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.xieyi.etoffice.jsonData.EtOfficeGetUserStatus

class MainActivity : AppCompatActivity() {
    val TAG:String ="MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        Thread {
            var r: String = ""

//            r = EtOfficeUserInfo.post()
//            Log.e(TAG, "onCreate: r==$r")
//            Log.e("infoUserStatusList 0",
//                EtOfficeUserInfo.infoUserStatusList().toString()
//            )

            r = EtOfficeGetUserStatus.post()
            Log.e(TAG, "infoUserStatusList: r==$r")
            Log.e("infoUserStatusList 0",
                EtOfficeGetUserStatus.infoUserStatusList(0,).toString()
            )

//            r = EtOfficeSetUserStatus.post()
//            Log.e(TAG, "onCreate: r==$r")
//            Log.e("infoUserStatusList 0",
//                EtOfficeSetUserStatus.infoUserStatusList(0,).toString()
//            )
//
//            r = EtOfficeGetUserLocation.post()
//            Log.e(TAG, "EtOfficeGetUserLocation.post(): r==$r")
//            Log.e("infoUserstatuslist 0",
//                EtOfficeGetUserLocation.infoUserstatuslist(0,).toString()
//            )
//
//            r = EtOfficeSetUserLocation.post()
//            Log.e(TAG, "EtOfficeSetUserLocation.post(): r==$r")
//            Log.e("infoLocationList 0",
//                EtOfficeSetUserLocation.infoLocationList(0,).toString()
//            )
//
//            r = EtOfficeGetTenant.post()
//            Log.e(TAG, "EtOfficeGetTenant.post(): r==$r")
//            Log.e("infoTenantList 0",
//                EtOfficeGetTenant.infoTenantList(0).toString()
//            )
//
//            r = EtOfficeSetTenant.post()
//            Log.e(TAG, "EtOfficeSetTenant.post(): r==$r")
//            Log.e("infoTenantList 0",
//                EtOfficeSetTenant.infoTenantList(0).toString()
//            )
//
//            r = EtOfficeGetMessage.post()
//            Log.e(TAG, "EtOfficeGetMessage.post(): r==$r")
//            Log.e("infoRecordlist 0",
//                EtOfficeGetMessage.infoRecordlist(0).toString()
//            )

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
}
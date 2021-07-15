package com.xieyi.etoffice

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.xieyi.etoffice.jsonData.JC

class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"
    lateinit var view: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(android.R.id.content)

        testJson()

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home,
            R.id.navigation_notifications,
            R.id.member_fragment,
            R.id.ScrollingFragmentReport,
            R.id.MyPageFragment
        )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    fun testJson() {

        Thread {
            try {
                var r: String = ""

                r = JC.pEtOfficeUserInfo.post()
//                Log.e(TAG, "onCreate: r==$r")
//                Log.e("infoUserStatusList 0",
//                    EtOfficeUserInfo.infoUserStatusList().toString()
//                )

                r = JC.pEtOfficeGetUserStatus.post()
//                Log.e(TAG, "infoUserStatusList: r==$r")
//                Log.e("infoUserStatusList 0",
//                    EtOfficeGetUserStatus.infoUserStatusList(0).toString()
//                )

                r = JC.pEtOfficeSetUserStatus.post()
//                Log.e(TAG, "onCreate: r==$r")
//                Log.e("infoUserStatusList 0",
//                    EtOfficeSetUserStatus.infoUserStatusList(0).toString()
//                )

                r = JC.pEtOfficeGetUserLocation.post()
//                Log.e(TAG, "EtOfficeGetUserLocation.post(): r==$r")
//                Log.e("infoUserstatuslist 0",
//                    EtOfficeGetUserLocation.infoUserstatuslist(0).toString()
//                )

                r = JC.pEtOfficeSetUserLocation.post()
//                Log.e(TAG, "EtOfficeSetUserLocation.post(): r==$r")
//                Log.e("infoLocationList 0",
//                    EtOfficeSetUserLocation.infoLocationList(0).toString()
//                )

                r = JC.pEtOfficeGetTenant.post()
//                Log.e(TAG, "EtOfficeGetTenant.post(): r==$r")
//                Log.e("infoTenantList 0",
//                    EtOfficeGetTenant.infoTenantList(0).toString()
//                )

                r = JC.pEtOfficeSetTenant.post()
//                Log.e(TAG, "EtOfficeSetTenant.post(): r==$r")
//                Log.e("infoTenantList 0",
//                    EtOfficeSetTenant.infoTenantList(0).toString()
//                )

                r = JC.pEtOfficeGetMessage.post()
//                Log.e(TAG, "EtOfficeGetMessage.post(): r==$r")
//                Log.e(TAG,"infoRecordlist 0"+
//                        EtOfficeGetMessage.infoRecordlist(0).toString()
//                )
//                Log.e(TAG,"getResult():"+
//                        EtOfficeGetMessage.getResult()
//                )

                r = JC.pEtOfficeGetStuffList.post()
//                Log.e(TAG, "EtOfficeGetStuffList post(): r==$r")
//                Log.e(TAG,"EtOfficeGetStuffList result:"
//                        +EtOfficeGetStuffList.getResult().toString()
//                )


                //EtOfficeSetMessage test
                var arrayString: Array<String> = emptyArray()
                arrayString += "2020111320070768"
                arrayString += "2020111320065968"
                arrayString += "2020111319510968"

                r = JC.pEtOfficeSetMessage.post(arrayString)
//                Log.e(TAG, "EtOfficeSetMessageJson post(Tools.array2String(arrayString)): r==$r")
//                Log.e(TAG,"EtOfficeSetMessageJson result:"
//                        + EtOfficeSetMessage.getResult()
//                )


                //EtOfficeJson test
                /*
                var pEtOfficeJson = EtOfficeJson()
                val jsonObject = JSONObject()
                jsonObject.put("app", EtOfficeUserInfo.app)
                jsonObject.put("token", jsonCenter.pEtOfficeLogin.infoLoginResult().token)
                jsonObject.put("tenant", jsonCenter.pEtOfficeLogin.infoLoginResult().tenantid)
                jsonObject.put("hpid", jsonCenter.pEtOfficeLogin.infoLoginResult().hpid)
                jsonObject.put("device","android")
                 */

                //EtOfficeGetStatusList test
                r = JC.pEtOfficeGetStatusList.post()
                /*
                Log.e(TAG, "EtOfficeGetStatusList post(): r==$r")
                Log.e(TAG,"EtOfficeGetStatusList getJson:"
                        +EtOfficeGetStatusList.getJson().toString()
                )
                 */

                //EtOfficeGetReportList test
                r = JC.pEtOfficeGetReportList.post()

                //EtOfficeSetApprovalJsk test
                r = JC.pEtOfficeSetApprovalJsk.post()

                //EtOfficeGetReportInfo test
                r = JC.pEtOfficeGetReportInfo.post()
                Log.e(TAG,"pEtOfficeGetReportInfo infoResult:"
                        +JC.pEtOfficeGetReportInfo.infoResult().toString()
                )


                //pEtOfficeGetProject プロジェクト一覧
                r = JC.pEtOfficeGetProject.post()
                Log.e(TAG,"pEtOfficeGetProject infoResult:"
                        +JC.pEtOfficeGetProject.infoResult().toString()
                )


            } catch (e: Exception) {

                Snackbar.make(view, "Error:$e", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
                Log.e(TAG, "TAG", e)
            }


        }.start()
    }

    fun Any.toJson(): String = Gson().toJson(this)
}
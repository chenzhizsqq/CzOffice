package com.xieyi.etoffice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.xieyi.etoffice.jsonData.JC
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity"
    lateinit var view: View


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view = findViewById(android.R.id.content)
        supportActionBar?.hide()

        //gps検査する
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //Jsonテスト   begin
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {

                //特別データ　テスト
//                try {
//                    var r:String ="-1"
//                    r= JC.pEtOfficeGetProject.post()
//                    Log.e(TAG, "pEtOfficeGetProject.post():$r")
//                } catch (e: Exception) {
//
//                    Snackbar.make(view, "Error:$e", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null)
//                        .show()
//                    Log.e(TAG, "TAG", e)
//                }

                //全部データ　テスト
                try {
                    //testJson()
                } catch (e: Exception) {
                    Log.e(TAG, "testJson catch:$e")
                }

            }
        }
        //Jsonテスト   end

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home,
            R.id.navigation_notifications,
            R.id.member_fragment,
            R.id.report_fragment,
            R.id.MyPageFragment
        )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    fun testJson() {
        Log.e(TAG, "testJson() begin" )

            try {
                var r: String = ""

                r = JC.pEtOfficeUserInfo.post()
                Log.e(TAG, "onCreate: r==$r")
                Log.e(TAG,"pEtOfficeUserInfo --- "+
                    JC.pEtOfficeUserInfo.infoJson().toString()
                )

                //  3	出勤記録    HomeReportDialogで、完成しました
                r = JC.pEtOfficeGetUserStatus.post()
                Log.e(TAG, "pEtOfficeGetUserStatus: r==$r")
                Log.e(TAG, "pEtOfficeGetUserStatus --- "+
                    JC.pEtOfficeGetUserStatus.infoJson().toString()
                )

                r = JC.pEtOfficeSetUserStatus.post("test")
//                Log.e(TAG, "onCreate: r==$r")
//                Log.e(TAG, "pEtOfficeSetUserStatus --- "+
//                    JC.pEtOfficeSetUserStatus.infoJson().toString()
//                )

                r = JC.pEtOfficeGetUserLocation.post()
                Log.e(TAG, "EtOfficeGetUserLocation.post(): r==$r")
                Log.e(TAG, "pEtOfficeGetUserLocation --- "+
                    JC.pEtOfficeGetUserLocation.infoJson().toString()
                )

                r = JC.pEtOfficeSetUserLocation.post("test")
                Log.e(TAG, "EtOfficeSetUserLocation.post(): r==$r")
                Log.e(TAG,"pEtOfficeSetUserLocation --- "+
                        JC.pEtOfficeSetUserLocation.infoJson().toString()
                )

                r = JC.pEtOfficeGetTenant.post()
                Log.e(TAG, "pEtOfficeGetTenant.post(): r==$r")
                Log.e("pEtOfficeGetTenant --- ",
                    JC.pEtOfficeGetTenant.infoJson().toString()
                )

//                r = JC.pEtOfficeSetTenant.post()
//                Log.e(TAG, "pEtOfficeSetTenant.post(): r==$r")
//                Log.e("pEtOfficeSetTenant --- ",
//                    JC.pEtOfficeSetTenant.infoJson().toString()
//                )

                r = JC.pEtOfficeGetMessage.post()
                Log.e(TAG, "pEtOfficeGetMessage.post(): r==$r")
                Log.e(TAG,"pEtOfficeGetMessage --- "+
                        JC.pEtOfficeGetMessage.infoJson().toString()
                )

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
                r = JC.pEtOfficeGetReportInfo.post("20210305")
                Log.e(TAG,"pEtOfficeGetReportInfo infoResult:"
                        +JC.pEtOfficeGetReportInfo.infoResult().toString()
                )


                //pEtOfficeGetProject プロジェクト一覧
                r = JC.pEtOfficeGetProject.post()
                Log.e(TAG,"pEtOfficeGetProject infoResult:"
                        +JC.pEtOfficeGetProject.infoResult().toString()
                )

                //EtOfficeSetReport     日報登録
                r = JC.pEtOfficeSetReport.post()
                Log.e(TAG,"EtOfficeSetReport infoJson:"
                        +JC.pEtOfficeSetReport.infoJson()
                )


                //EtOfficeSetComment    コメント登録
                r = JC.pEtOfficeSetComment.post()
                Log.e(TAG,"EtOfficeSetComment infoJson:"
                        + JC.pEtOfficeSetComment.infoJson()?.result.toString()
                )


            } catch (e: Exception) {

                Snackbar.make(view, "Error:$e", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show()
                Log.e(TAG, "TAG", e)
            }


        Log.e(TAG, "testJson() end" )
    }

    fun Any.toJson(): String = Gson().toJson(this)


}
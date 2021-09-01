package com.xieyi.etoffice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : BaseActivity() {
    val TAG: String = javaClass.simpleName

    private lateinit var binding: ActivityMainBinding

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }


    //fragのリスト
    private val listFrag = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //gps検査する
        gpsCheck()

        //Jsonテスト   begin
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {

                //特別データ　テスト
//                try {
//                    var r:String ="-1"
//                    r = pEtOfficeSetComment.post("20210802","ok")
//                    Log.e(TAG,"EtOfficeSetComment infoJson:"
//                            + pEtOfficeSetComment.infoJson().result.toString()
//                    )
//                    Log.e(TAG, "onCreate: pEtOfficeSetComment r:$r" )
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

        val navView: BottomNavigationView = binding.navView

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


        //fragのリスト
        listFrag.clear()
        listFrag.add(R.id.navigation_home)
        listFrag.add(R.id.navigation_notifications)
        listFrag.add(R.id.member_fragment)
        listFrag.add(R.id.report_fragment)
        listFrag.add(R.id.MyPageFragment)

        //初回登録Frag
        //val index = EtOfficeApp.selectUi
        val index = Tools.sharedPreGetInt(Config.FragKey)
        selectFrag(index)

        //初回登録した後、KEY削除します
        Tools.sharedPreRemove(Config.FragKey)
    }

    //选择frag登录
    private fun selectFrag(index: Int) {
        val mNavigationView: BottomNavigationView = binding.navView
        mNavigationView.menu.getItem(index).isChecked = true;
        mNavigationView.menu.performIdentifierAction(listFrag[index], index)
    }


    //gps検査する
    private fun gpsCheck() {
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
    }


}
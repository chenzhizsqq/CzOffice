package com.xieyi.etoffice

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.forEachIndexed
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.xieyi.etoffice.base.BaseActivity
import com.xieyi.etoffice.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    val TAG: String = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    //fragのリスト
    private val listFrag = ArrayList<Int>()

    //与MainActivity共同的ViewModel
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_notifications,
                R.id.member_fragment,
                R.id.report_fragment,
                R.id.MyPageFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED

        //fragのリスト
        listFrag.clear()
        listFrag.add(R.id.navigation_home)
        listFrag.add(R.id.navigation_notifications)
        listFrag.add(R.id.member_fragment)
        listFrag.add(R.id.report_fragment)
        listFrag.add(R.id.MyPageFragment)

        //初回登録Frag
        val index = Tools.sharedPreGetInt(Config.FragKey)
        selectFrag(index)

        //初回登録した後、KEY削除します
        Tools.sharedPreRemove(Config.FragKey)

        //把reportFrag页改名字
        viewModel.reportFragTitle.observe(this, Observer {
            binding.navView.menu.forEachIndexed { index, item ->
                when (index) {
                    3 -> item.title = it
                }
            }
        })
    }

    //选择frag登录
    private fun selectFrag(index: Int) {
        val mNavigationView: BottomNavigationView = binding.navView
        mNavigationView.menu.getItem(index).isChecked = true;
        mNavigationView.menu.performIdentifierAction(listFrag[index], index)
    }


}
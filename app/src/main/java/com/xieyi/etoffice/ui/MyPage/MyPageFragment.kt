package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.R
import com.xieyi.etoffice.databinding.FragmentMyPageBinding
import com.xieyi.etoffice.jsonData.EtOfficeUserInfo

import kotlinx.coroutines.*


class MyPageFragment : Fragment() {

    private val TAG: String = javaClass.simpleName
    private lateinit var pEtOfficeUserInfo : EtOfficeUserInfo

    private lateinit var binding: FragmentMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         pEtOfficeUserInfo = EtOfficeUserInfo()

    }


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private fun mainViewUpdate() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                //データ更新
                try {
                    val r =
                        pEtOfficeUserInfo.post()                                    //Json 送信
                    Log.e(TAG, "pEtOfficeUserInfo.post() :$r")

                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeUserInfo.post()",e)
                }

                doOnUiCode()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Log.e(TAG, "onCreateView: begin")
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        mainViewUpdate()

        return binding.root
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            try {

                binding.userName.text = pEtOfficeUserInfo.infoJson().result.username

                binding.userMail.text = pEtOfficeUserInfo.infoJson().result.mail

                binding.nameValue.text = pEtOfficeUserInfo.infoJson().result.username

                binding.mobileValue.text = pEtOfficeUserInfo.infoJson().result.phone

                binding.mailValue.text = pEtOfficeUserInfo.infoJson().result.mail

            } catch (e: Exception) {
                Log.e(TAG, "doOnUiCode", e)
            }

            //Place　Setting
            binding.placeManagement.setOnClickListener(View.OnClickListener {

                EtOfficeApp.selectUi = 4
                val intent = Intent(activity, MyPagePlaceSettingActivity::class.java)
                startActivity(intent)
                activity?.finish()

            })

            //change　company
            binding.changeCompany.setOnClickListener(View.OnClickListener {

                EtOfficeApp.selectUi = 4
                val intent = Intent(activity, MyPageChangeCompanyActivity::class.java)
                startActivity(intent)
                activity?.finish()
            })


            //ログアウト
            binding.SYSTEMInfo.setOnClickListener(View.OnClickListener {

                val mMyPageLogoutDialog = MyPageLogoutDialog()
                val fragmentManager = this@MyPageFragment.parentFragmentManager
                fragmentManager.let { it1 -> mMyPageLogoutDialog.show(it1, "mMyPageLogoutDialog") }

            })
        }
    }


    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

}
package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.UserInfoResult
import com.xieyi.etoffice.databinding.FragmentMyPageBinding

import kotlinx.coroutines.*


class MyPageFragment : Fragment() {

    private val TAG: String = javaClass.simpleName

    private lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Log.e(TAG, "onCreateView: begin")
        binding = FragmentMyPageBinding.inflate(inflater, container, false)

        initView()

        EtOfficeUserInfoPost()

        return binding.root
    }


    private fun EtOfficeUserInfoPost() {

        Api.EtOfficeUserInfo(
            context = requireContext(),
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeUserInfoResult(model.result)
                        }
                        else -> {
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data")
                }
            }
        )
    }


    private fun EtOfficeUserInfoResult(result: UserInfoResult) {
        try {

            binding.userName.text = result.username

            binding.userMail.text = result.mail

            binding.nameValue.text = result.username

            binding.mobileValue.text = result.phone

            binding.mailValue.text = result.mail

        } catch (e: Exception) {
            Log.e(TAG, "doOnUiCode", e)
        }
    }

    private fun initView() {
        //Place　Setting
        binding.placeManagement.setOnClickListener(View.OnClickListener {

            Tools.sharedPrePut(Config.FragKey, 4)
            //EtOfficeApp.selectUi = 4
            val intent = Intent(activity, MyPagePlaceSettingActivity::class.java)
            startActivity(intent)
            activity?.finish()

        })

        //change　company
        binding.changeCompany.setOnClickListener(View.OnClickListener {

            Tools.sharedPrePut(Config.FragKey, 4)
            //EtOfficeApp.selectUi = 4
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


    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

}
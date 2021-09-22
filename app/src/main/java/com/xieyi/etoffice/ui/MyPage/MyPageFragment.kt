package com.xieyi.etoffice.ui.MyPage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.UserInfoResult
import com.xieyi.etoffice.databinding.FragmentMyPageBinding
import com.xieyi.etoffice.ui.login.LoginActivity

import kotlinx.coroutines.*


class MyPageFragment : Fragment() {

    private val TAG: String = "MyPageFragment"

    private lateinit var binding: FragmentMyPageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                            activity?.let {
                                Tools.showErrorDialog(
                                    it,
                                    model.message
                                )
                            }
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
            activity?.let { it1 ->
                Tools.showConfirmDialog(it1,
                    getString(R.string.confirmation),
                    getString(R.string.dialog_my_page_logout_confirm),
                    yesListener = { _, _ ->
                        // ログアウト
                        logout()
                    },
                    noListener = { _, _ ->
                        // 何もしない
                    }
                )
            }
        })
    }

    /**
     * ログアウト処理
     */
    private fun logout() {
        // 登録されたユーザ情報を削除
        val userInfo = requireActivity().getSharedPreferences(
            Config.EtOfficeUser,
            AppCompatActivity.MODE_PRIVATE
        )
        userInfo?.edit()?.clear()?.commit()

        //消除所有的Activity
        val intent = Intent(activity, LoginActivity::class.java)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        startActivity(intent)
        activity?.finish()
    }

    companion object {
        fun newInstance(): MyPageFragment {
            return MyPageFragment()
        }
    }

}
package com.xieyi.etoffice.ui.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.BaseFragment
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.MessageResult
import com.xieyi.etoffice.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import java.util.*


class HomeFragment : BaseFragment() {
    private val TAG = "HomeFragment"

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var mGetMessageAdapter: GetMessageAdapter
    private lateinit var mGetStatusListHomeAdapter: GetStatusListHomeAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)


        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val textCompanyTitle: TextView = binding.textCompanyTitle
        homeViewModel.companyTitle.observe(viewLifecycleOwner) {
            textCompanyTitle.text = it
        }

        val textDate: TextView = binding.textTime
        homeViewModel.date.observe(viewLifecycleOwner) {
            textDate.text = it
        }

        homeViewModel.liveDataLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.scrollViewContent.visibility = View.GONE
                binding.llProgressbar.visibility = View.VISIBLE
            } else {
                binding.scrollViewContent.visibility = View.VISIBLE
                binding.llProgressbar.visibility = View.GONE
            }
        }
        homeViewModel.mLoading.value = true

        binding.inWork.setOnClickListener {
            buttonColorReset()
            binding.tvOnDuty.setTextColor(Color.BLACK)
            showStatusDialog("1", getString(R.string.fragment_home_on_duty))
        }

        binding.outWork.setOnClickListener {
            buttonColorReset()
            binding.tvOutsideDuty.setTextColor(Color.BLACK)
            showStatusDialog("2", getString(R.string.fragment_home_outside_duty))
        }

        binding.sleep.setOnClickListener {
            buttonColorReset()
            binding.tvRest.setTextColor(Color.BLACK)
            showStatusDialog("3", getString(R.string.fragment_home_rest))
        }

        binding.moving.setOnClickListener {
            buttonColorReset()
            binding.tvMoving.setTextColor(Color.BLACK)
            showStatusDialog("4", getString(R.string.fragment_home_moving))
        }

        binding.meeting.setOnClickListener {
            buttonColorReset()
            binding.tvMeeting.setTextColor(Color.BLACK)
            showStatusDialog("5", getString(R.string.fragment_home_meeting))
        }


        mGetMessageAdapter = GetMessageAdapter(ArrayList())
        binding.recyclerMessage.adapter = mGetMessageAdapter
        binding.recyclerMessage.isNestedScrollingEnabled = false


        mGetStatusListHomeAdapter = GetStatusListHomeAdapter(ArrayList())
        binding.recyclerView.adapter = mGetStatusListHomeAdapter

        //出勤記録を表示します
        binding.stateLayout.setOnClickListener {
            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@HomeFragment.parentFragmentManager
            fragmentManager.let { it1 ->
                mHomeReportDialog.show(
                    it1,
                    "HomeReportDialogTag"
                )
            }
        }

        mGetStatusListHomeAdapter.setOnAdapterListener(object :
            GetStatusListHomeAdapter.OnAdapterListener {
            override fun onClick() {
                val mHomeReportDialog = HomeReportDialog()
                val fragmentManager = this@HomeFragment.parentFragmentManager
                fragmentManager.let { it1 ->
                    mHomeReportDialog.show(
                        it1,
                        "HomeReportDialogTag"
                    )
                }
            }
        })

        //ページを更新
        dataPost()

        // ユーザ名表示
        // 读取用户信息，如果已经登录了，直接跳转到Main画面
        val prefs = activity?.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        val userName = prefs?.getString("username", "")
        if (userName != null){
            binding.userName.text = userName
        }

        return binding.root
    }


    //ページを更新
    private fun dataPost() {
        EtOfficeGetMessagePost()
        EtOfficeGetTenantPost()
    }

    private fun EtOfficeGetMessagePost() {
        lifecycleScope.launch {
            Api.EtOfficeGetMessage(
                context = requireActivity(),
                count = 4,
                lasttime = "",
                lastsubid = "",
                onSuccess = { model ->
                    lifecycleScope.launch {
                        when (model.status) {
                            0 -> {
                                EtOfficeGetMessageResult(model.result)

                                homeViewModel.mLoading.value = false
                            }
                            else -> {
                                homeViewModel.mLoading.value = false
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
                    lifecycleScope.launch {
                        homeViewModel.mLoading.value = false
                        Log.e(TAG, "onFailure:$data")
                    }
                }
            )

        }
    }

    private fun EtOfficeGetTenantPost() {
        lifecycleScope.launch {
            Api.EtOfficeGetTenant(
                context = requireContext(),
                onSuccess = { model ->
                    lifecycleScope.launch {
                        when (model.status) {
                            0 -> {
                                for(record in model.result.tenantlist ) {
                                    if(record.startflg.equals("1")){
                                        binding.textCompanyTitle.text = record.tenantname
                                        break
                                    }
                                }
                            }
                            else -> {
                                Tools.showErrorDialog(
                                    requireContext(),
                                    model.message
                                )
                            }
                        }
                    }

                },
                onFailure = { error, data ->
                    lifecycleScope.launch {
                        Log.e(TAG, "onFailure:$data")
                    }

                }
            )
        }


    }

    // Message UI更新
    private fun EtOfficeGetMessageResult(result: MessageResult) {
        mGetMessageAdapter.notifyDataChange(result.messagelist)
        mGetStatusListHomeAdapter.notifyDataChange(result.recordlist)

        if (result.recordlist.isNotEmpty()) {
            binding.state.text = getDispSatus(result.recordlist[0].statusvalue)
        }

        //データ存在の確認表示
        binding.recyclerView.setEmptyView(binding.listEmpty)

        //データ存在の確認表示
        binding.recyclerMessage.setEmptyView(binding.listMessageEmpty)
    }

    private fun showStatusDialog(statusvalue: String, statustext: String) {
        val mHomeStatusDialog = HomeStatusDialog(statusvalue, statustext)

        val fragmentManager = this@HomeFragment.parentFragmentManager
        fragmentManager.let { it1 -> mHomeStatusDialog.show(it1, "mHomeStatusDialog") }

        mHomeStatusDialog.setOnDialogListener(object : HomeStatusDialog.OnDialogListener {
            override fun onClick(userLocation: String, memo: String) {
                dataPost()

                //更新成功后，给用户提示一个信息。页面返回到主页面。
                Toast.makeText(activity, R.string.UPDATE_SUCCESS, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * すべてのボタンの色が標準の色に変わります。
     */
    private fun buttonColorReset() {
        binding.tvOnDuty.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.iphone_button_color
            )
        )
        binding.tvOutsideDuty.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.iphone_button_color
            )
        )
        binding.tvRest.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.iphone_button_color
            )
        )
        binding.tvMoving.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.iphone_button_color
            )
        )
        binding.tvMeeting.setTextColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.iphone_button_color
            )
        )
    }
    // MARK: - 勤務状態から表示するステータス文字列を取得
    fun getDispSatus(statusValue: String) : String {

        when (statusValue) {
            // 勤務開始
            "1" -> {
                return "勤務中"
            }
            // 勤務終了
            "2" -> {
                return "勤務外"
            }
            // 休憩開始
            "3" -> {
                return "休憩中"
            }
            // 休憩終了
            "4" -> {
                return "勤務中"
            }
            // 会議開始
            "5" -> {
                return "会議中"
            }
            // 会議終了
            "6" -> {
                return "勤務中"
                // 移動開始
            }
            "7" -> {
                return "移動中"
            }
            // 移動終了
            "8" -> {
                "勤務中"

            }
            else -> {
                return ""
            }
        }

        return ""
    }
}
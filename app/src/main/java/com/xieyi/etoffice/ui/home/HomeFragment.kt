package com.xieyi.etoffice.ui.home

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.MessageResult
import com.xieyi.etoffice.common.model.UserStatusResult
import com.xieyi.etoffice.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import java.util.*


class HomeFragment : Fragment() {
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
        homeViewModel.companyTitle.observe(viewLifecycleOwner, Observer {
            textCompanyTitle.text = it
        })

        val textDate: TextView = binding.textTime
        homeViewModel.date.observe(viewLifecycleOwner, Observer {
            textDate.text = it
        })

        homeViewModel.liveDataLoading.observe(viewLifecycleOwner, Observer {
            if (it){
                binding.progressbar.visibility = android.widget.ProgressBar.VISIBLE
            }else{
                binding.progressbar.visibility = android.widget.ProgressBar.GONE
            }
        })

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
                    getString(R.string.HomeReportDialogTag)
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
                        getString(R.string.HomeReportDialogTag)
                    )
                }
            }
        })


        //ページを更新
        dataPost()

        return binding.root
    }


    //ページを更新
    private fun dataPost() {
        homeViewModel.mLoading.value = true
        EtOfficeGetUserStatusPost()
        EtOfficeGetMessagePost()
    }

    private fun EtOfficeGetUserStatusPost() {
        Api.EtOfficeGetUserStatus(
            context = requireActivity(),
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetUserStatusResult(model.result)
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


    private fun EtOfficeGetUserStatusResult(result: UserStatusResult) {
        if (result.userstatuslist.size > 0) {
            binding.state.text = result.userstatuslist[0].statustext

        }

    }

    private fun EtOfficeGetMessagePost() {
        Api.EtOfficeGetMessage(
            context = requireActivity(),
            count = 5,
            lasttime = "",
            lastsubid = "",
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetMessageResult(model.result)
                            homeViewModel.mLoading.value = false
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

    // Message UI更新
    private fun EtOfficeGetMessageResult(result: MessageResult) {
        mGetMessageAdapter.notifyDataChange(result.messagelist)
        mGetStatusListHomeAdapter.notifyDataChange(result.recordlist)

    }

    private fun showStatusDialog(statusvalue: String, statustext: String) {
        val mHomeStatusDialog = HomeStatusDialog(statusvalue, statustext)

        val fragmentManager = this@HomeFragment.parentFragmentManager
        fragmentManager.let { it1 -> mHomeStatusDialog.show(it1, "mHomeStatusDialog") }

        mHomeStatusDialog.setOnDialogListener(object : HomeStatusDialog.OnDialogListener {
            override fun onClick(userLocation: String, memo: String) {
                dataPost()
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
        binding.tvRest.setTextColor(ContextCompat.getColor(requireActivity(), R.color.iphone_button_color))
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

}
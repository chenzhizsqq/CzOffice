package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.MessageResult
import com.xieyi.etoffice.common.model.StatusResult
import com.xieyi.etoffice.common.model.UserStatusResult
import com.xieyi.etoffice.databinding.FragmentHomeBinding
import kotlinx.coroutines.*


class HomeFragment : Fragment()
{
    private lateinit var homeViewModel: HomeViewModel

    private val TAG = javaClass.simpleName

    private lateinit var mAdapter: GetMessageAdapter
    private lateinit var mGetStatusListAdapter: GetStatusListAdapter


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

        binding.inWork.setOnClickListener {
            showStatusDialog("1","勤務中")
        }

        binding.outWork.setOnClickListener {
            showStatusDialog("2","勤務外")
        }

        binding.sleep.setOnClickListener {
            showStatusDialog("3","休憩中")
        }

        binding.moving.setOnClickListener {
            showStatusDialog("4","移動中")
        }

        binding.meeting.setOnClickListener {
            showStatusDialog("5","会議中")
        }


        //出勤記録を表示します
        binding.stateLayout.setOnClickListener {
            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@HomeFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }


        }


        //ページを更新
        dataPost()

        return binding.root
    }


    //ページを更新
    private fun dataPost() {
        EtOfficeGetUserStatusPost()
        EtOfficeGetStatusListPost()
        EtOfficeGetMessagePost()
    }

    private  fun EtOfficeGetUserStatusPost(){
        Api.EtOfficeGetUserStatus(
            context = requireActivity(),
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetUserStatusResult(model.result)
                        }
                        1 -> {
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
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
                    Log.e(TAG, "onFailure:$data");
                }
            }
        )
    }


    private fun EtOfficeGetUserStatusResult(result: UserStatusResult) {
        if(result.userstatuslist.size>0)
        {
            binding.state.text = result.userstatuslist[0].statustext
        }

    }

    private fun EtOfficeGetStatusListPost(){
        Api.EtOfficeGetStatusList(
            context = requireActivity(),
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetStatusListResult(model.result)
                        }
                        1 -> {
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
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
                    Log.e(TAG, "onFailure:$data");
                }
            }
        )
    }


    // GetStatusList UI更新
    private fun EtOfficeGetStatusListResult(result: StatusResult) {
        mGetStatusListAdapter= GetStatusListAdapter(result.recordlist)
        binding.recyclerView.adapter = mGetStatusListAdapter
    }

    private fun EtOfficeGetMessagePost(){
        Api.EtOfficeGetMessage(
            context = requireActivity(),
            count = 50,
            lasttime = "",
            lastsubid = "",
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetMessageResult(model.result)
                        }
                        1 -> {
                            Snackbar.make(
                                binding.root,
                                model.message,
                                Snackbar.LENGTH_LONG
                            ).show()
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
                    Log.e(TAG, "onFailure:$data");
                }
            }
        )
    }

    // Message UI更新
    private fun EtOfficeGetMessageResult(result: MessageResult) {
            Log.e(TAG, "doOnUiCode_Message: begin", )

            mAdapter=GetMessageAdapter(result.messagelist)
            binding.recyclerMessage.adapter = mAdapter

    }

    private fun showStatusDialog(statusvalue:String,statustext:String) {
        val mHomeStatusDialog = HomeStatusDialog(statusvalue,statustext)

        val fragmentManager = this@HomeFragment.parentFragmentManager
        fragmentManager.let { it1 -> mHomeStatusDialog.show(it1, "mHomeStatusDialog") }

        mHomeStatusDialog.setOnDialogListener(object : HomeStatusDialog.OnDialogListener{
            override fun onClick(userLocation: String, memo: String) {
                Log.e(TAG, "onDialogClick: userLocation:$userLocation memo:$memo", )

                dataPost()
            }
        })
    }

}
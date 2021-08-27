package com.xieyi.etoffice.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.databinding.FragmentReportBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetReportList

import com.xieyi.etoffice.ui.home.HomeReportDialog
import kotlinx.coroutines.*
import java.util.*


class ReportFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = javaClass.simpleName


    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: GetReportListGroupAdapter


    private lateinit var binding: FragmentReportBinding


    private val arrayListYmd = ArrayList<String>()


    private lateinit var viewModel: ReportViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(this).get(ReportViewModel::class.java)

        mSwipeRefreshLayout= binding.swipeRefreshLayout

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this)


        mRecyclerView = binding.recyclerViewGetReport

        topMenu()

        EtOfficeGetReportListPost("","")

        return binding.root
    }


    private fun EtOfficeGetReportListPost(startym:String,months:String) {
        Api.EtOfficeGetReportListPost(
            context = requireActivity(),
            startym = startym,
            months = months,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetReportListResult(model.result)
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

    private fun EtOfficeSetApprovalJskPost(arrayListYmd : ArrayList<String>) {
        //指定された　発信
        if (arrayListYmd.size > 0) {

            Api.EtOfficeSetApprovalJskPost(
                context = requireActivity(),
                ymdArray = arrayListYmd,
                onSuccess = { model ->
                    Handler(Looper.getMainLooper()).post {

                        when (model.status) {
                            0 -> {
                                EtOfficeGetReportListPost("","")
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
    }

    private fun EtOfficeGetReportListResult(result: EtOfficeGetReportList.Result) {
        viewModel.allSelectChangeFalse()
        mAdapter= activity?.let {
            GetReportListGroupAdapter(
                result.group
                ,arrayListYmd
                ,it
                ,viewModel
                ,viewLifecycleOwner
            )
        }!!
        mRecyclerView.adapter = mAdapter


        //allSelect click
        binding.allSelect.setOnClickListener {
            arrayListYmd.clear()
            viewModel.allSelectChange()
            if (viewModel.isAllSelect() == true){
                for (i in result.group.indices){
                    for (j in result.group[i].reportlist.indices)
                        if(!arrayListYmd.contains(result.group[i].reportlist[j].yyyymmdd)) {
                            arrayListYmd.add(result.group[i].reportlist[j].yyyymmdd)
                        }
                }
            }
        }
    }


    private fun topMenu() {

        //出勤記録を表示します
        binding.people.setOnClickListener {
            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@ReportFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }
        }


        viewModel.visibility.observe(viewLifecycleOwner,{
            binding.allSelect.visibility = it
            binding.commit.visibility = it
            binding.people.visibility = it
        })

        binding.edit.setOnClickListener(View.OnClickListener {
            viewModel.visibilityChange()
        })



        //commit click
        binding.commit.setOnClickListener {
            commitAlertDialog()

        }
    }




    private fun commitAlertDialog(){

        if(arrayListYmd.size>0) {
            AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                .setTitle("消息")
                .setMessage("現在選択されている情報を承認しますか？")
                .setPositiveButton("确定") { _, which ->
                    if (viewModel.visibility.value == View.VISIBLE) {
                        EtOfficeSetApprovalJskPost(arrayListYmd)
                    }
                }
                .setNegativeButton("取消") { _, which ->
                }
                .show()
        }else{
            AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                .setTitle("消息")
                .setMessage("まだ選択していません。選択してください。")
                .setPositiveButton("确定") { _, which ->
                }
                .show()
        }
    }

    companion object {
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }

    override fun onRefresh() {
        Log.e(TAG, "onRefresh: begin", )
        mSwipeRefreshLayout.isRefreshing = false;
        EtOfficeGetReportListPost("","")
    }
}
package com.xieyi.etoffice.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.ReportListResult
import com.xieyi.etoffice.databinding.FragmentReportBinding

import com.xieyi.etoffice.ui.home.HomeReportDialog
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class ReportFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = "ReportFragment"

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

        mSwipeRefreshLayout = binding.swipeRefreshLayout

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this)


        mRecyclerView = binding.recyclerViewGetReport

        mAdapter = GetReportListGroupAdapter()
        mRecyclerView.adapter = mAdapter


        viewModel.liveDataLoading.observe(viewLifecycleOwner, {
            if (it){
                binding.swipeRefreshLayout.visibility = View.GONE
                binding.llProgressbar.visibility = View.VISIBLE
            }else{
                binding.swipeRefreshLayout.visibility = View.VISIBLE
                binding.llProgressbar.visibility = View.GONE
            }
        })
        viewModel.mLoading.value = true

        topMenu()

        EtOfficeGetReportListPost("", "")

        return binding.root
    }


    private fun EtOfficeGetReportListPost(startym: String, months: String) {
        Api.EtOfficeGetReportList(
            context = requireActivity(),
            startym = startym,
            months = months,
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetReportListResult(model.result)
                            if(viewModel.mLoading.value == true){
                                try {
                                    Thread.sleep(1000)
                                } catch (e: InterruptedException) {
                                    e.printStackTrace()
                                }
                                viewModel.mLoading.value = false
                            }
                        }
                        else -> {
                            activity?.let {
                                Tools.showErrorDialog(
                                    it,
                                    model.message)
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

    private fun EtOfficeSetApprovalJskPost(arrayListYmd: ArrayList<String>) {
        //指定された　発信
        if (arrayListYmd.size > 0) {

            Api.EtOfficeSetApprovalJsk(
                context = requireActivity(),
                ymdArray = arrayListYmd,
                onSuccess = { model ->
                    Handler(Looper.getMainLooper()).post {

                        when (model.status) {
                            0 -> {
                                EtOfficeGetReportListPost("", "")
                            }
                            else -> {
                                activity?.let {
                                    Tools.showErrorDialog(
                                        it,
                                        model.message)
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
    }

    private fun EtOfficeGetReportListResult(result: ReportListResult) {
        viewModel.allSelectChangeFalse()
        activity?.let {
            mAdapter.notifyDataSetChanged(
                result.group, arrayListYmd, it, viewModel, viewLifecycleOwner
            )
        }


        //allSelect click
        binding.allSelect.setOnClickListener {
            arrayListYmd.clear()
            viewModel.allSelectChange()
            if (viewModel.isAllSelect() == true) {
                for (i in result.group.indices) {
                    for (j in result.group[i].reportlist.indices)
                        if (!arrayListYmd.contains(result.group[i].reportlist[j].yyyymmdd)) {
                            arrayListYmd.add(result.group[i].reportlist[j].yyyymmdd)
                        }
                }
            }
        }
    }


    private fun topMenu() {

        //出勤記録を表示します
//        binding.people.setOnClickListener {
//            val mHomeReportDialog = HomeReportDialog()
//
//            val fragmentManager = this@ReportFragment.parentFragmentManager
//            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog") }
//        }

        //メンバーページに切り替えます
        binding.people.setOnClickListener { view ->
            Navigation.findNavController(view).navigate(R.id.member_fragment)
        }


        viewModel.visibility.observe(viewLifecycleOwner, {
            binding.allSelect.visibility = it
            binding.commit.visibility = it
            if (it == View.GONE) {
                binding.edit.visibility = View.VISIBLE
                binding.cancel.visibility = View.GONE
            } else if (it == View.VISIBLE) {
                binding.cancel.visibility = View.VISIBLE
                binding.edit.visibility = View.GONE
                binding.cancel.text = EtOfficeApp.context.getString(R.string.CANCEL)
            }
        })

        binding.edit.setOnClickListener(View.OnClickListener {
            viewModel.visibilityChange()
        })

        binding.cancel.setOnClickListener(View.OnClickListener {
            viewModel.visibilityChange()
        })

        //commit click
        binding.commit.setOnClickListener {
            commitAlertDialog()

        }
    }


    private fun commitAlertDialog() {

        if (arrayListYmd.size > 0) {
            AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                .setTitle(R.string.CONFIRM)
                .setMessage(R.string.MSG18)
                .setPositiveButton(R.string.OK) { _, which ->
                    if (viewModel.visibility.value == View.VISIBLE) {
                        EtOfficeSetApprovalJskPost(arrayListYmd)
                    }
                }
                .setNegativeButton(R.string.CANCEL) { _, which ->
                }
                .show()
        } else {
            activity?.let {
                activity?.let {
                    Tools.showErrorDialog(it, getString(R.string.MSG19))
                }
            }
        }
    }

    companion object {
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }

    override fun onRefresh() {
        Log.e(TAG, "onRefresh: begin")
        mSwipeRefreshLayout.isRefreshing = false
        EtOfficeGetReportListPost("", "")
    }
}
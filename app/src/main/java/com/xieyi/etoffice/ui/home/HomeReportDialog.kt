package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.FullScreenDialogBaseFragment
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.StatusInfo
import com.xieyi.etoffice.databinding.DialogHomeReportBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeReportDialog : FullScreenDialogBaseFragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG: String = "HomeReportDialog"

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mAdapter: GetStatusListAdapter
    private var recordlist: List<StatusInfo> = ArrayList()
    private lateinit var binding: DialogHomeReportBinding
    private var loading: Boolean = false

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogHomeReportBinding.inflate(inflater, container, false)

        //ボタン　保存後に閉じる
        val btnSaveAndClose = binding.btnSaveAndClose
        btnSaveAndClose.setOnClickListener {
            dialog!!.dismiss()
        }

        // リストビュー初期化
        initRecycleView()

        EtOfficeGetStatusListPost()

        return binding.root
    }

    /**
     * リストビュー初期化
     */
    private fun initRecycleView() {

        mSwipeRefreshLayout = binding.swipeRefreshLayout

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this)

        mAdapter = GetStatusListAdapter(recordlist)
        binding.recyclerView.adapter = mAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition: Int = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 == binding.recyclerView.adapter?.itemCount && !loading) {
                    loading = true
                    Log.e(TAG, "EtOfficeGetStatusListPost calling 000...dx:" + dx + "   dy:" + dy)
                    EtOfficeGetStatusListPost()
                }
            }
        })
    }

    private fun EtOfficeGetStatusListPost() {
        CoroutineScope(Dispatchers.IO).launch {
            loading = true
            Log.e(TAG, "EtOfficeGetStatusListPost calling...")
            Api.EtOfficeGetStatusList(
                context = requireActivity(),
                onSuccess = { model ->
                    CoroutineScope(Dispatchers.Main).launch {
                        when (model.status) {
                            0 -> {
                                mAdapter.updateData(model.result.recordlist)
                                mAdapter.notifyDataSetChanged()
                            }
                            else -> {
                                activity?.let {
                                    Tools.showErrorDialog(
                                        it,
                                        model.message,
                                    )
                                }
                            }
                        }
                        loading = false
                        binding.swipeRefreshLayout.isRefreshing = false

                    }
                },
                onFailure = { error, data ->
                    CoroutineScope(Dispatchers.Main).launch {
                        loading = false
                        binding.swipeRefreshLayout.isRefreshing = false
                        Log.e(TAG, "onFailure:$data")

                    }
                }
            )
        }

    }

    override fun onRefresh() {
        Log.e(TAG, "EtOfficeGetStatusListPost calling...onRefresh")
        EtOfficeGetStatusListPost()
    }
}

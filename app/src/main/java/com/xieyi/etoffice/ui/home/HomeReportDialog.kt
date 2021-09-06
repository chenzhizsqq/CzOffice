package com.xieyi.etoffice.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.StatusResult
import com.xieyi.etoffice.databinding.DialogHomeReportBinding


class HomeReportDialog : DialogFragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG: String = "HomeReportDialog"

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var mAdapter: GetStatusListAdapter
    private lateinit var binding: DialogHomeReportBinding

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogHomeReportBinding.inflate(inflater, container, false)


        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        //attributes.gravity = Gravity.BOTTOM //下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes


        //ボタン　保存後に閉じる
        val btnSaveAndClose = binding.btnSaveAndClose
        btnSaveAndClose.setOnClickListener {
            dialog!!.dismiss()
        }

        mSwipeRefreshLayout = binding.swipeRefreshLayout

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this)


        EtOfficeGetStatusListPost()

        return binding.root
    }


    private fun EtOfficeGetStatusListResult(result: StatusResult) {
        mAdapter = GetStatusListAdapter(result.recordlist)
        binding.recyclerView.adapter = mAdapter

    }

    private fun EtOfficeGetStatusListPost() {
        Api.EtOfficeGetStatusList(
            context = requireActivity(),
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetStatusListResult(model.result)
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

    override fun onRefresh() {
        binding.swipeRefreshLayout.isRefreshing = false
        EtOfficeGetStatusListPost()
    }
}

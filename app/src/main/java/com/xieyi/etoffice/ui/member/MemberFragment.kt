package com.xieyi.etoffice.ui.member

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.StuffListResult
import com.xieyi.etoffice.databinding.FragmentMemberBinding


class MemberFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {

    private val TAG = "MemberFragment"

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: GetStuffSectionListAdapter

    private lateinit var binding: FragmentMemberBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemberBinding.inflate(inflater, container, false)

        EtOfficeGetStuffListPost()

        mSwipeRefreshLayout = binding.swipeRefreshLayout

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this)

        mRecyclerView = binding.recyclerViewStuffList
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!recyclerView.canScrollVertically(1)) {
                    }
                }

            }
        })

        return binding.root
    }

    private fun EtOfficeGetStuffListPost() {
        Api.EtOfficeGetStuffList(
            context = requireActivity(),
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            EtOfficeGetStuffListResult(model.result)
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


    private fun EtOfficeGetStuffListResult(result: StuffListResult) {
        val recyclerView: RecyclerView = binding.recyclerViewStuffList

        mAdapter = GetStuffSectionListAdapter(result.sectionlist, requireActivity())
        recyclerView.adapter = mAdapter

    }

    override fun onRefresh() {
        mSwipeRefreshLayout.isRefreshing = false
        EtOfficeGetStuffListPost()
    }
}
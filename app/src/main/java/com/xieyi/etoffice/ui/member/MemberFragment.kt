package com.xieyi.etoffice.ui.member

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.SectionInfo
import com.xieyi.etoffice.databinding.FragmentMemberBinding


class MemberFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private val TAG = "MemberFragment"
    private lateinit var mAdapter: GetStuffSectionListAdapter
    private lateinit var binding: FragmentMemberBinding
    private var loading: Boolean = false
    private lateinit var viewModel: MemberViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemberBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(this).get(MemberViewModel::class.java)
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

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        initRecyclerView()

        EtOfficeGetStuffListPost()

        return binding.root
    }

    private fun initRecyclerView() {
        mAdapter = GetStuffSectionListAdapter(ArrayList<SectionInfo>(), requireActivity())
        binding.recyclerViewStuffList.adapter = mAdapter

        binding.recyclerViewStuffList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager =
                    binding.recyclerViewStuffList.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition: Int = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 == binding.recyclerViewStuffList.adapter?.itemCount && !loading) {
                    loading = true
                    Log.d(TAG, "EtOfficeGetStuffListPost calling ...dx:" + dx + "   dy:" + dy)
                    EtOfficeGetStuffListPost()
                }
            }
        })
    }

    private fun EtOfficeGetStuffListPost() {
        loading = true
        Api.EtOfficeGetStuffList(
            context = requireActivity(),
            onSuccess = { model ->
                Handler(Looper.getMainLooper()).post {

                    when (model.status) {
                        0 -> {
                            mAdapter.notifyDataUpdateList(model.result.sectionlist)
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

                    loading = false
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data")
                    loading = false
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        )
    }

    override fun onRefresh() {
        Log.d(TAG, "onRefresh calling ...")
        EtOfficeGetStuffListPost()
    }
}
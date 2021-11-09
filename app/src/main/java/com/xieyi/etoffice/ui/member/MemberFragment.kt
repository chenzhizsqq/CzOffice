package com.xieyi.etoffice.ui.member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.BaseFragment
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.StuffListModel
import com.xieyi.etoffice.common.model.StuffStatusDispInfo
import com.xieyi.etoffice.common.model.UserStatusModel
import com.xieyi.etoffice.databinding.FragmentMemberBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MemberFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private val TAG = "MemberFragment"
    private lateinit var mAdapter: GetStuffSectionListAdapter
    private lateinit var binding: FragmentMemberBinding
    private var loading: Boolean = false
    private lateinit var viewModel: MemberViewModel
    private lateinit var userStatusModel: UserStatusModel
    private var dispInfoList: ArrayList<StuffStatusDispInfo> = ArrayList<StuffStatusDispInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemberBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(this).get(MemberViewModel::class.java)
        viewModel.liveDataLoading.observe(viewLifecycleOwner, {
            if (it) {
                binding.swipeRefreshLayout.visibility = View.GONE
                binding.llProgressbar.visibility = View.VISIBLE
            } else {
                binding.swipeRefreshLayout.visibility = View.VISIBLE
                binding.llProgressbar.visibility = View.GONE
            }
        })
        viewModel.mLoading.value = true

        // Listenerをセット
        binding.swipeRefreshLayout.setOnRefreshListener(this)

        //データ存在の確認表示
        binding.recyclerViewStuffList.setEmptyView(binding.listEmpty)

        initRecyclerView()


        // ユーザー最新勤務状態の一覧取得
        EtOfficeGetUserStatusPost()

        return binding.root
    }

    private fun initRecyclerView() {
        mAdapter = GetStuffSectionListAdapter(dispInfoList, requireActivity())
        binding.recyclerViewStuffList.adapter = mAdapter

        binding.recyclerViewStuffList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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

        mAdapter.setOnAdapterListener(object : GetStuffSectionListAdapter.OnAdapterListener {
            override fun onClick(phoneNumber: String) {
                //确定是否有电话号码
                if (phoneNumber == "") {
                    Tools.showErrorDialog(
                        requireActivity(),
                        getString(R.string.no_telephone_number)
                    )
                } else {
                    val activity = context as FragmentActivity
                    val fm: FragmentManager = activity.supportFragmentManager
                    val mMemberTelDialog = MemberTelDialog(phoneNumber)
                    fm.let { it1 -> mMemberTelDialog.show(it1, "mMemberTelDialog") }
                }
            }
        })
    }

    /**
     * ユーザー最新勤務状態の一覧取得
     */
    private fun EtOfficeGetUserStatusPost() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                loading = true
                Api.EtOfficeGetUserStatus(
                    context = requireActivity(),
                    onSuccess = { model ->
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {

                                when (model.status) {
                                    0 -> {
                                        userStatusModel = model
                                        // 社員一覧取得
                                        EtOfficeGetStuffListPost()
                                        viewModel.mLoading.value = false
                                    }
                                    else -> {
                                        activity?.let {
                                            Tools.showErrorDialog(
                                                it,
                                                model.message
                                            )
                                            viewModel.mLoading.value = false
                                        }
                                    }
                                }

                                loading = false
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    },
                    onFailure = { error, data ->
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                Log.e(TAG, "onFailure:$data")
                                loading = false
                                binding.swipeRefreshLayout.isRefreshing = false
                                viewModel.mLoading.value = false
                            }
                        }
                    }
                )
            }
        }
    }

    /**
     * 社員一覧取得
     */
    private fun EtOfficeGetStuffListPost() {
        if (!isAdded) return
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                loading = true
                Api.EtOfficeGetStuffList(
                    context = requireActivity(),
                    onSuccess = { model ->
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {

                                when (model.status) {
                                    0 -> {
                                        makeDispInfo(model, userStatusModel)
                                        mAdapter.notifyDataUpdateList(dispInfoList)

                                        viewModel.mLoading.value = false
                                    }
                                    else -> {
                                        viewModel.mLoading.value = false

                                        activity?.let {
                                            Tools.showErrorDialog(
                                                it,
                                                model.message
                                            )
                                        }
                                    }
                                }

                                loading = false
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    },
                    onFailure = { error, data ->
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                viewModel.mLoading.value = false
                                Log.e(TAG, "onFailure:$data")
                                loading = false
                                binding.swipeRefreshLayout.isRefreshing = false
                            }
                        }
                    }
                )
            }
        }
    }

    /**
     * 表示用社員出勤情報作成
     *
     * @param stuffListMode: 社員一覧情報
     * @param userStatusModel: 社員最新出勤情報
     */
    private fun makeDispInfo(stuffListMode: StuffListModel, userStatusModel: UserStatusModel) {
        dispInfoList.clear()
        for (section in stuffListMode.result.sectionlist) {
            // 部門名
            var info = StuffStatusDispInfo(
                section.sectioncd,
                section.sectionname,
                null,
                null
            )
            dispInfoList.add(info)

            // 社員リスト
            for (stuffInfo in section.stufflist) {
                for (userStatusInfo in userStatusModel.result.userstatuslist) {
                    if (stuffInfo.userid == userStatusInfo.userid) {
                        var info = StuffStatusDispInfo(
                            section.sectioncd,
                            section.sectionname,
                            stuffInfo,
                            userStatusInfo
                        )
                        dispInfoList.add(info)
                        break
                    }
                }
            }
        }
    }

    override fun onRefresh() {
        Log.d(TAG, "onRefresh calling ...")
        // ユーザー最新勤務状態の一覧取得
        EtOfficeGetUserStatusPost()
    }
}
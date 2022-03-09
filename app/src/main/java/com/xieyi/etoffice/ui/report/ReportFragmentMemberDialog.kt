package com.xieyi.etoffice.ui.report

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.MainActivityViewModel
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.StuffListModel
import com.xieyi.etoffice.common.model.StuffStatusDispInfo
import com.xieyi.etoffice.common.model.UserStatusModel
import com.xieyi.etoffice.databinding.DialogReportFragmentMemberBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ReportFragmentMemberDialog : DialogFragment(), SwipeRefreshLayout.OnRefreshListener {

    private val TAG = "ReportFragmentMemberDialog"
    private lateinit var mAdapter: ReportGetStuffSectionListAdapter
    private lateinit var binding: DialogReportFragmentMemberBinding
    private var loading: Boolean = false
    private lateinit var viewModel: ReportMemberViewModel
    private lateinit var userStatusModel: UserStatusModel
    private var dispInfoList: ArrayList<StuffStatusDispInfo> = ArrayList<StuffStatusDispInfo>()

    //与MainActivity共同的ViewModel
    private val sharedVM: MainActivityViewModel by activityViewModels()

    //与ReportFragment联系
    lateinit var listener: OnDialogListener

    interface OnDialogListener {
        fun onClick(userid: String)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogReportFragmentMemberBinding.inflate(inflater, container, false)


        //フルスクリーン　Full screen
        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val attributes = window.attributes
        attributes.gravity = Gravity.BOTTOM //下方
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        window.attributes = attributes

        //类似iphone的，从下到上的动画效果
        window.setWindowAnimations(R.style.BottomDialogAnimation)

        viewModel =
            ViewModelProvider(this).get(ReportMemberViewModel::class.java)
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

        initRecyclerView()


        // ユーザー最新勤務状態の一覧取得
        EtOfficeGetUserStatusPost()

        return binding.root
    }

    private fun initRecyclerView() {
        mAdapter = ReportGetStuffSectionListAdapter(dispInfoList, requireActivity())
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

        mAdapter.setOnAdapterListener(object : ReportGetStuffSectionListAdapter.OnAdapterListener {
            override fun onClick(userName: String, userid: String) {
                //确定是否有fragTitle
                if (userName == "") {
                    Tools.showErrorDialog(
                        requireActivity(),
                        "fragTitle empty"
                    )
                } else {
                    sharedVM.reportFragTitle.value = userName

                    Log.d(TAG, "userid: "+userid )
                    listener.onClick(userid)

                    //点击后退出Dialog
                    dismiss()
                }
            }
        })
    }

    /**
     * ユーザー最新勤務状態の一覧取得
     */
    private fun EtOfficeGetUserStatusPost() {
        CoroutineScope(Dispatchers.IO).launch {
            loading = true
            Api.EtOfficeGetUserStatus(
                context = requireActivity(),
                onSuccess = { model ->
                    CoroutineScope(Dispatchers.Main).launch {

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

                },
                onFailure = { error, data ->
                    CoroutineScope(Dispatchers.Main).launch {
                        Log.e(TAG, "onFailure:$data")
                        loading = false
                        binding.swipeRefreshLayout.isRefreshing = false
                        viewModel.mLoading.value = false
                    }
                }

            )
        }

    }

    /**
     * 社員一覧取得
     */
    private fun EtOfficeGetStuffListPost() {
        if (!isAdded) return
        CoroutineScope(Dispatchers.IO).launch {
            loading = true
            Api.EtOfficeGetStuffList(
                context = requireActivity(),
                onSuccess = { model ->
                    CoroutineScope(Dispatchers.Main).launch {

                        when (model.status) {
                            0 -> {
                                makeDispInfo(model, userStatusModel)
                                mAdapter.notifyDataUpdateList(dispInfoList)

                                //データ存在の確認表示
                                binding.recyclerViewStuffList.setEmptyView(binding.listEmpty)

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

                },
                onFailure = { error, data ->
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.mLoading.value = false
                        Log.e(TAG, "onFailure:$data")
                        loading = false
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                }

            )
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
            val info = StuffStatusDispInfo(
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
                        val mStuffStatusDispInfo = StuffStatusDispInfo(
                            section.sectioncd,
                            section.sectionname,
                            stuffInfo,
                            userStatusInfo
                        )
                        dispInfoList.add(mStuffStatusDispInfo)
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
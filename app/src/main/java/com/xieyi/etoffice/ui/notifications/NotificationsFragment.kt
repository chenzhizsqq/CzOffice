package com.xieyi.etoffice.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.xieyi.etoffice.*
import com.xieyi.etoffice.common.HttpUtil
import com.xieyi.etoffice.databinding.FragmentNotificationsBinding
import com.xieyi.etoffice.enum.ResultType
import org.json.JSONObject
import kotlin.concurrent.thread


class NotificationsFragment : Fragment(), OnRefreshListener, OnLoadMoreListener {
    private val TAG = "NotificationsFragment"
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var adapter:NotificationsAdapter
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            binding.textNotifications.text = it
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        binding.swipeToLoadLayout.setOnRefreshListener(this);
        binding.swipeToLoadLayout.setOnLoadMoreListener(this);

        // 第一次进入，模拟用户下拉刷新
        binding.swipeToLoadLayout.isRefreshing = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 最新メッセージ一覧取得
    private fun sendRequest() {
        thread {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetMessage")
            jsonObject.put("token", "202107151828510590000000090010001231850971920426")
            jsonObject.put("tenant", "3")
            jsonObject.put("hpid", "6")
            jsonObject.put("device","android")
            jsonObject.put("count","10")
            jsonObject.put("lasttime",notificationsViewModel.lasttime)
            jsonObject.put("lastsubid",notificationsViewModel.lastsubid)
            // 通信処理
            HttpUtil.callAsyncHttp(
                context = requireContext(),
                url = Config.LoginUrl,
                parameter = jsonObject,
                classType = NotificationsResponse::class.java as Class<Any>,
                authToken = true,
                fcmToken = true,
                onSuccess = ::onSuccess,
                onFailure = ::onFailure
            )
        }
    }

    // 成功結果処理
    private fun onSuccess(data: Any){
        if (data is NotificationsResponse) {
            notificationsViewModel.messageList.clear()
            notificationsViewModel.appendMessage(data.result.messagelist)

            var lastMessage = data.result.messagelist.last()
            notificationsViewModel.lastsubid = lastMessage.subid
            notificationsViewModel.lasttime = lastMessage.updatetime

            activity?.runOnUiThread{
                adapter.notifyDataChange(notificationsViewModel.messageList)
            }
        }
    }

    // 通信失敗時、結果処理
    private fun onFailure(error: ResultType, data: Any){
        Log.e(TAG, "onFailure:" + data.toString() );
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        // 设置LayoutManager
        binding.swipeTarget.layoutManager = layoutManager
        binding.swipeTarget.itemAnimator = DefaultItemAnimator()
        // 设置Adapter
        adapter = NotificationsAdapter(ArrayList())
        binding.swipeTarget.adapter = adapter

        binding.swipeTarget.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)) {
                        binding.swipeToLoadLayout.isLoadingMore = true
                    }
                }
            }
        })
    }

    override fun onRefresh() {
        Log.i("NotificationsFragment", "正在刷新:");
        binding.swipeToLoadLayout.postDelayed({
            binding.swipeToLoadLayout.isRefreshing = false

            notificationsViewModel.messageList.clear()
            sendRequest()

        }, 2000)
    }

    override fun onLoadMore() {
        binding.swipeToLoadLayout.postDelayed({
            binding.swipeToLoadLayout.isLoadingMore = false

            sendRequest()
            adapter.notifyDataChange(notificationsViewModel.messageList)
        }, 2000)
    }
}
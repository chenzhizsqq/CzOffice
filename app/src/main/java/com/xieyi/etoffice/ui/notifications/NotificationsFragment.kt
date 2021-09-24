package com.xieyi.etoffice.ui.notifications

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.*
import com.xieyi.etoffice.base.BaseFragment
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.model.MessageInfo
import com.xieyi.etoffice.common.model.SetMessageModel
import com.xieyi.etoffice.databinding.FragmentNotificationsBinding
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.forEach
import kotlin.collections.isNotEmpty
import kotlin.collections.iterator
import kotlin.collections.last


class NotificationsFragment : BaseFragment(), View.OnClickListener,
    CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    private val TAG = "NotificationsFragment"
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var adapter: NotificationsAdapter
    private var loading: Boolean = false

    /**
     * 创建视图，初始化Fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // ViewBinding
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)
        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.titleNotifications.text = it
        })

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

        // 监听按钮事件
        binding.edit.setOnClickListener(this)
        binding.selectAll.setOnCheckedChangeListener(this)
        binding.delete.setOnClickListener(this)
        binding.cancel.setOnClickListener(this)

        binding.swipeRefreshLayout.setOnRefreshListener(this)

        initRecyclerView()

        getMessageRequest(false)

        return binding.root
    }

    /**
     * 监听按钮事件
     */
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.edit -> {
                binding.edit.visibility = View.GONE
                binding.delete.visibility = View.VISIBLE
                binding.selectAll.visibility = View.VISIBLE
                binding.cancel.visibility = View.VISIBLE
                adapter.isEdit = true
                adapter.initCheck(false)
                adapter.notifyDataChange(viewModel.messageList)
            }
            R.id.cancel -> {
                binding.edit.visibility = View.VISIBLE
                binding.delete.visibility = View.GONE
                binding.selectAll.visibility = View.GONE
                binding.cancel.visibility = View.GONE
                adapter.isEdit = false
                adapter.initCheck(false)
                adapter.notifyDataChange(viewModel.messageList)
            }
            R.id.delete -> {
                deleteData(view)
            }
        }
    }

    /**
     * 一览行操作，删除数据
     */
    private fun deleteData(view: View) {
        var updateArray = JSONArray()
        val checkStatus = adapter.getCheckStatus()
        for ((index, value) in checkStatus) {
            if (value.isNotEmpty()) {
                updateArray.put(value)
            }
        }
        if (updateArray.length() > 0) {
            activity?.let {
                AlertDialog.Builder(it).apply {
                    setTitle(R.string.remind_message)
                    setMessage(R.string.remind_content)
                    setCancelable(false)
                    setPositiveButton(R.string.confirm) { _, _ ->
                        deleteMessagesRequest(
                            "2",
                            updateArray
                        )
                    }
                    setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                }.show()
            }
        } else {
            activity?.let {
                Tools.showErrorDialog(it, getString(R.string.delete_message_require))
            }
            return
        }
    }

    /**
     * 取得最新消息一览
     */
    private fun getMessageRequest(isLoadMore: Boolean = false) {
        loading = true

        var loadMore = isLoadMore
        if (viewModel.messageList.size == 0) {
            loadMore = false
        }
        var lasttime: String? = null
        var lastsubid: String? = null
        if (loadMore) {
            lasttime = viewModel.lasttime
            lastsubid = viewModel.lastsubid
        }

        Api.EtOfficeGetMessage(
            context = requireContext(),
            lasttime = lasttime,
            lastsubid = lastsubid,
            count = viewModel.searchCount,
            onSuccess = { data ->
                Handler(Looper.getMainLooper()).post {
                    if (data.status == 0 && data.result.messagelist.isNotEmpty()) {
                        viewModel.appendMessage(data.result.messagelist)

                        var lastMessage = data.result.messagelist.last()
                        viewModel.lastsubid = lastMessage.subid
                        viewModel.lasttime = lastMessage.updatetime

                        adapter.notifyDataChange(viewModel.messageList)
                    }

                    binding.swipeRefreshLayout.isRefreshing = false
                    loading = false

                    if(viewModel.mLoading.value == true){
                        try {
                            Thread.sleep(1000)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                        viewModel.mLoading.value = false
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    binding.swipeRefreshLayout.isRefreshing = false
                    loading = false
                    Log.e(TAG, "onFailure:$data");
                }
            }
        )
    }

    /**
     * 初始化一览行操作
     */
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        // 设置LayoutManager
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.itemAnimator = DefaultItemAnimator()
        // 设置Adapter
        adapter = NotificationsAdapter(ArrayList())

        // スクロールリスナー
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = binding.recycleView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition: Int = layoutManager.findLastVisibleItemPosition()

                if (lastVisibleItemPosition + 1 == binding.recycleView.adapter?.itemCount && !loading) {
                    Log.d(TAG, "loading more...");
                    loading = true
                    getMessageRequest(true)
                }
            }
        })

        // 点击List行数据，弹出操作对话框
        adapter.setOnItemClickListener(object : NotificationsAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                if (adapter.isEdit) {
                    return
                }
                val alertDialog = NotificationsAlertDialog()
                val bundle = Bundle()
                val message = viewModel.messageList[position]
                bundle.putSerializable("messageItem", message)
                alertDialog.arguments = bundle
                val fragmentManager = this@NotificationsFragment.parentFragmentManager
                fragmentManager.let { alertDialog.show(it, "notificationsAlertDialog") }
                viewModel.checkedPosition = position
                alertDialog.setOnDeleteClick(object : NotificationsAlertDialog.OnDeleteListener {
                    override fun onDeleteClick(message: MessageInfo) {
                        var itemArray = JSONArray().put(message.updatetime + message.subid)
                        activity?.let {
                            AlertDialog.Builder(it).apply {
                                setTitle(R.string.remind_message)
                                setMessage(R.string.remind_content)
                                setCancelable(false)
                                setPositiveButton(R.string.confirm) { _, _ ->
                                    deleteMessagesRequest("2", itemArray)
                                }
                                setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                            }.show()

                        }
                    }
                })

                alertDialog.setOnArchiveClick(object : NotificationsAlertDialog.OnArchiveListener {
                    override fun onArchiveClick(message: MessageInfo) {
                        var itemArray = JSONArray().put(message.updatetime + message.subid)
                        activity?.let {
                            AlertDialog.Builder(it).apply {
                                setTitle(R.string.remind_message)
                                setMessage(R.string.remind_content)
                                setCancelable(false)
                                setPositiveButton(R.string.confirm) { _, _ ->
                                    deleteMessagesRequest("1", itemArray)
                                }
                                setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                            }.show()
                        }
                    }
                })
            }
        })

        binding.recycleView.adapter = adapter
    }

    /**
     * 消息状态更新
     */
    private fun deleteMessagesRequest(readflg: String, updateArray: JSONArray) {
        Api.EtOfficeSetMessage(
            context = requireContext(),
            updateid = updateArray,
            readflg = readflg,
            onSuccess = { data ->
                Handler(Looper.getMainLooper()).post {
                    // 成功結果処理
                    var checkStatus = adapter.getCheckStatus()
                    var statusMap = adapter.getCheckStatus().clone() as HashMap<Int, String>
                    val msgListTmp = viewModel.messageList.clone() as ArrayList<MessageInfo>
                    val msgIterator = msgListTmp.iterator()

                    if (data is SetMessageModel) {
                        if (data.status == 0) {
                            if (viewModel.checkedPosition >= 0) {
                                viewModel.messageList.removeAt(viewModel.checkedPosition)
                            } else {
                                statusMap.forEach { (key, value) ->
                                    while (msgIterator.hasNext()) {
                                        val msgItem = msgIterator.next()
                                        val value2 = msgItem.updatetime + msgItem.subid
                                        if (value == value2) {
                                            viewModel.messageList.remove(msgItem)
                                            checkStatus.remove(key)
                                            break
                                        }
                                    }
                                }
                            }
                            activity?.let {
                                Tools.showAlertDialog(
                                    it,
                                    "",
                                    getString(R.string.update_success)
                                )
                            }
                            activity?.runOnUiThread {
                                adapter.notifyDataChange(viewModel.messageList, checkStatus)
                            }
                        } else {
                            activity?.let { Tools.showErrorDialog(it, data.message) }
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

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        viewModel.selectFlag = if (viewModel.selectFlag) {
            adapter.unSelectAll()
            false
        } else {
            adapter.selectAll()
            true
        }
    }

    override fun onRefresh() {
        Log.e(TAG, "getMessage calling...onRefresh")
        getMessageRequest(true)
    }
}

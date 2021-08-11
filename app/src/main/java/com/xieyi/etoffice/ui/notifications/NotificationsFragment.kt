package com.xieyi.etoffice.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.aspsine.swipetoloadlayout.OnLoadMoreListener
import com.aspsine.swipetoloadlayout.OnRefreshListener
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.*
import com.xieyi.etoffice.common.HttpUtil
import com.xieyi.etoffice.databinding.FragmentNotificationsBinding
import com.xieyi.etoffice.enum.ResultType
import org.json.JSONArray
import org.json.JSONObject
import java.util.HashMap
import kotlin.concurrent.thread


class NotificationsFragment : Fragment(), View.OnClickListener, OnRefreshListener, OnLoadMoreListener,
    CompoundButton.OnCheckedChangeListener {
    private val TAG = "NotificationsFragment"
    private lateinit var binding: FragmentNotificationsBinding
   // private val binding get() = _binding!!
    private lateinit var viewModel: NotificationsViewModel
    private lateinit var adapter:NotificationsAdapter

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
        // 监听按钮事件
        binding.edit.setOnClickListener(this)
        binding.selectAll.setOnCheckedChangeListener(this)
        binding.delete.setOnClickListener(this)
        binding.cancel.setOnClickListener(this)
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

//    override fun onDestroyView() {
//        super.onDestroyView()
//      //  _binding = null
//    }

    // 监听按钮事件
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

    private fun deleteData(view: View){
        var updateArray = JSONArray()
        val checkStatus = adapter.getCheckStatus()
        for((index,value) in checkStatus){
            if (value.isNotEmpty()){
                updateArray.put(value)
            }
        }
        if (updateArray.length() > 0) {
            activity?.let {
                var dialog = AlertDialog.Builder(it).apply {
                    setTitle(R.string.remind_message)
                    setMessage(R.string.remind_content)
                    setCancelable(false)
                    setPositiveButton(R.string.confirm){ _, _ ->deleteMessagesRequest("2", updateArray)}
                    setNegativeButton(R.string.cancel){ dialog, _ -> dialog.dismiss()}
                }.show()
            }
        } else {
            Snackbar.make(view, R.string.delete_message_require, Snackbar.LENGTH_SHORT).show()
            return
        }
    }

    // 最新メッセージ一覧取得
    private fun getMessageRequest() {
        thread {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetMessage")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device",Config.Device)
            jsonObject.put("count","10")
            jsonObject.put("lasttime",viewModel.lasttime)
            jsonObject.put("lastsubid",viewModel.lastsubid)
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
        if (data is NotificationsResponse && data.status == "0") {
            viewModel.appendMessage(data.result.messagelist)

            if(data.result.messagelist.size > 0) {
                var lastMessage = data.result.messagelist.last()
                viewModel.lastsubid = lastMessage.subid
                viewModel.lasttime = lastMessage.updatetime

                if(data.result.messagelist.size < 10) {
                    binding.swipeToLoadLayout.isLoadMoreEnabled = false
                    binding.swipeToLoadLayout.isLoadingMore = false
                }
                activity?.runOnUiThread{
                    adapter.notifyDataChange(viewModel.messageList)
                }
            } else {
                binding.swipeToLoadLayout.isLoadMoreEnabled = false
                binding.swipeToLoadLayout.isLoadingMore = false
            }
        }
    }

    // 通信失敗時、結果処理
    private fun onFailure(error: ResultType, data: Any){
        Log.e(TAG, "onFailure:$data");
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        // 设置LayoutManager
        binding.swipeTarget.layoutManager = layoutManager
        binding.swipeTarget.itemAnimator = DefaultItemAnimator()
        // 设置Adapter
        adapter = NotificationsAdapter(ArrayList())

        // 点击List行数据，弹出操作对话框
        adapter.setOnItemClickListener(object:NotificationsAdapter.OnItemClickListener{
            override fun onItemClick(position:Int){
                if(adapter.isEdit) {
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
                alertDialog.setOnDeleteClick(object : NotificationsAlertDialog.OnDeleteListener{
                    override fun onDeleteClick(message: Message) {
                        var itemArray = JSONArray().put(message.updatetime + message.subid)
                        activity?.let {
                           AlertDialog.Builder(it).apply {
                                setTitle(R.string.remind_message)
                                setMessage(R.string.remind_content)
                                setCancelable(false)
                                setPositiveButton(R.string.confirm){ _, _ ->
                                    deleteMessagesRequest("2", itemArray)}
                                setNegativeButton(R.string.cancel){ dialog, _ -> dialog.dismiss()}
                            }.show()
                        }
                    }
                })

                alertDialog.setOnArchiveClick(object : NotificationsAlertDialog.OnArchiveListener{
                    override fun onArchiveClick(message: Message) {
                        var itemArray = JSONArray().put(message.updatetime + message.subid)
                        deleteMessagesRequest("1", itemArray)
                        activity?.let {
                            AlertDialog.Builder(it).apply {
                                setTitle(R.string.remind_message)
                                setMessage(R.string.remind_content)
                                setCancelable(false)
                                setPositiveButton(R.string.confirm){ _, _ ->
                                    deleteMessagesRequest("1", itemArray)}
                                setNegativeButton(R.string.cancel){ dialog, _ -> dialog.dismiss()}
                            }.show()
                        }
                    }
                })
            }
        })

        binding.swipeTarget.adapter = adapter

    }

    override fun onRefresh() {
        Log.i("NotificationsFragment", "正在刷新:");
        binding.swipeToLoadLayout.postDelayed({
            binding.swipeToLoadLayout.isRefreshing = false
            binding.swipeToLoadLayout.isLoadMoreEnabled = true
            viewModel.messageList.clear()
            viewModel.lastsubid = ""
            viewModel.lasttime = ""

            adapter.initCheck(false)
            getMessageRequest()

        }, 1000)
    }

    override fun onLoadMore() {
        binding.swipeToLoadLayout.postDelayed({
            binding.swipeToLoadLayout.isLoadingMore = false

            getMessageRequest()
            adapter.notifyDataChange(viewModel.messageList)
        }, 1000)
    }

    // メッセージ更新
    private fun deleteMessagesRequest(readflg:String, updateArray:JSONArray) {
        thread {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetMessage")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device",Config.Device)
            jsonObject.put("updateid",updateArray)
            jsonObject.put("readflg",readflg)
            // 通信処理
            HttpUtil.callAsyncHttp(
                context = requireContext(),
                url = Config.LoginUrl,
                parameter = jsonObject,
                classType = ResultResponse::class.java as Class<Any>,
                authToken = true,
                fcmToken = true,
                onSuccess = ::onSuccess2,
                onFailure = ::onFailure
            )
        }
    }

    // 成功結果処理
    private fun onSuccess2(data: Any){
        var checkStatus = adapter.getCheckStatus()
        var statusMap = adapter.getCheckStatus().clone() as HashMap<Int, String>
        val msgListTmp = viewModel.messageList.clone() as ArrayList<Message>
        val msgIterator = msgListTmp.iterator()

        if (data is ResultResponse) {
            if (data.status == "0") {
                if(viewModel.checkedPosition >= 0) {
                    viewModel.messageList.removeAt(viewModel.checkedPosition)
                } else {
                    statusMap.forEach { (key, value) ->
                        while(msgIterator.hasNext()) {
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
                Snackbar.make(binding.root, R.string.update_success, Snackbar.LENGTH_SHORT).show()
                activity?.runOnUiThread {
                    adapter.notifyDataChange(viewModel.messageList, checkStatus)
                }
            }
            else {
                Snackbar.make(binding.delete, data.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
        viewModel.selectFlag = if(viewModel.selectFlag) {
                                adapter.unSelectAll()
                                false
                            } else {
                                adapter.selectAll()
                                true
                            }
    }

}

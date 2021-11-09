package com.xieyi.etoffice.ui.notifications

import android.os.Bundle
import android.view.*
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import com.xieyi.etoffice.R
import com.xieyi.etoffice.common.model.MessageInfo
import com.xieyi.etoffice.databinding.NotificationsCustomDialogBinding

class NotificationsAlertDialog : DialogFragment(), View.OnClickListener {
    private var _binding: NotificationsCustomDialogBinding? = null
    private val binding get() = _binding!!
    private var mDeleteListener: OnDeleteListener? = null
    private var mArchiveListener: OnArchiveListener? = null
    private lateinit var messageItem: MessageInfo
    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        _binding = NotificationsCustomDialogBinding.inflate(inflater, container, false)

        val bundle = arguments
        messageItem = bundle?.get("messageItem") as MessageInfo
        binding.title.text = messageItem.title
        binding.message.text = messageItem.content
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)

        // 监听事件
        binding.btnDelete.setOnClickListener(this)

        //已读监听事件
        binding.btnArchive.setOnClickListener(this)

        //ボタン　保存後に閉じる
        binding.alertCancel.setOnClickListener(this)

        initWindow()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initWindow() {
        //フルスクリーン　Full screen
        val window = dialog!!.window
        // 设备背景为透明（默认白色）
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window.decorView.setPadding(16, 16, 16, 0)
        val attributes = window.attributes

        // 设置window宽高(单位px)
        attributes.width = WindowManager.LayoutParams.WRAP_CONTENT //满屏
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置window位置
        window.attributes?.gravity = Gravity.CENTER//居中
        window.attributes = attributes
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_delete -> {
                // 通过接口回传数据给activity
                mDeleteListener?.onDeleteClick(messageItem)
                dismiss()
            }
            R.id.btn_archive -> {
                mArchiveListener?.onArchiveClick(messageItem)
                dismiss()
            }
            R.id.alert_cancel -> {
                dismiss()
            }
        }
    }

    // 回调接口，用于传递数据给Activity
    interface OnDeleteListener {
        fun onDeleteClick(message: MessageInfo)
    }

    interface OnArchiveListener {
        fun onArchiveClick(message: MessageInfo)
    }

    fun setOnDeleteClick(deleteListener: OnDeleteListener) {
        this.mDeleteListener = deleteListener
    }

    fun setOnArchiveClick(archiveListener: OnArchiveListener) {
        this.mArchiveListener = archiveListener
    }
}
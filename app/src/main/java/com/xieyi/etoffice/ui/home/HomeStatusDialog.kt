package com.xieyi.etoffice.ui.home

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.xieyi.etoffice.GpsTracker
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.base.FullScreenDialogBaseFragment
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.common.setupClearButtonWithAction
import com.xieyi.etoffice.databinding.DialogHomeStatusBinding
import kotlinx.coroutines.*


class HomeStatusDialog(statusvalue: String, statustext: String) : FullScreenDialogBaseFragment() {

    private val TAG = "HomeStatusDialog"

    private val mStatusvalue = statusvalue

    private val mStatustext = statustext

    private var longitude = 0.0

    private var latitude = 0.0

    private lateinit var binding: DialogHomeStatusBinding

    private lateinit var gpsTracker: GpsTracker


    lateinit var listener: OnDialogListener

    interface OnDialogListener {
        fun onClick(userLocation: String, memo: String)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireActivity(), theme) {
            override fun dispatchTouchEvent(motionEvent: MotionEvent): Boolean {
                // 点击屏幕空白区域，隐藏软键盘
                if (currentFocus != null) {
                    val inputMethodManager: InputMethodManager =
                        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                }
                return super.dispatchTouchEvent(motionEvent)
            }
        }
    }

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View {
        binding = DialogHomeStatusBinding.inflate(inflater, container, false)

        gpsCheck()

        //ボタン　保存後に閉じる
        binding.btnCancelAndClose.setOnClickListener {
//            listener?.onClick(
//                binding.userLocation.text.toString(),
//                binding.userStatusMemo.text.toString()
//            )
            dialog!!.dismiss()
        }

        //状態表示
        binding.state.text = mStatustext

        //更新按钮点击后，画面添加一个蒙版，按钮不能二次点击。 点击前
        binding.mengBan.visibility = View.GONE
        binding.setUserStatus.isClickable = true
        binding.setUserLocation.isClickable = true

        //set_user_Status
        binding.setUserStatus.setOnClickListener {
            if (binding.userStatusMemo.text.toString().isEmpty()) {
                activity?.let { it1 ->
                    Tools.showErrorDialog(
                        it1,
                        getString(R.string.no_memo)
                    )
                }
                return@setOnClickListener
            }

            EtOfficeSetUserStatusPost(
                longitude,
                latitude,
                binding.userLocation.text.toString(),
                mStatusvalue,
                mStatustext,
                binding.userStatusMemo.text.toString()
            )
            binding.userLocation.text.clear()
            binding.userStatusMemo.text.clear()

            //更新按钮点击后，画面添加一个蒙版，按钮不能二次点击。 点击后
            binding.mengBan.visibility = View.VISIBLE
            binding.setUserStatus.isClickable = false
            binding.setUserLocation.isClickable = false
        }

        //set_user_location
        binding.setUserLocation.setOnClickListener {

            if (binding.userLocation.text.toString().isEmpty()) {
                activity?.let { it1 ->
                    Tools.showErrorDialog(
                        it1,
                        getString(R.string.MSG11)
                    )
                }
                return@setOnClickListener
            }

            EtOfficeSetUserLocationPost(binding.userLocation.text.toString())
            binding.userLocation.text.clear()
            binding.userStatusMemo.text.clear()
        }

        //EditText，编辑框(EditText)右侧追加一个自动清除按钮，输入内容后删除按钮表示，可以清除内容。
        binding.userLocation.setupClearButtonWithAction()
        binding.userStatusMemo.setupClearButtonWithAction()

        return binding.root
    }

    private fun EtOfficeSetUserStatusPost(
        longitude: Double,
        latitude: Double,
        location: String,
        statusvalue: String,
        statustext: String,
        memo: String
    ) {
        if (gpsTracker.canGetLocation()) {
            CoroutineScope(Dispatchers.IO).launch {
                Api.EtOfficeSetUserStatus(
                    context = requireActivity(),
                    location = location,
                    longitude = longitude,
                    latitude = latitude,
                    statusvalue = statusvalue,
                    statustext = statustext,
                    memo = memo,
                    onSuccess = { model ->
                        CoroutineScope(Dispatchers.Main).launch {
                            when (model.status) {
                                0 -> {
                                    Tools.showMsg(binding.root, getString(R.string.UPDATE_SUCCESS))
                                    listener.onClick(
                                        statusvalue,
                                        statustext
                                    )
                                    dialog!!.dismiss()
                                }
                                else -> {
                                    activity?.let {
                                        Tools.showErrorDialog(
                                            it,
                                            model.message
                                        )
                                    }
                                }
                            }

                        }
                    },
                    onFailure = { error, data ->
                        CoroutineScope(Dispatchers.Main).launch {
                            Log.e(TAG, "onFailure:$data")

                        }
                    }
                )

            }
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private fun EtOfficeSetUserLocationPost(location: String) {
        if (gpsTracker.canGetLocation()) {
            CoroutineScope(Dispatchers.IO).launch {
                Api.EtOfficeSetUserLocation(
                    context = requireActivity(),
                    location = location,
                    latitude = latitude,
                    longitude = longitude,
                    onSuccess = { model ->
                        CoroutineScope(Dispatchers.Main).launch {

                            when (model.status) {
                                0 -> {
                                    activity?.let {
                                        Tools.showAlertDialog(
                                            it,
                                            it.getString(R.string.MESSAGE),
                                            getString(R.string.MSG11)
                                        )
                                    }
                                }
                                else -> {
                                    activity?.let {
                                        Tools.showErrorDialog(
                                            it,
                                            model.message
                                        )
                                    }
                                }
                            }
                        }

                    },
                    onFailure = { error, data ->
                        CoroutineScope(Dispatchers.Main).launch {
                            Log.e(TAG, "onFailure:$data")

                        }
                    }
                )
            }

        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private fun gpsCheck() {
        gpsTracker = GpsTracker(activity)
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.latitude
            longitude = gpsTracker.longitude
            binding.latitude.text = latitude.toString()
            binding.longitude.text = longitude.toString()
        } else {
            gpsTracker.showSettingsAlert()
        }
    }
}

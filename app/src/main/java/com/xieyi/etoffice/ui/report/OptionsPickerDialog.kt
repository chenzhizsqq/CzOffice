package com.xieyi.etoffice.ui.report

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.xieyi.etoffice.*
import com.xieyi.etoffice.common.HttpUtil
import com.xieyi.etoffice.databinding.DialogBottomPickerBinding
import com.xieyi.etoffice.databinding.DialogReportAddBinding
import com.xieyi.etoffice.enum.RequestMethod
import com.xieyi.etoffice.enum.ResultType
import org.json.JSONObject
import kotlin.concurrent.thread


class OptionsPickerDialog:  DialogFragment() {
    private val TAG = "OptionsPickerDialog"
    private lateinit var optionsData: ArrayList<OptionItem>
    private lateinit var adapter: OptionsPickerAdater
    private lateinit var flag:String
    private var _binding:DialogBottomPickerBinding? = null
    private val binding get() = _binding!!

    var mlistener: OnDialogListener ? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogBottomPickerBinding.inflate(inflater, container, false)

        val bundle = arguments
        flag = bundle?.getString("flag")?:"10"
        optionsData = bundle?.get("data") as ArrayList<OptionItem>

        adapter = OptionsPickerAdater(this.requireContext(), R.layout.option_item, optionsData)
        binding.optionList.adapter = adapter
        binding.optionList.setOnItemClickListener{parent,view,position,id->
            val itemName = optionsData[position].name
            val itemCode = optionsData[position].code
            // 通过接口回传数据给activity
            mlistener?.onDialogClick(itemCode, itemName)
            dismiss()
        }
        if (flag == "10") {
            binding.listTitle.text = "プロジェクト名を選択する"
        } else {
            binding.listTitle.text = "作業コードを選択する"
        }

        binding.buttonCancle.setOnClickListener { dismiss() }
       // getProjectList()
        initWindow()

        return binding.root
    }

    private fun initWindow() {
        //フルスクリーン　Full screen
        val window = dialog!!.window
        // 设备背景为透明（默认白色）
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window.decorView.setPadding(0, 16, 0, 0);
        val attributes = window.attributes

        // 设置window宽高(单位px)
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT //满屏
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        //设置window位置
        window?.attributes?.gravity = Gravity.BOTTOM//居中
        window.attributes = attributes
    }

    // 回调接口，用于传递数据给Activity -------
    interface OnDialogListener  {
        fun onDialogClick(code:String, name:String)
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.mlistener = dialogListener
    }

}

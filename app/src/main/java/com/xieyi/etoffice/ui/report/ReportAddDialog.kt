package com.xieyi.etoffice.ui.report

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.OptionsPickerView
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.Api
import com.xieyi.etoffice.databinding.DialogReportAddBinding


class ReportAddDialog : DialogFragment(),View.OnClickListener{
    private val TAG = "ReportAddDialog"
    private var _binding: DialogReportAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs:SharedPreferences
    private lateinit var viewModel: ReportAddViewModel
    private lateinit var pvOptions:OptionsPickerView<Any>

    var listener: OnDialogListener? = null

    interface OnDialogListener {
        fun onClick()
    }

    fun setOnDialogListener(dialogListener: OnDialogListener) {
        this.listener = dialogListener
    }

    companion object {
        fun actionStart(fm: FragmentManager,reportDate:String){
            val bundle = Bundle()
            bundle.putString("reportDate", reportDate)
            val reportAddDialog = ReportAddDialog()
            reportAddDialog.arguments = bundle
            reportAddDialog.show(fm, "ReportAddDialog")
        }
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
        //return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View {
        _binding = DialogReportAddBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this).get(ReportAddViewModel::class.java)

        val bundle = arguments
        if (bundle != null) {
            viewModel.reportAddDate = bundle.getString("reportDate", "")
        }
        // 初始化时间表示内容
        viewModel.initTime()

        prefs = EtOfficeApp.context.getSharedPreferences(Config.EtOfficeUser, Context.MODE_PRIVATE)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        // 开始时间监听事件
        binding.btnStarttime.setOnClickListener(this)

        //終わり時間選択を表示
        binding.btnEndtime.setOnClickListener(this)

        //ボタン　保存後に閉じる
        binding.btnSave.setOnClickListener(this)

        // 取消按钮事件
        binding.btnCancel.setOnClickListener(this)

        // 工程选择
        binding.projectPicker.setOnClickListener(this)

        // 作业选择
        binding.wbsPicker.setOnClickListener(this)

        // 工数选择
        binding.btnWorktime.setOnClickListener(this)

        initWindow()
        searchProject()

        // 工程Cd改变时，重新取得作业Cd
        viewModel.projectCd.observe(this, androidx.lifecycle.Observer {
            viewModel.getProjectWbsOption()
        })

        return binding.root
    }

    /**
     * 销毁View
     **/
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * 监听事件
     **/
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.btn_starttime -> {
                initTimePicker(binding.btnStarttime, binding.tvStarttime)
            }
            R.id.btn_endtime ->{
                initTimePicker(binding.btnEndtime, binding.tvEndtime)
            }
            // 工程选择
            R.id.project_picker -> {
                val dialog = OptionsPickerDialog()
                val bundle = Bundle()
                bundle.putString("flag", "10")
                bundle.putSerializable("data",viewModel.projectPickerData)
                dialog.arguments = bundle
                parentFragmentManager?.let { dialog.show(it, "bottomDialog") }
                dialog.setOnDialogListener(object : OptionsPickerDialog.OnDialogListener{
                    override fun onDialogClick(code: String, name: String) {
                        binding.projectPicker.text = "$code - $name"
                        binding.projectCode.text = code
                        viewModel.projectCd.value = code
                    }
                })
            }
            // 作业选择
            R.id.wbs_picker -> {
                var projectcd = binding.projectCode.text.toString()
                if (projectcd.isEmpty()) {
                    activity?.let { Tools.showErrorDialog(it, getString(R.string.MSG14)) }
                    return
                }
                val dialog = OptionsPickerDialog()
                val bundle = Bundle()
                bundle.putString("flag", "11")
                bundle.putSerializable("data",viewModel.wbsPickerData)
                dialog.arguments = bundle
                parentFragmentManager?.let { dialog.show(it, "bottomDialog") }
                dialog.setOnDialogListener(object : OptionsPickerDialog.OnDialogListener{
                    override fun onDialogClick(code: String, name: String) {
                        binding.wbsPicker.text = "$code - $name"
                        binding.wbsCode.text = code
                    }
                })
            }
            R.id.btn_worktime->{
                initTimePicker(binding.btnWorktime,binding.tvWorktime)
            }
            R.id.btn_cancel->{
                dialog!!.dismiss()
            }
            R.id.btn_save->{
                var projectCd = binding.projectCode.text
                if (projectCd.isEmpty()) {
                    activity?.let {
                        Tools.showErrorDialog(it,getString(R.string.MSG14))
                    }
                    return
                }
                var wbsCode = binding.wbsCode.text
                if (wbsCode.isEmpty()) {
                    activity?.let {
                        Tools.showErrorDialog(it,getString(R.string.wbs_require))
                    }
                    return
                }
                var worktime = binding.tvWorktime.text
                if (worktime.isEmpty()) {
                    activity?.let {
                        Tools.showErrorDialog(it, getString(R.string.workTime_require))
                    }
                    return
                }
                sendSetReport()
            }
        }
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
        attributes.height = WindowManager.LayoutParams.MATCH_PARENT
        //设置window位置
        window?.attributes?.gravity = Gravity.CENTER//居中
        window.attributes = attributes
    }

    private fun initTimePicker(clickView: Button, valueView:TextView) {
        OptionsPickerView.Builder(context,
            OptionsPickerView.OnOptionsSelectListener { options1, options2, options3, v ->
                clickView.text = viewModel.hourList[options1] + ":" + viewModel.minuteList[options2]
                valueView.text = viewModel.hourList[options1] +  viewModel.minuteList[options2]
            })
            .setDividerColor(Color.LTGRAY)//设置分割线的颜色
            .setSelectOptions(8, 0, 0)//默认选中项
            .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
            .setContentTextSize(20)
            .setCyclic(false,false,false)
            .setTitleText(getString(R.string.select_time))
            .setDecorView(dialog!!.window!!.decorView as ViewGroup)
            .build().also { pvOptions = it }
        pvOptions.setNPicker(viewModel.hourList as List<Any>?, viewModel.minuteList as List<Any>?, null)
        pvOptions.show()
    }

    /**
     * 日報登録
     **/
    private fun sendSetReport() {
        Api.EtOfficeSetReport(
            context = requireContext(),
            ymd = viewModel.reportAddDate,
            projectcd = binding.projectCode.text.toString(),
            wbscd= binding.wbsCode.text.toString(),
            totaltime = binding.tvWorktime.text.toString(),
            starttime= binding.tvStarttime.text.toString(),
            endtime = binding.tvEndtime.text.toString(),
            place = binding.etPlace.text.toString(),
            memo = binding.etWorkDetail.text.toString(),
            onSuccess = { data ->
                Handler(Looper.getMainLooper()).post {
                    if (data.status == 0) {
                        listener?.onClick()
                        dialog!!.dismiss()
                    } else {
                        activity?.let { Tools.showErrorDialog(it, data.message) }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data");
                    //CommonUtil.handleError(it, error, data)
                }
            }
        )
    }


    /**
     * プロジェクト一覧
     * */
    private fun searchProject() {
        Api.EtOfficeGetProject(
            context = requireContext(),
            ymd = viewModel.reportAddDate,
            onSuccess = { data ->
                Handler(Looper.getMainLooper()).post {
                    if (data.status == 0) {
                        viewModel.projectList.clear()
                        for(project in data.result.projectlist){
                            viewModel.projectList.add(project)
                            viewModel.initProjectOption()
                        }
                    }
                }
            },
            onFailure = { error, data ->
                Handler(Looper.getMainLooper()).post {
                    Log.e(TAG, "onFailure:$data");
                    //CommonUtil.handleError(it, error, data)
                }
            }
        )
    }
}

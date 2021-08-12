package com.xieyi.etoffice.ui.report

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.bigkoo.pickerview.OptionsPickerView
import com.google.android.material.snackbar.Snackbar
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.R
import com.xieyi.etoffice.common.HttpUtil
import com.xieyi.etoffice.databinding.DialogReportAddBinding
import com.xieyi.etoffice.enum.ResultType
import org.json.JSONObject
import kotlin.concurrent.thread


class ReportAddDialog : DialogFragment(),View.OnClickListener{
    private val TAG = "ReportAddDialog"
    private var _binding: DialogReportAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs:SharedPreferences
    private lateinit var viewModel: ReportAddViewModel
    private lateinit var pvOptions:OptionsPickerView<Any>

    companion object {
        fun newInstance(): ReportAddDialog {
            return ReportAddDialog()
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
        return super.onCreateDialog(savedInstanceState)
    }
    override fun onCreateView(
            @NonNull inflater: LayoutInflater,
            @Nullable container: ViewGroup?,
            @Nullable savedInstanceState: Bundle?
    ): View {
        _binding = DialogReportAddBinding.inflate(inflater, container, false)
        viewModel =
            ViewModelProvider(this).get(ReportAddViewModel::class.java)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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
                    Snackbar.make(view, R.string.project_require, Snackbar.LENGTH_SHORT).show()
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
                    Snackbar.make(view,R.string.project_require,Snackbar.LENGTH_SHORT).show()
                    return
                }
                var wbsCode = binding.wbsCode.text
                if (wbsCode.isEmpty()) {
                    Snackbar.make(view,R.string.wbs_require,Snackbar.LENGTH_SHORT).show()
                    return
                }
                var worktime = binding.tvWorktime.text
                if (worktime.isEmpty()) {
                    Snackbar.make(view, R.string.workTime_require,Snackbar.LENGTH_SHORT).show()
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
            .setTitleText("時間を選択する")
            .setDecorView(dialog!!.window!!.decorView as ViewGroup)
           // .isDialog(true)
            .build().also { pvOptions = it }
        pvOptions.setNPicker(viewModel.hourList as List<Any>?, viewModel.minuteList as List<Any>?, null)
        pvOptions.show()
    }

    // 最新メッセージ一覧取得
    private fun sendSetReport() {

        thread {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeSetReport")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device",Config.Device)
            jsonObject.put("userid",prefs.getString("userid", ""))
            jsonObject.put("ymd","20210728")
            jsonObject.put("projectcd",binding.projectCode.text)
            jsonObject.put("wbscd",binding.wbsCode.text)
            jsonObject.put("totaltime",binding.tvWorktime.text)
            jsonObject.put("starttime",binding.tvStarttime.text)
            jsonObject.put("endtime",binding.tvEndtime.text)
            jsonObject.put("place",binding.etPlace.text)
            jsonObject.put("memo",binding.etWorkDetail.text)

            // 通信処理
            HttpUtil.callAsyncHttp(
                context = requireContext(),
                url = Config.LoginUrl,
                parameter = jsonObject,
                classType = ReportResponse::class.java as Class<Any>,
                authToken = true,
                fcmToken = true,
                onSuccess = ::onSuccess,
                onFailure = ::onFailure
            )
        }
    }

    // 成功結果処理
    private fun onSuccess(data: Any){
        if (data is ReportResponse) {
            dialog!!.dismiss()
        }
    }

    // 通信失敗時、結果処理
    private fun onFailure(error: ResultType, data: Any){
        Log.e(TAG, "onFailure:$data");
    }


    // 最新メッセージ一覧取得
    private fun searchProject() {
        thread {
            val jsonObject = JSONObject()
            jsonObject.put("app", "EtOfficeGetProject")
            jsonObject.put("token", EtOfficeApp.Token)
            jsonObject.put("tenant", EtOfficeApp.TenantId)
            jsonObject.put("hpid", EtOfficeApp.HpId)
            jsonObject.put("device",Config.Device)
            jsonObject.put("userid",prefs.getString("userid", ""))
            jsonObject.put("ymd","20210727")
            // 通信処理
            HttpUtil.callAsyncHttp(
                context = requireContext(),
                url = Config.LoginUrl,
                parameter = jsonObject,
                classType = ProjectResponse::class.java as Class<Any>,
                authToken = true,
                fcmToken = true,
                onSuccess = ::onSuccess2,
                onFailure = ::onFailure
            )
        }
    }

    // 成功結果処理
    private fun onSuccess2(data: Any){
        if (data is ProjectResponse) {
            viewModel.projectList.clear()
            for(project in data.result.projectlist){
                viewModel.projectList.add(project)
                viewModel.initProjectOption()
            }
        }
    }
}

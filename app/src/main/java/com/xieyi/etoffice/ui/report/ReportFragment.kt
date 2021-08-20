package com.xieyi.etoffice.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.xieyi.etoffice.databinding.FragmentReportBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetReportList
import com.xieyi.etoffice.jsonData.EtOfficeSetApprovalJsk

import com.xieyi.etoffice.ui.home.HomeReportDialog
import kotlinx.coroutines.*
import java.util.*


class ReportFragment : Fragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = javaClass.simpleName


    private lateinit var pEtOfficeGetReportList : EtOfficeGetReportList
    private lateinit var pEtOfficeSetApprovalJsk : EtOfficeSetApprovalJsk


    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mAdapter: GetReportListGroupAdapter


    private lateinit var binding: FragmentReportBinding



    class checkTagYmd{
        var tag:String = ""
        var ymd:String = ""

    }
    private val arrayListTagYmd = ArrayList<checkTagYmd>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.e(TAG, "onCreate: begin")

        pEtOfficeGetReportList = EtOfficeGetReportList()
        pEtOfficeSetApprovalJsk = EtOfficeSetApprovalJsk()
    }



    private lateinit var viewModel: ReportViewModel
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        viewModel =
            ViewModelProvider(this).get(ReportViewModel::class.java)

        mSwipeRefreshLayout= binding.swipeRefreshLayout

        // Listenerをセット
        mSwipeRefreshLayout.setOnRefreshListener(this)


        mRecyclerView = binding.recyclerViewGetReport

        topMenu()

        refreshPage()

        return binding.root
    }

    private fun refreshPage() {
        GlobalScope.launch(errorHandler) {
            withContext(Dispatchers.IO) {
                arrayListTagYmd.clear()
                //データ更新
                try {

                    //日報一覧取得
                    val r = pEtOfficeGetReportList.post()
                    Log.e(TAG, "pEtOfficeGetReportList.post() :$r")

                    if(r=="0"){
                        doOnUiCode()
                    }

                } catch (e: Exception) {
                    Log.e(TAG, "pEtOfficeGetReportList.post()",e)

                }

            }
        }
    }


    private fun topMenu() {

        //出勤記録を表示します
        binding.people.setOnClickListener {
            val mHomeReportDialog = HomeReportDialog()

            val fragmentManager = this@ReportFragment.parentFragmentManager
            fragmentManager.let { it1 -> mHomeReportDialog.show(it1, "mHomeReportDialog")  }
        }


        viewModel.visibility.observe(viewLifecycleOwner,{
            binding.allSelect.visibility = it
            binding.commit.visibility = it
            binding.people.visibility = it
        })

        binding.edit.setOnClickListener(View.OnClickListener {
            viewModel.visibilityChange()
        })


        //allSelect click
        binding.allSelect.setOnClickListener {
            viewModel.allSelectChange()
        }

        //commit click
        binding.commit.setOnClickListener {
            commitAlertDialog()

        }
    }

    private fun commitClick() {
        if (viewModel.visibility.value == View.VISIBLE) {
            try {
                GlobalScope.launch(errorHandler) {
                    withContext(Dispatchers.IO) {
                        //指定された　発信
                        val ymdArray = ArrayList<String>()
                        for (tagYmd in arrayListTagYmd) {
                            val checkBox: CheckBox =
                                binding.root.findViewWithTag(tagYmd.tag) as CheckBox
                            if (checkBox.isChecked) {

                                ymdArray.add(tagYmd.ymd)
                            }
                        }
                        if(ymdArray.size>0){
                            var r: String = "-1"
                            r = pEtOfficeSetApprovalJsk.post(ymdArray)
                            Log.e(TAG, "topMenu: r:$r")


                            if(r=="0"){
                                //データ更新
                                try {

                                    //日報一覧取得
                                    Log.e(TAG, "commitClick: 日報一覧取得", )
                                    refreshPage()

                                } catch (e: Exception) {
                                    Log.e(TAG, "pEtOfficeGetReportList.post()",e)

                                }
                            }
                        }

                    }

                }

            } catch (e: Exception) {
                Log.e(TAG, "tv_commit.setOnClickListener", e)
            }
        }
    }


    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    private suspend fun doOnUiCode() {
        withContext(Dispatchers.Main) {
            viewModel.allSelectChangeFalse()
            mAdapter= activity?.let {
                GetReportListGroupAdapter(
                    pEtOfficeGetReportList.infoJson().result.group
                    ,arrayListTagYmd
                    ,it
                    ,viewModel
                    ,viewLifecycleOwner
                )
            }!!
            mRecyclerView.adapter = mAdapter



            mAdapter.setOnAdapterListener(object : GetReportListGroupAdapter.OnAdapterListener{
                override fun onClick(tenantid: String) {
                    GlobalScope.launch(errorHandler) {
                        withContext(Dispatchers.IO) {

                        }
                    }

                }
            })
        }
    }




    private fun commitAlertDialog(){

        val ymdArray = ArrayList<String>()

        for (tagYmd in arrayListTagYmd) {
            val checkBox: CheckBox =
                binding.root.findViewWithTag(tagYmd.tag) as CheckBox
            if (checkBox.isChecked) {

                ymdArray.add(tagYmd.ymd)
            }
        }
        if(ymdArray.size>0) {
            AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                .setTitle("消息")
                .setMessage("現在選択されている情報を承認しますか？")
                .setPositiveButton("确定") { _, which ->
                    commitClick()
                }
                .setNegativeButton("取消") { _, which ->
                }
                .show()
        }else{
            AlertDialog.Builder(activity) // FragmentではActivityを取得して生成
                .setTitle("消息")
                .setMessage("まだ選択していません。選択してください。")
                .setPositiveButton("确定") { _, which ->
                }
                .show()
        }
    }

    companion object {
        fun newInstance(): ReportFragment {
            return ReportFragment()
        }
    }

    override fun onRefresh() {
        Log.e(TAG, "onRefresh: begin", )
        mSwipeRefreshLayout.isRefreshing = false;
        refreshPage()
    }
}
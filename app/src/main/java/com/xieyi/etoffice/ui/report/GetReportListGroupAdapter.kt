package com.xieyi.etoffice.ui.report

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.GroupInfo
import com.xieyi.etoffice.databinding.GetReportListGroupBinding

//EtOfficeGetReportList.json result中的group
class GetReportListGroupAdapter : RecyclerView.Adapter<GetReportListGroupAdapter.ViewHolder>() {
    val TAG: String = "GetReportListGroupAdapter"

    var list = listOf<GroupInfo>()
    var arrayListYmd = ArrayList<String>()
    lateinit var activity: Activity
    lateinit var viewModel: ReportViewModel
    lateinit var lifecycleOwner: LifecycleOwner


    private lateinit var binding: GetReportListGroupBinding
    private lateinit var listener: OnAdapterListener

    fun notifyDataSetChanged(
        list: List<GroupInfo>,
        arrayListYmd: ArrayList<String>,
        activity: Activity,
        viewModel: ReportViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        //Log.e(TAG, "notifyDataSetChanged: begin", )
        this.list = listOf()
        this.list = list
        //Log.e(TAG, "notifyDataSetChanged: list:"+this.list.toString() )

        this.arrayListYmd.clear()
        this.arrayListYmd = arrayListYmd
        //Log.e(TAG, "notifyDataSetChanged: arrayListYmd:"+this.arrayListYmd.toString() )

        this.activity = activity
        this.viewModel = viewModel
        this.lifecycleOwner = lifecycleOwner
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(yyyymmdd: String, isApproved: Boolean)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    inner class ViewHolder(binding: GetReportListGroupBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val month: TextView = binding.month
        val recyclerView: RecyclerView = binding.recyclerViewGetReportReportlist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            GetReportListGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.month.text =
            Tools.dateGetYear(list[position].month) + "." + Tools.dateGetMonth(list[position].month)

        Log.e(TAG, "onBindViewHolder: position:$position")
        Log.e(TAG, "onBindViewHolder: list[position].reportlist:"+list[position].reportlist.toString() )
        Log.e(TAG, "onBindViewHolder: arrayListYmd:$arrayListYmd")
        val mAdapter = GetReportListGroupReportlistAdapter()
        mAdapter.notifyDataSetChanged(
            list[position].reportlist,
            arrayListYmd,
            activity,
            viewModel,
            lifecycleOwner
        )

        binding.recyclerViewGetReportReportlist.adapter = mAdapter
        mAdapter.setOnAdapterListener(object :
            GetReportListGroupReportlistAdapter.OnAdapterListener {
            override fun onClick(yyyymmdd: String, isApproved: Boolean) {
                listener.onClick(yyyymmdd, isApproved)
            }
        })

        viewModel.mIsScrolled.observe(lifecycleOwner,{

            val location = IntArray(2)
            holder.month.getLocationOnScreen(location)

            val stateList = ReportState()
            stateList.mScrolledName = holder.month.text.toString()
            stateList.mScrolledY = location[1]
            stateList.mPosition = position
            viewModel.mLiveDataReportState.value = stateList
        })
    }
}
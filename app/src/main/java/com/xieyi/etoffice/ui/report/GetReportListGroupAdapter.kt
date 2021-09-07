package com.xieyi.etoffice.ui.report

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.GroupInfo
import com.xieyi.etoffice.databinding.GetReportListGroupBinding

//EtOfficeGetReportList.json result中的group
class GetReportListGroupAdapter(
    val list: List<GroupInfo>,
    var arrayListYmd: ArrayList<String>,
    val activity: Activity,
    val viewModel: ReportViewModel,
    val lifecycleOwner: LifecycleOwner

) : RecyclerView.Adapter<GetReportListGroupAdapter.ViewHolder>() {
    val TAG: String = javaClass.simpleName

    private lateinit var binding: GetReportListGroupBinding
    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener {
        fun onClick(tenantid: String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    inner class ViewHolder(binding: GetReportListGroupBinding) : RecyclerView.ViewHolder(binding.root) {
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

        holder.month.text = Tools.dateGetYear(list[position].month) + "." + Tools.dateGetMonth(list[position].month)
        binding.recyclerViewGetReportReportlist.adapter = GetReportListGroupReportlistAdapter(
            list[position].reportlist,
            arrayListYmd,
            activity,
            viewModel,
            lifecycleOwner
        )
    }
}
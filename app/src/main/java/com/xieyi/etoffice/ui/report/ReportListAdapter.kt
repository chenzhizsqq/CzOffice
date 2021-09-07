package com.xieyi.etoffice.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.EtOfficeApp
import com.xieyi.etoffice.R
import com.xieyi.etoffice.common.model.ReportInfo
import com.xieyi.etoffice.databinding.GetReportListBinding


class ReportListAdapter(
    val list: List<ReportInfo>
) : RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {
    val TAG: String = "ReportListAdapter"


    inner class ViewHolder(binding: GetReportListBinding) : RecyclerView.ViewHolder(binding.root) {
        val project: TextView = binding.project
        val wbs: TextView = binding.wbs
        val time: TextView = binding.time
        val memo: TextView = binding.memo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetReportListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.project.text = EtOfficeApp.context.getString(R.string.project_title) + list[position].project
        holder.wbs.text = EtOfficeApp.context.getString(R.string.wbs_title) + list[position].wbs
        holder.time.text = EtOfficeApp.context.getString(R.string.time_title) + list[position].time
        holder.memo.text = EtOfficeApp.context.getString(R.string.memo_title) + list[position].memo
    }
}
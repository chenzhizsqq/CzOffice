package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.ReportInfo
import com.xieyi.etoffice.databinding.GetReportListBinding


class ReportListAdapter(
    val list: List<ReportInfo>
) : RecyclerView.Adapter<ReportListAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName


    class ViewHolder(binding: GetReportListBinding) : RecyclerView.ViewHolder(binding.root) {

        val project: TextView = binding.project
        val wbs: TextView = binding.wbs
        val time: TextView = binding.time
        val memo: TextView = binding.memo



        //bind
        fun bind(info: ReportInfo) {
            project.text = "プロジェクト：" + info.project
            wbs.text = "作業コード："+info.wbs
            time.text = "工数："+info.time
            memo.text = "報告："+info.memo

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetReportListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
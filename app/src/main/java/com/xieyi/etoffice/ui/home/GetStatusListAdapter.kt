package com.xieyi.etoffice.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.StatusInfo
import com.xieyi.etoffice.databinding.GetStatusListBinding


class GetStatusListAdapter(
    var list: List<StatusInfo>,
) : RecyclerView.Adapter<GetStatusListAdapter.ViewHolder>() {
    val TAG: String = "GetStatusListAdapter"

    open class ViewHolder(binding: GetStatusListBinding) : RecyclerView.ViewHolder(binding.root) {
        val statusTime: TextView = binding.statusTime
        val status: TextView = binding.status
        val memo: TextView = binding.memo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetStatusListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = list[position]

        holder.statusTime.text = Tools.allDateTime(info.statustime)
        holder.status.text = info.statustext
        holder.memo.text = info.memo
    }

    /**
     * データ更新
     *
     * @param list ステータスリスト
     */
    fun updateData(recordList: List<StatusInfo>) {
        this.list = recordList
    }
}
package com.xieyi.etoffice.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.StatusInfo
import com.xieyi.etoffice.databinding.GetStatusListHomeBinding


class GetStatusListHomeAdapter(
    var list: List<StatusInfo>,
) : RecyclerView.Adapter<GetStatusListHomeAdapter.ViewHolder>() {
    val TAG: String = "GetStatusListHomeAdapter"

    fun notifyDataChange(list: List<StatusInfo>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: GetStatusListHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        val statusTime: TextView = binding.statusTime
        val status: TextView = binding.status
        val memo: TextView = binding.memo
        val ll: LinearLayout = binding.ll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetStatusListHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.statusTime.text = Tools.allDateTime(list[position].statustime)
        holder.status.text = list[position].statustext
        holder.memo.text = list[position].memo
        holder.ll.setOnClickListener {
            listener.onClick()
        }
    }

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener {
        fun onClick()
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }
}
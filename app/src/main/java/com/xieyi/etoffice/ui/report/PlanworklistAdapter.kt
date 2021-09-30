package com.xieyi.etoffice.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.PlanWorkInfo
import com.xieyi.etoffice.databinding.GetPlanWorkInfoBinding


class PlanworklistAdapter : RecyclerView.Adapter<PlanworklistAdapter.ViewHolder>() {
    val TAG: String = "PlanworklistAdapter"

    lateinit var list: List<PlanWorkInfo>

    fun notifyDataSetChanged(list: List<PlanWorkInfo>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: GetPlanWorkInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val project: TextView = binding.project
        val wbs: TextView = binding.wbs
        val date: TextView = binding.date
        val time: TextView = binding.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetPlanWorkInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.project.text = list[position].project
        holder.wbs.text = list[position].wbs
        holder.date.text = list[position].date
        holder.time.text = list[position].time
    }
}
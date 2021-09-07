package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.WorkStatusInfo
import com.xieyi.etoffice.databinding.GetWorkStatusInfoBinding


class WorkstatuslistAdapter(
    val list:  List<WorkStatusInfo>,
) : RecyclerView.Adapter<WorkstatuslistAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName


    inner class ViewHolder(binding: GetWorkStatusInfoBinding) : RecyclerView.ViewHolder(binding.root) {
         val location: TextView = binding.location
         val time: TextView = binding.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetWorkStatusInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.location.text = list[position].status
        holder.time.text = list[position].time
    }
}
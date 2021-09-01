package com.xieyi.etoffice.ui.home
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.StatusInfo
import com.xieyi.etoffice.databinding.GetStatusListBinding


class GetStatusListAdapter(
    val list: List<StatusInfo>,
) : RecyclerView.Adapter<GetStatusListAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName


    class ViewHolder(binding: GetStatusListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val statusTime: TextView = binding.statusTime
        private val status: TextView = binding.status
        private val memo: TextView = binding.memo


        //bind
        fun bind(info: StatusInfo) {
            statusTime.text =Tools.allDateTime(info.statustime)
            status.text = info.statustext
            memo.text = info.memo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetStatusListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
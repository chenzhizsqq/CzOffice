package com.xieyi.etoffice.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.MessageInfo
import com.xieyi.etoffice.databinding.GetMessageListBinding
import java.util.ArrayList


class GetMessageAdapter : RecyclerView.Adapter<GetMessageAdapter.ViewHolder>() {
    val TAG: String = "GetMessageAdapter"

    private var list= ArrayList<MessageInfo>()

    fun notifyDataChange( list: List<MessageInfo>) {
        this.list = list as ArrayList<MessageInfo>
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: GetMessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_title: TextView = binding.title
        val tv_updatetime: TextView = binding.updatetime
        val tv_content: TextView = binding.content
        val ll: LinearLayout = binding.ll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetMessageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_title.text = list[position].title
        holder.tv_updatetime.text = Tools.allDateTime(list[position].updatetime)
        holder.tv_content.text = list[position].content
    }
}
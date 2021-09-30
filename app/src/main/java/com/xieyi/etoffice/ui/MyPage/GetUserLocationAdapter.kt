package com.xieyi.etoffice.ui.MyPage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.UserLocationInfo
import com.xieyi.etoffice.databinding.GetUserLocationBinding


class GetUserLocationAdapter(
    var list: List<UserLocationInfo>,
) : RecyclerView.Adapter<GetUserLocationAdapter.ViewHolder>() {
    val TAG: String = "GetUserLocationAdapter"


    inner class ViewHolder(binding: GetUserLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val location: TextView = binding.location
        val ll: LinearLayout = binding.ll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetUserLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.location.text = list[position].location
    }

    fun notifyDataSetChanged(list: List<UserLocationInfo>) {
        this.list = list
        notifyDataSetChanged()
    }
}
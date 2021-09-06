package com.xieyi.etoffice.ui.MyPage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.UserLocationInfo
import com.xieyi.etoffice.databinding.GetUserLocationBinding


class GetUserLocationAdapter(
    val list: List<UserLocationInfo>,
) : RecyclerView.Adapter<GetUserLocationAdapter.ViewHolder>() {
    val TAG: String = javaClass.simpleName


    class ViewHolder(binding: GetUserLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        //        private val latitude: TextView = binding.latitude
//        private val longitude: TextView = binding.longitude
        private val location: TextView = binding.location


        val ll: LinearLayout = binding.ll

        //bind
        fun bind(info: UserLocationInfo) {
//            latitude.text = Tools.srcContent(locationList.latitude,8,"")
//            longitude.text = Tools.srcContent(locationList.longitude,8,"")
            location.text = info.location


        }
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
        holder.bind(list[position])
    }
}
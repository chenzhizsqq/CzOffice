package com.xieyi.etoffice.ui.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.EtOfficeGetStuffList


class FlowerAdapter(val getStuffList: List<EtOfficeGetStuffList.SectionList>) :
    RecyclerView.Adapter<FlowerAdapter.sectionListViewHolder>() {

    // Describes an item view and its place within the RecyclerView
    class sectionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_userkana: TextView = itemView.findViewById(R.id.userkana)
        private val tv_username: TextView = itemView.findViewById(R.id.username)
        private val tv_phone: TextView = itemView.findViewById(R.id.phone)
        private val tv_sectioncd: TextView = itemView.findViewById(R.id.sectioncd)
        private val tv_sectionname: TextView = itemView.findViewById(R.id.sectionname)
        private val tv_mail: TextView = itemView.findViewById(R.id.mail)

        fun bind(sectionList: EtOfficeGetStuffList.SectionList) {
            tv_userkana.text = sectionList.stufflist[0].userkana
            tv_username.text = sectionList.stufflist[0].username
            tv_phone.text = sectionList.stufflist[0].phone
            tv_sectioncd.text = sectionList.sectioncd
            tv_sectionname.text = sectionList.sectionname
            tv_mail.text = sectionList.stufflist[0].mail
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.get_stuff_list, parent, false)

        return sectionListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return getStuffList.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.bind(getStuffList[position])
    }
}
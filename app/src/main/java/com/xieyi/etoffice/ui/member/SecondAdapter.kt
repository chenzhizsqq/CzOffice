package com.xieyi.etoffice.ui.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.EtOfficeGetStuffList


class SecondAdapter(val getStuffList: List<EtOfficeGetStuffList.StuffList>,val sectioncd:String,val sectionname:String) :
    RecyclerView.Adapter<SecondAdapter.sectionListViewHolder>() {

    // Describes an item view and its place within the RecyclerView
    class sectionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_userkana: TextView = itemView.findViewById(R.id.userkana)
        private val tv_username: TextView = itemView.findViewById(R.id.username)
        private val tv_phone: TextView = itemView.findViewById(R.id.phone)
        private val tv_mail: TextView = itemView.findViewById(R.id.mail)

        private val tv_sectioncd: TextView = itemView.findViewById(R.id.sectioncd)
        private val tv_sectionname: TextView = itemView.findViewById(R.id.sectionname)

        fun bind(stufflist: EtOfficeGetStuffList.StuffList, sectioncd:String, sectionname:String) {
            tv_userkana.text = stufflist.userkana
            tv_username.text = stufflist.username
            tv_phone.text = stufflist.phone
            tv_mail.text = stufflist.mail
            tv_sectioncd.text = sectioncd
            tv_sectionname.text = sectionname
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.get_stuff_list_second, parent, false)

        return sectionListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return getStuffList.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.bind(getStuffList[position],sectioncd,sectionname)
    }
}
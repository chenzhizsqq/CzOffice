package com.xieyi.etoffice.ui.member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.EtOfficeGetStuffList


class GetStuffSectionListAdapter(val sectionList: List<EtOfficeGetStuffList.SectionList>) :
    RecyclerView.Adapter<GetStuffSectionListAdapter.sectionListViewHolder>() {
    val TAG:String = "FirstAdapter"

    // Describes an item view and its place within the RecyclerView
    class sectionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val recyclerView: RecyclerView = itemView.findViewById(R.id.recycler_view_first)

        fun bind(sectionList: EtOfficeGetStuffList.SectionList) {
            recyclerView.adapter = GetStuffStuffListAdapter(sectionList.stufflist,sectionList.sectioncd,sectionList.sectionname)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.get_stuff_section_list, parent, false)

        return sectionListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.bind(sectionList[position])
    }
}
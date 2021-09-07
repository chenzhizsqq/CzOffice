package com.xieyi.etoffice.ui.member

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.SectionInfo
import com.xieyi.etoffice.databinding.GetStuffSectionListBinding


class GetStuffSectionListAdapter(var list: ArrayList<SectionInfo>, val context: Context) :
    RecyclerView.Adapter<GetStuffSectionListAdapter.sectionListViewHolder>() {
    val TAG: String = javaClass.simpleName

    inner class sectionListViewHolder(binding: GetStuffSectionListBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        val recyclerView: RecyclerView = binding.recyclerViewStuffList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {

        val binding =
            GetStuffSectionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return sectionListViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.recyclerView.adapter = GetStuffStuffListAdapter(
            list[position].stufflist,
            list[position].sectioncd,
            list[position].sectionname, context
        )
    }

    fun notifyDataAdd(section: SectionInfo) {
        //Log.e(TAG, "notifyDataAdd: sectionList:$sectionList", )
        this.list.add(section)
        notifyDataSetChanged()
    }

    fun notifyDataAddList(sectionList: ArrayList<SectionInfo>) {
        this.list.addAll(sectionList)
        notifyDataSetChanged()
    }
}
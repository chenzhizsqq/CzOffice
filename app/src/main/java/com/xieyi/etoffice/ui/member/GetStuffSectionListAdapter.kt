package com.xieyi.etoffice.ui.member

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.SectionInfo
import com.xieyi.etoffice.databinding.GetStuffSectionListBinding


class GetStuffSectionListAdapter(var sectionList: ArrayList<SectionInfo>, val context: Context) :
    RecyclerView.Adapter<GetStuffSectionListAdapter.sectionListViewHolder>() {
    val TAG:String = javaClass.simpleName

    // Describes an item view and its place within the RecyclerView
    class sectionListViewHolder(binding: GetStuffSectionListBinding,val context: Context) : RecyclerView.ViewHolder(binding.root) {

        val recyclerView: RecyclerView = binding.recyclerViewStuffList


        fun bind(sectionList: SectionInfo) {
            recyclerView.adapter = GetStuffStuffListAdapter(
                sectionList.stufflist
                ,sectionList.sectioncd
                ,sectionList.sectionname
                ,context
            )

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {

        val binding =
            GetStuffSectionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return sectionListViewHolder(binding, context)
    }

    override fun getItemCount(): Int {
        return sectionList.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.bind(sectionList[position])
    }

    fun notifyDataAdd(section: SectionInfo) {
        //Log.e(TAG, "notifyDataAdd: sectionList:$sectionList", )
        this.sectionList.add(section)
        notifyDataSetChanged()
    }

    fun notifyDataAddList(sectionList: ArrayList<SectionInfo>) {
        this.sectionList.addAll(sectionList)
        notifyDataSetChanged()
    }
}
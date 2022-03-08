package com.xieyi.etoffice.ui.report

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.common.model.StuffStatusDispInfo


class ReportGetStuffSectionListAdapter(
    var list: ArrayList<StuffStatusDispInfo>,
    val context: Context
) :
    RecyclerView.Adapter<ReportGetStuffSectionListAdapter.SectionListViewHolder>() {
    val TAG: String = "GetStuffSectionListAdapter"

    inner class SectionListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_userkana: TextView? = view.findViewById(R.id.userkana)
        val tv_username: TextView? = view.findViewById(R.id.username)
        val sectionName: TextView? = view.findViewById(R.id.txtViewSectionName)
    }

    // レイアウトの設定
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionListViewHolder {

        // セクション名
        val mView: View = if (viewType == 1) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.report_section_header_sectionname, parent, false)
        }
        // 社員情報
        else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.report_get_section_stuff_list, parent, false)
        }

        return SectionListViewHolder(mView)
    }

    // ViewTypeを設定
    override fun getItemViewType(position: Int): Int {
        return if (list[position].stuffInfo == null) {
            // 部門名ヘッダ
            1
        } else {
            // 社員一覧
            2
        }
    }

    override fun onBindViewHolder(holder: SectionListViewHolder, position: Int) {
        // 部門名ヘッダ
        if (list[position].stuffInfo == null) {
            holder.sectionName?.text = list[position].sectionName
        }
        // 社員一覧
        else {
            holder.tv_userkana?.text = list[position].userStatusInfo?.userkana
            holder.tv_username?.text = list[position].userStatusInfo?.username
            holder.tv_username?.setOnClickListener {
                list[position].userStatusInfo?.username?.let {
                        it1 -> list[position].userStatusInfo?.userid?.let { it2 ->
                    listener.onClick(it1,it2)
                } }
            }

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyDataUpdateList(sectionList: ArrayList<StuffStatusDispInfo>) {
        this.list = sectionList
        notifyDataSetChanged()
    }

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener {
        fun onClick(userName: String,userid:String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }
}
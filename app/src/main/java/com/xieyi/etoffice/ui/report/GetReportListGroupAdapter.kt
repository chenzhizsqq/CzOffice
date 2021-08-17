package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.databinding.GetReportListBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetReportList


class GetReportListGroupAdapter(
    val getReportList: ArrayList<EtOfficeGetReportList.Group>,
) : RecyclerView.Adapter<GetReportListGroupAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener  {
        fun onClick(tenantid:String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    class ViewHolder(binding: GetReportListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tv_month: TextView = binding.month


        //bind
        fun bind(group: EtOfficeGetReportList.Group,listener:OnAdapterListener) {
            tv_month.text = group.month

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetReportListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return getReportList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getReportList[position],listener)
    }

    fun notifyDataAdd(group: EtOfficeGetReportList.Group) {
        //Log.e(TAG, "notifyDataAdd: sectionList:$sectionList", )
        this.getReportList.add(group)
        notifyDataSetChanged()
    }
}
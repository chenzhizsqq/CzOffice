package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.databinding.GetReportListGroupBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetReportList

//EtOfficeGetReportList.json result中的group
class GetReportListGroupAdapter(
    val getReportListGroup: ArrayList<EtOfficeGetReportList.Group>,
) : RecyclerView.Adapter<GetReportListGroupAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName

    private lateinit var binding: GetReportListGroupBinding
    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener  {
        fun onClick(tenantid:String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    class ViewHolder(binding: GetReportListGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        private val month: TextView = binding.month
        private val recyclerView:RecyclerView = binding.recyclerViewGetReportReportlist


        //bind
        fun bind(group: EtOfficeGetReportList.Group,listener:OnAdapterListener) {
            this.month.text = group.month
            recyclerView.adapter=GetReportListGroupReportlistAdapter(group.reportlist)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            GetReportListGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return getReportListGroup.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getReportListGroup[position],listener)
    }

    fun notifyDataAdd(group: EtOfficeGetReportList.Group) {
        //Log.e(TAG, "notifyDataAdd: sectionList:$sectionList", )
        this.getReportListGroup.add(group)
        notifyDataSetChanged()
    }
}
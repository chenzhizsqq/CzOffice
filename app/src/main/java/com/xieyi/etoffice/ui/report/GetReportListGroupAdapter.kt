package com.xieyi.etoffice.ui.report
import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.databinding.GetReportListGroupBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetReportList

//EtOfficeGetReportList.json result中的group
class GetReportListGroupAdapter(
    val getReportListGroup: ArrayList<EtOfficeGetReportList.Group>
    ,var arrayListYmd:ArrayList<String>
    ,val activity: Activity
    ,val viewModel: ReportViewModel
    ,val lifecycleOwner: LifecycleOwner

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
        fun bind(
            group: EtOfficeGetReportList.Group
            , arrayListYmd:ArrayList<String>
            , activity: Activity
            , viewModel: ReportViewModel
            , lifecycleOwner: LifecycleOwner
        ) {
            this.month.text = Tools.dateGetYear(group.month)+"."+Tools.dateGetMonth(group.month)
            recyclerView.adapter=GetReportListGroupReportlistAdapter(group.reportlist,arrayListYmd,activity,viewModel,lifecycleOwner)

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
        holder.bind(getReportListGroup[position],arrayListYmd,activity,viewModel
            ,lifecycleOwner)
    }
}
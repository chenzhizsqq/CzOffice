package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.databinding.GetReportListGroupReportlistBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetReportList

//EtOfficeGetReportList.json result-group-reportlist
class GetReportListGroupReportlistAdapter(
    val getReportListReportlist: ArrayList<EtOfficeGetReportList.Reportlist>,
) : RecyclerView.Adapter<GetReportListGroupReportlistAdapter.ViewHolder>() {
    val TAG: String = javaClass.simpleName

    private lateinit var binding: GetReportListGroupReportlistBinding

    class ViewHolder(binding: GetReportListGroupReportlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val yyyymmdd: TextView = binding.yyyymmdd
        private val itemid: TextView = binding.itemid
        private val holiday: TextView = binding.holiday
        private val title: TextView = binding.title
        private val approval: TextView = binding.approval
        private val content: TextView = binding.content
        private val warning: TextView = binding.warning


        //bind
        fun bind(reportlist: EtOfficeGetReportList.Reportlist) {
            this.yyyymmdd.text = reportlist.yyyymmdd
            this.itemid.text = reportlist.itemid
            this.holiday.text = reportlist.holiday
            this.title.text = reportlist.title
            this.approval.text = reportlist.approval
            this.content.text = reportlist.content
            this.warning.text = reportlist.warning

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            GetReportListGroupReportlistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return getReportListReportlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getReportListReportlist[position])
    }
}
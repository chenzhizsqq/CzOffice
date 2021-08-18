package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
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
        private val title: TextView = binding.title
        private val approval: TextView = binding.approval
        private val content: TextView = binding.content
        private val warning: TextView = binding.warning


        //bind
        fun bind(reportlist: EtOfficeGetReportList.Reportlist) {
            this.yyyymmdd.text  = reportlist.yyyymmdd
            this.approval.text  = reportlist.approval
            if (this.approval.text.isEmpty()){
                this.warning.text = "未承認"
                this.warning.setBackgroundResource(R.drawable.ic_round_edge_red)
            }else{
                this.warning.text = "承認済み"
                this.warning.setBackgroundResource(R.drawable.ic_round_edge_blue)
            }
            this.title.text     = reportlist.title
            this.content.text   = reportlist.content

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
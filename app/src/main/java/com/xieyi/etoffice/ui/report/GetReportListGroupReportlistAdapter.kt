package com.xieyi.etoffice.ui.report
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Config
import com.xieyi.etoffice.EtOfficeApp.Companion.context
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.ReportListnfo
import com.xieyi.etoffice.databinding.GetReportListGroupReportlistBinding

//EtOfficeGetReportList.json result-group-reportlist
class GetReportListGroupReportlistAdapter(
    val list: List<ReportListnfo>
    ,var arrayListYmd:ArrayList<String>
    ,val activity: Activity
    ,val viewModel: ReportViewModel
    ,val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<GetReportListGroupReportlistAdapter.ViewHolder>() {
    val TAG: String = javaClass.simpleName

    private lateinit var binding: GetReportListGroupReportlistBinding

    class ViewHolder(binding: GetReportListGroupReportlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val yyyymmdd: TextView = binding.yyyymmdd
        val title: TextView = binding.title
        val approval: TextView = binding.approval
        val content: TextView = binding.content
        val warning: TextView = binding.warning
        val checkbox: CheckBox = binding.checkbox
        val ll: LinearLayout = binding.ll


        //bind
        fun bind(
            info: ReportListnfo
            , activity: Activity
            , viewModel: ReportViewModel
            , lifecycleOwner: LifecycleOwner
        ) {
            this.yyyymmdd.text  = Tools.allDate(info.yyyymmdd)
            this.approval.text  = info.approval
            if (this.approval.text.isEmpty()){
                this.warning.text = context.getString(R.string.unacknowledged)
                this.warning.setBackgroundResource(R.drawable.ic_round_edge_red)
            }else{
                this.warning.text = context.getString(R.string.Approved)
                this.warning.setBackgroundResource(R.drawable.ic_round_edge_blue)
            }
            this.title.text     = info.title
            this.content.text   = info.content

            //checkbox
            this.checkbox.tag = info.yyyymmdd


            ll.setOnClickListener(View.OnClickListener {
                if(viewModel.visibility.value == View.GONE) {
                    Tools.sharedPrePut(Config.FragKey,3)
                    //EtOfficeApp.selectUi = 3
                    val intent = Intent(activity, ReportDetailActivity::class.java)
                    intent.putExtra("ReportFragmentMessage", info.yyyymmdd)
                    activity.startActivity(intent)
                    activity.finish()
                }

            })

            viewModel.allSelect.observe(lifecycleOwner,{
                this.checkbox.isChecked = it
            })

            viewModel.visibility.observe(lifecycleOwner,{
                this.checkbox.visibility = it
            })
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
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            list[position]
            , activity
            , viewModel
        ,lifecycleOwner)


        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if(!arrayListYmd.contains(list[position].yyyymmdd)){
                    arrayListYmd.add(list[position].yyyymmdd)
                }
            } else {
                if(arrayListYmd.contains(list[position].yyyymmdd)) {
                    arrayListYmd.remove(list[position].yyyymmdd)
                }
            }
        }
    }
}
package com.xieyi.etoffice.ui.report

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.EtOfficeApp.Companion.context
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.ReportListnfo
import com.xieyi.etoffice.databinding.GetReportListGroupReportlistBinding
class GetReportListGroupReportlistAdapter : RecyclerView.Adapter<GetReportListGroupReportlistAdapter.ViewHolder>() {
    val TAG: String = "GetReportListGroupReportlistAdapter"


    var list = listOf<ReportListnfo>()
    private var arrayListYmd= ArrayList<String>()
    lateinit var activity: Activity
    lateinit var viewModel: ReportViewModel
    lateinit var lifecycleOwner: LifecycleOwner

    private lateinit var binding: GetReportListGroupReportlistBinding


    fun notifyDataSetChanged(
        list: List<ReportListnfo>,
        arrayListYmd: ArrayList<String>,
        activity: Activity,
        viewModel: ReportViewModel,
        lifecycleOwner: LifecycleOwner
    ) {
        Log.e(TAG, "notifyDataSetChanged: begin", )
        this.list = listOf()
        this.list = list
        Log.e(TAG, "notifyDataSetChanged: list:"+this.list.toString() )

        this.arrayListYmd.clear()
        this.arrayListYmd = arrayListYmd
        Log.e(TAG, "notifyDataSetChanged: arrayListYmd:"+this.arrayListYmd.toString() )

        this.activity = activity
        this.viewModel = viewModel
        this.lifecycleOwner = lifecycleOwner
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: GetReportListGroupReportlistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val yyyymmdd: TextView = binding.yyyymmdd
        val title: TextView = binding.title
        val approval: TextView = binding.approval
        val content: TextView = binding.content
        val warning: TextView = binding.warning
        val checkbox: CheckBox = binding.checkbox
        val ll: LinearLayout = binding.ll
        val checkbox_ll: LinearLayout = binding.checkboxLl
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

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener {
        fun onClick(yyyymmdd: String, isApproved: Boolean)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.e(TAG, "!!! onBindViewHolder: list:"+this.list.toString() )
        holder.yyyymmdd.text = Tools.allDate(list[position].yyyymmdd)
        holder.approval.text = list[position].approval
        if (holder.approval.text.isEmpty()) {
            holder.warning.text = context.getString(R.string.unacknowledged)
            holder.warning.setBackgroundResource(R.drawable.ic_round_edge_red)
        } else {
            holder.warning.text = context.getString(R.string.Approved)
            holder.warning.setBackgroundResource(R.drawable.ic_round_edge_blue)
        }
        holder.title.text = list[position].title
        holder.content.text = list[position].content

        //checkbox
        holder.checkbox.tag = list[position].yyyymmdd


        holder.ll.setOnClickListener(View.OnClickListener {
            if (viewModel.visibility.value == View.GONE) {
                if (holder.approval.text.isEmpty()) {
                    listener.onClick(list[position].yyyymmdd, false)
                } else {
                    listener.onClick(list[position].yyyymmdd, true)
                }
            } else if (viewModel.visibility.value == View.VISIBLE) {
                holder.checkbox.isChecked = !holder.checkbox.isChecked
            }

        })

        viewModel.allSelect.observe(lifecycleOwner, {
            holder.checkbox.isChecked = it
        })

        viewModel.visibility.observe(lifecycleOwner, { it ->
            //holder.checkbox.visibility = it
            if (it == View.VISIBLE) {
                holder.checkbox_ll.minimumWidth = 120
                holder.checkbox.visibility = View.VISIBLE
                if (holder.approval.text.isEmpty()) {
                    holder.checkbox.visibility = View.VISIBLE
                } else {
                    holder.checkbox.visibility = View.GONE
                }

            } else {
                holder.checkbox_ll.minimumWidth = 0
                holder.checkbox.visibility = View.GONE
            }
        })

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (!arrayListYmd.contains(list[position].yyyymmdd)) {
                    arrayListYmd.add(list[position].yyyymmdd)
                }
            } else {
                if (arrayListYmd.contains(list[position].yyyymmdd)) {
                    arrayListYmd.remove(list[position].yyyymmdd)
                }
            }
        }
    }
}
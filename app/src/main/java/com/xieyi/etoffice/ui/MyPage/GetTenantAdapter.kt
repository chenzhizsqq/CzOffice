package com.xieyi.etoffice.ui.MyPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.TenantInfo
import com.xieyi.etoffice.databinding.GetTenantListBinding


class GetTenantAdapter : RecyclerView.Adapter<GetTenantAdapter.ViewHolder>() {
    val TAG: String = "GetTenantAdapter"

    lateinit var list: List<TenantInfo>

    fun notifyDataSetChanged(list: List<TenantInfo>) {
        this.list = list
        notifyDataSetChanged()
    }

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener {
        fun onClick(tenantid: String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    inner class ViewHolder(binding: GetTenantListBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_posturl: TextView = binding.posturl
        val tv_tenantname: TextView = binding.tenantname
        val iv_clicked: ImageView = binding.clicked
        val ll: LinearLayout = binding.ll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetTenantListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tv_posturl.text = list[position].posturl
        holder.tv_tenantname.text = list[position].tenantname
        if (list[position].startflg == "1") {
            holder.iv_clicked.visibility = View.VISIBLE
        } else {
            holder.iv_clicked.visibility = View.GONE
        }

        holder.ll.setOnClickListener(View.OnClickListener {
            listener.onClick(list[position].tenantid)
        })


    }
}
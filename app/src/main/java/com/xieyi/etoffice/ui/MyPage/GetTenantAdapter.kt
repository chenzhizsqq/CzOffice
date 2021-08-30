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


class GetTenantAdapter(
    val getGetTenant: List<TenantInfo>,
) : RecyclerView.Adapter<GetTenantAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener  {
        fun onClick(tenantid:String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    class ViewHolder(binding: GetTenantListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tv_posturl: TextView = binding.posturl
        private val tv_tenantname: TextView = binding.tenantname
        private val iv_clicked: ImageView = binding.clicked


        val ll: LinearLayout = binding.ll

        //bind
        fun bind(tenantInfo: TenantInfo, listener:OnAdapterListener) {
            tv_posturl.text = tenantInfo.posturl
            tv_tenantname.text = tenantInfo.tenantname
            if (tenantInfo.startflg == "1"){
                iv_clicked.visibility = View.VISIBLE
            }else{
                iv_clicked.visibility = View.GONE
            }

            ll.setOnClickListener(View.OnClickListener {
                listener.onClick(tenantInfo.tenantid)
            })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetTenantListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return getGetTenant.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getGetTenant[position],listener)
    }
}
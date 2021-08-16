package com.xieyi.etoffice.ui.MyPage
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.EtOfficeGetTenant
import com.xieyi.etoffice.jsonData.EtOfficeSetTenant
import kotlinx.coroutines.*


class GetTenantAdapter(
    val getGetTenant: List<EtOfficeGetTenant.Tenantlist>,
) : RecyclerView.Adapter<GetTenantAdapter.sectionListViewHolder>() {
    val TAG:String = javaClass.simpleName

    private lateinit var pEtOfficeSetTenant : EtOfficeSetTenant

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener  {
        fun onClick(tenantid:String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }

    class sectionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_posturl: TextView = itemView.findViewById(R.id.posturl)
        private val tv_tenantname: TextView = itemView.findViewById(R.id.tenantname)
        private val iv_clicked: ImageView = itemView.findViewById(R.id.clicked)

        val ll: LinearLayout = itemView.findViewById(R.id.ll)

        //telephone
        fun bind(stufflist: EtOfficeGetTenant.Tenantlist,listener:OnAdapterListener) {
            tv_posturl.text = stufflist.posturl
            tv_tenantname.text = stufflist.tenantname
            if (stufflist.startflg == "1"){
                iv_clicked.visibility = View.VISIBLE
            }else{
                iv_clicked.visibility = View.GONE
            }

            ll.setOnClickListener(View.OnClickListener {
                listener.onClick(stufflist.tenantid)
            })

        }
    }

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        // 发生异常时的捕获
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.get_tenant_list, parent, false)


        pEtOfficeSetTenant = EtOfficeSetTenant()
        
        return sectionListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return getGetTenant.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.bind(getGetTenant[position],listener)
    }
}
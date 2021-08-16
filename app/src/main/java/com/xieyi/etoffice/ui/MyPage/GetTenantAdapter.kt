package com.xieyi.etoffice.ui.MyPage
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.jsonData.EtOfficeGetTenant
import com.xieyi.etoffice.jsonData.EtOfficeSetTenant
import kotlinx.coroutines.*


class GetTenantAdapter(
    val getGetTenant: List<EtOfficeGetTenant.Tenantlist>,
    val context: Context,
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
        private val tv_userkana: TextView = itemView.findViewById(R.id.userkana)
        private val tv_username: TextView = itemView.findViewById(R.id.username)
        private val tv_phone: TextView = itemView.findViewById(R.id.phone)
        private val tv_mail: TextView = itemView.findViewById(R.id.mail)


        val ll: LinearLayout = itemView.findViewById(R.id.ll)


        //telephone
        fun bind(stufflist: EtOfficeGetTenant.Tenantlist,context: Context,listener:OnAdapterListener) {
            tv_userkana.text = stufflist.hpid
            tv_username.text = stufflist.posturl
            tv_phone.text = stufflist.startflg
            tv_mail.text = stufflist.tenantname

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
        holder.bind(getGetTenant[position],context,listener)
    }
}
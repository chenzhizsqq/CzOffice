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


class GetTenantAdapter(val getGetTenant: List<EtOfficeGetTenant.Tenantlist>, val context: Context
) :
    RecyclerView.Adapter<GetTenantAdapter.sectionListViewHolder>() {
    val TAG:String = javaClass.simpleName


    class sectionListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tv_userkana: TextView = itemView.findViewById(R.id.userkana)
        private val tv_username: TextView = itemView.findViewById(R.id.username)
        private val tv_phone: TextView = itemView.findViewById(R.id.phone)
        private val tv_mail: TextView = itemView.findViewById(R.id.mail)


        private val ll: LinearLayout = itemView.findViewById(R.id.ll)

        //telephone
        fun bind(stufflist: EtOfficeGetTenant.Tenantlist,context: Context) {
            tv_userkana.text = stufflist.hpid
            tv_username.text = stufflist.posturl
            tv_phone.text = stufflist.startflg
            tv_mail.text = stufflist.tenantname

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.get_tenant_list, parent, false)

        
        return sectionListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return getGetTenant.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.bind(getGetTenant[position],context)
    }
}
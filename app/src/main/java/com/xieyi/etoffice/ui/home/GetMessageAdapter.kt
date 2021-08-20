package com.xieyi.etoffice.ui.home
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.databinding.GetMessageListBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetMessage
import com.xieyi.etoffice.jsonData.EtOfficeSetTenant


class GetMessageAdapter(
    val messagelist: List<EtOfficeGetMessage.Messagelist>
) : RecyclerView.Adapter<GetMessageAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName

    private lateinit var pEtOfficeSetTenant : EtOfficeSetTenant


    class ViewHolder(binding: GetMessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tv_title: TextView = binding.title
        private val tv_updatetime: TextView = binding.updatetime
        private val tv_content: TextView = binding.content


        val ll: LinearLayout = binding.ll

        //bind
        fun bind(messagelist: EtOfficeGetMessage.Messagelist) {
            tv_title.text = messagelist.title
            tv_updatetime.text = messagelist.updatetime
            tv_content.text = messagelist.content

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetMessageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        pEtOfficeSetTenant = EtOfficeSetTenant()
        return viewHolder
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(messagelist[position])
    }
}
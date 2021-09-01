package com.xieyi.etoffice.ui.home
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.MessageInfo
import com.xieyi.etoffice.databinding.GetMessageListBinding


class GetMessageAdapter(
    val list: List<MessageInfo>
) : RecyclerView.Adapter<GetMessageAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName


    class ViewHolder(binding: GetMessageListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tv_title: TextView = binding.title
        private val tv_updatetime: TextView = binding.updatetime
        private val tv_content: TextView = binding.content


        val ll: LinearLayout = binding.ll

        //bind
        fun bind(info: MessageInfo) {
            tv_title.text = info.title
            tv_updatetime.text =Tools.allDateTime(info.updatetime)
            tv_content.text = info.content

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetMessageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}
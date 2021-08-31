package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.PlanWorkInfo
import com.xieyi.etoffice.databinding.GetPlanWorkInfoBinding


class PlanworklistAdapter(
    val list:  List<PlanWorkInfo>,
) : RecyclerView.Adapter<PlanworklistAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName


    class ViewHolder(binding: GetPlanWorkInfoBinding) : RecyclerView.ViewHolder(binding.root) {

//        var project: String,    // 項目名
//        var wbs: String,        // 作業名
//        var date: String,       // 時間帯
//        var time: String,       // 計画時間
         val project: TextView = binding.project
        val wbs: TextView = binding.wbs
        val date: TextView = binding.date
        val time: TextView = binding.time



        //bind
        fun bind(info: PlanWorkInfo) {
            project.text = info.project
            wbs.text = info.wbs
            date.text = info.date
            time.text = info.time

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetPlanWorkInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
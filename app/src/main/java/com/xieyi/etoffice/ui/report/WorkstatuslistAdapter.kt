package com.xieyi.etoffice.ui.report
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.WorkStatusInfo
import com.xieyi.etoffice.databinding.GetPlanworkListWorkStatusInfoBinding


class WorkstatuslistAdapter(
    val getUserLocation:  List<WorkStatusInfo>,
) : RecyclerView.Adapter<WorkstatuslistAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName


    class ViewHolder(binding: GetPlanworkListWorkStatusInfoBinding) : RecyclerView.ViewHolder(binding.root) {
         val location: TextView = binding.location
         val time: TextView = binding.time



        //bind
        fun bind(locationList: WorkStatusInfo) {
            location.text = locationList.status
            time.text = locationList.time

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetPlanworkListWorkStatusInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return getUserLocation.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getUserLocation[position])
    }
}
package com.xieyi.etoffice.ui.MyPage
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.databinding.GetUserLocationBinding
import com.xieyi.etoffice.jsonData.EtOfficeGetUserLocation


class GetUserLocationAdapter(
    val getUserLocation: List<EtOfficeGetUserLocation.Locationlist>,
) : RecyclerView.Adapter<GetUserLocationAdapter.ViewHolder>() {
    val TAG:String = javaClass.simpleName

    private lateinit var pEtOfficeGetUserLocation : EtOfficeGetUserLocation


    class ViewHolder(binding: GetUserLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        private val latitude: TextView = binding.latitude
        private val longitude: TextView = binding.longitude
        private val location: TextView = binding.location


        val ll: LinearLayout = binding.ll

        //bind
        fun bind(locationList: EtOfficeGetUserLocation.Locationlist) {
            latitude.text = Tools.srcContent(locationList.latitude,8,"")
            longitude.text = Tools.srcContent(locationList.longitude,8,"")
            location.text = locationList.location


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GetUserLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        pEtOfficeGetUserLocation = EtOfficeGetUserLocation()
        return viewHolder
    }

    override fun getItemCount(): Int {
        return getUserLocation.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getUserLocation[position])
    }
}
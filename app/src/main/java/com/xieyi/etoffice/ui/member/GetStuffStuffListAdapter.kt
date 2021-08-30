package com.xieyi.etoffice.ui.member

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.common.model.StuffInfo
import com.xieyi.etoffice.databinding.GetStuffStuffListBinding


class GetStuffStuffListAdapter(val getStuffList: List<StuffInfo>
, val sectioncd:String, val sectionname:String,val context: Context
) :
    RecyclerView.Adapter<GetStuffStuffListAdapter.sectionListViewHolder>() {
    val TAG:String = javaClass.simpleName


    class sectionListViewHolder(binding: GetStuffStuffListBinding) : RecyclerView.ViewHolder(binding.root) {
        val tv_userkana: TextView = binding.userkana
        val tv_username: TextView = binding.username
        val tv_phone: TextView = binding.phone
        val tv_mail: TextView = binding.mail

        val tv_sectioncd: TextView = binding.sectioncd
        val tv_sectionname: TextView = binding.sectionname

        val ll: LinearLayout = binding.ll

        //telephone
        fun bind(stufflist: StuffInfo, sectioncd:String, sectionname:String,context: Context) {
            tv_userkana.text = stufflist.userkana
            tv_username.text = stufflist.username
            tv_phone.text = stufflist.phone
            tv_mail.text = stufflist.mail
            tv_sectioncd.text = sectioncd
            tv_sectionname.text = sectionname

            val REQUEST_CALL_PERMISSION = 10111 //電話　申し込む
            ll.setOnClickListener(View.OnClickListener {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CALL_PHONE
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    // CALL_PHONE 権利　ない
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf<String>(Manifest.permission.CALL_PHONE),
                        REQUEST_CALL_PERMISSION
                    )
                } else {
                    //CALL_PHONE 権利　ある


                    AlertDialog.Builder(context)
                        .setTitle("電話番号")
                        .setMessage(stufflist.phone)
                        .setPositiveButton("call") { _, _ ->

                            val uri: Uri = Uri.parse("tel:"+stufflist.phone)
                            val intent = Intent(Intent.ACTION_CALL, uri)
                            it.context.startActivity(intent)

                        }
                        .setNegativeButton("Cancel") { _, which ->
                        }
                        .show()
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {

        val binding = GetStuffStuffListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        
        return sectionListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return getStuffList.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.bind(getStuffList[position],sectioncd,sectionname,context)
    }
}
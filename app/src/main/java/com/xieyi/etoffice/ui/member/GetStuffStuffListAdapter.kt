package com.xieyi.etoffice.ui.member

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.StuffInfo
import com.xieyi.etoffice.databinding.GetStuffStuffListBinding


class GetStuffStuffListAdapter(
    val list: List<StuffInfo>, val sectioncd: String, val sectionname: String, val context: Context
) :
    RecyclerView.Adapter<GetStuffStuffListAdapter.sectionListViewHolder>() {
    val TAG: String = "GetStuffStuffListAdapter"


    inner class sectionListViewHolder(binding: GetStuffStuffListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_userkana: TextView = binding.userkana
        val tv_username: TextView = binding.username
        val tv_phone: TextView = binding.phone
        val tv_mail: TextView = binding.mail
        val tv_sectioncd: TextView = binding.sectioncd
        val tv_sectionname: TextView = binding.sectionname
        val ll: LinearLayout = binding.ll
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): sectionListViewHolder {

        val binding =
            GetStuffStuffListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return sectionListViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: sectionListViewHolder, position: Int) {
        holder.tv_userkana.text = list[position].userkana
        holder.tv_username.text = list[position].username
        holder.tv_phone.text = list[position].phone
        holder.tv_mail.text = list[position].mail
        holder.tv_sectioncd.text = sectioncd
        holder.tv_sectionname.text = sectionname

        holder.ll.setOnClickListener {
            //确定是否有电话号码
            if (list[position].phone == "") {
                Tools.showMsg(holder.ll, context.getString(R.string.no_telephone_number))
            } else {
                val activity = context as FragmentActivity
                val fm: FragmentManager = activity.supportFragmentManager
                val mMemberTelDialog = MemberTelDialog(list[position].phone)
                fm.let { it1 -> mMemberTelDialog.show(it1, "mMemberTelDialog") }
            }
        }

    }
}
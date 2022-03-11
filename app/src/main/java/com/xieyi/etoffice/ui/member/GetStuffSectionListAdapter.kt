package com.xieyi.etoffice.ui.member

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.common.model.StuffStatusDispInfo


class GetStuffSectionListAdapter(var list: ArrayList<StuffStatusDispInfo>, val context: Context) :
    RecyclerView.Adapter<GetStuffSectionListAdapter.SectionListViewHolder>() {
    val TAG: String = "GetStuffSectionListAdapter"

    inner class SectionListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_userkana: TextView? = view.findViewById(R.id.userkana)
        val tv_username: TextView? = view.findViewById(R.id.username)
        val tv_phone: TextView? = view.findViewById(R.id.phone)
        val tv_mail: TextView? = view.findViewById(R.id.mail)
        val tv_status: TextView? = view.findViewById(R.id.status)
        val tv_status_icon: TextView? = view.findViewById(R.id.status_icon)
        val tv_place: TextView? = view.findViewById(R.id.place)
        val ll: LinearLayout? = view.findViewById(R.id.ll)
        val sectionName: TextView? = view.findViewById(R.id.txtViewSectionName)
    }

    // レイアウトの設定
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionListViewHolder {

        // セクション名
        val mView: View = if (viewType == 1) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.section_header_sectionname, parent, false)
        }
        // 社員情報
        else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.get_section_stuff_list, parent, false)
        }

        return SectionListViewHolder(mView)
    }

    // ViewTypeを設定
    override fun getItemViewType(position: Int): Int {
        return if (list[position].stuffInfo == null) {
            // 部門名ヘッダ
            1
        } else {
            // 社員一覧
            2
        }
    }

    override fun onBindViewHolder(holder: SectionListViewHolder, position: Int) {
        // 部門名ヘッダ
        if (list[position].stuffInfo == null) {
            holder.sectionName?.text = list[position].sectionName
        }
        // 社員一覧
        else {
            holder.tv_userkana?.text = list[position].userStatusInfo?.userkana
            holder.tv_username?.text = list[position].userStatusInfo?.username
            holder.tv_phone?.text = list[position].stuffInfo?.phone
            holder.tv_mail?.text = list[position].stuffInfo?.mail

            holder.tv_status_icon?.text = "●"

            var status = list[position].userStatusInfo?.statustext
            val memoText = list[position].userStatusInfo?.memo

            if (status == null || status.length == 0) {
                status = context.getString(R.string.MSG10)
            }
            holder.tv_status?.text = status + " " + (memoText ?: "")

            //メンバー一览出勤状态按照出勤状态的不同圆点设置的颜色不一样。
            when (status) {
                //绿色：出勤
                "勤務中" -> {
                    holder.tv_status_icon?.setTextColor(Color.GREEN)
                }

                //棕色：不明状态
                "不明な状態" -> {
                    holder.tv_status_icon?.setTextColor(Color.parseColor("#E14B00"))
                }

                //灰色：休息/勤务外
                "休憩中", "勤務外" -> {
                    holder.tv_status_icon?.setTextColor(Color.GRAY)
                }

                //蓝色：会议/移动中
                "会議中", "移動中" -> {
                    holder.tv_status_icon?.setTextColor(Color.BLUE)
                }
            }

            var location = list[position].userStatusInfo?.location
            if (location == null || location.length == 0) {
                location = context.getString(R.string.MSG07)
            }
            holder.tv_place?.text = location
            holder.ll?.setOnClickListener {
                //确定是否有电话号码
//                if (list[position].stuffInfo?.phone == "") {
//                    Tools.showErrorDialog(context, context.getString(R.string.no_telephone_number))
//                } else {
//                    val activity = context as FragmentActivity
//                    val fm: FragmentManager = activity.supportFragmentManager
//                    val mMemberTelDialog = MemberTelDialog(list[position].stuffInfo!!.phone)
//                    fm.let { it1 -> mMemberTelDialog.show(it1, "mMemberTelDialog") }
//                }

                list[position].stuffInfo?.phone?.let { it1 -> listener.onClick(it1) }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyDataUpdateList(sectionList: ArrayList<StuffStatusDispInfo>) {
        this.list = sectionList
        notifyDataSetChanged()
    }

    private lateinit var listener: OnAdapterListener

    interface OnAdapterListener {
        fun onClick(phoneNumber: String)
    }

    fun setOnAdapterListener(adapterListener: OnAdapterListener) {
        this.listener = adapterListener
    }
}
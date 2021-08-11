package com.xieyi.etoffice.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.databinding.NotificationsItemBinding
import com.xieyi.etoffice.widget.CustomCheckBox
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class NotificationsAdapter(private var messagelist:List<Message>):
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>(){
    //用来记录所有checkbox的状态
    private var checkStatus = hashMapOf<Int, String>()
    var isEdit = false

    interface OnItemClickListener{
        fun onItemClick(position:Int)
    }

    private lateinit var onItemClickListener: OnItemClickListener
    fun setOnItemClickListener(onItemClickListener:OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }
    inner class ViewHolder(binding : NotificationsItemBinding):RecyclerView.ViewHolder(binding.root){
        val msgTitle:TextView = binding.msgTitle
        val msgUpdateTime:TextView = binding.msgUpdateTime
        val msgContent:TextView = binding.msgContent
        val checkBox:CustomCheckBox = binding.checkBox
    }

    //更新适配器数据
    fun notifyDataChange(list: List<Message>, checkStatus: HashMap<Int, String>) {
        this.messagelist = list
        this.checkStatus = checkStatus
        notifyDataSetChanged()
    }

    //更新适配器数据
    fun notifyDataChange(list: List<Message>) {
        this.messagelist = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)
        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            if (onItemClickListener != null) {
                val position = viewHolder.adapterPosition
                onItemClickListener.onItemClick(position)
            }
        })
    return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messagelist[position]
        holder.msgTitle.text = message.title
        holder.msgUpdateTime.text = strToDateLong(message.updatetime)
        holder.msgContent.text = message.content
        holder.itemView.tag = message.updatetime
        if (isEdit) {
            holder.checkBox.visibility = View.VISIBLE
            holder.checkBox.setOnCheckChangeListener(null)
            holder.checkBox.isCheck = !checkStatus[position].isNullOrEmpty()

            //再设置一次CheckBox的选中监听器，当CheckBox的选中状态发生改变时，把改变后的状态储存在Map中
            holder.checkBox.setOnCheckChangeListener(object : CustomCheckBox.OnCheckChangeListener{
                override fun onCheckChange(isChecked:Boolean) {
                    // checkStatus[position] = isChecked
                    if (isChecked) {
                        checkStatus[position] = message.updatetime + message.subid

                    } else {
                        checkStatus[position] = ""

                    }
                }
            })
        } else {
            holder.checkBox.visibility = View.GONE
        }
    }

    override fun getItemCount() = messagelist.size

    // 全选
    fun selectAll() {
        initCheck(true)
        notifyDataSetChanged()
    }
    // 全不选
    fun unSelectAll() {
        initCheck(false)
        notifyDataSetChanged()
    }

    fun getCheckStatus(): HashMap<Int, String> {
        return checkStatus
    }

    //更改集合内部存储的状态
    fun initCheck(flag: Boolean) {
        for (i in messagelist.indices) {
            //更改指定位置的数据
            if (flag){
                checkStatus[i] = messagelist[i].updatetime + messagelist[i].subid
            } else {
                checkStatus[i] = ""
            }
        }
    }

    /**
     * 时间日期转换
     * @param strDate 字符串yyyyMMddHHmmss
     * @return 字符串yyyy-MM-dd HH:mm:ss
     */
    fun strToDateLong(strDate: String?): String? {
        var date: Date? = Date()
        try {
            //先按照原格式转换为时间
            date = SimpleDateFormat("yyyyMMddHHmmss").parse(strDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        //再将时间转换为对应格式字符串
        return SimpleDateFormat("yyyy.MM.dd hh:mm:ss").format(date)
    }

}
package com.xieyi.etoffice.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.R
import com.xieyi.etoffice.databinding.NotificationsItemBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class NotificationsAdapter(private var messagelist:List<Message>):
    RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {
    inner class ViewHolder(binding : NotificationsItemBinding):RecyclerView.ViewHolder(binding.root){
        val msgTitle:TextView = binding.msgTitle
        val msgUpdateTime:TextView = binding.msgUpdateTime
        val msgContent:TextView = binding.msgContent
    }

    fun notifyDataChange(list: List<Message>) {       //更新适配器数据
        this.messagelist = list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NotificationsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messagelist[position]
        holder.msgTitle.text = message.title
        holder.msgUpdateTime.text = strToDateLong(message.updatetime)
        holder.msgContent.text = message.content
    }

    override fun getItemCount() = messagelist.size

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
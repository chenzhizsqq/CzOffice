package com.xieyi.etoffice.ui.report

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xieyi.etoffice.Tools
import com.xieyi.etoffice.common.model.CommentInfo
import com.xieyi.etoffice.databinding.GetCommentInfoBinding


class CommentListAdapter(
    val list: List<CommentInfo>,
) : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {
    val TAG: String = "CommentListAdapter"


    inner class ViewHolder(binding: GetCommentInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        val username: TextView = binding.username
        val comment: TextView = binding.comment
        val time: TextView = binding.time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            GetCommentInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.username.text = list[position].username
        holder.comment.text = list[position].comment
        holder.time.text = Tools.allDateTime(list[position].time)
    }
}
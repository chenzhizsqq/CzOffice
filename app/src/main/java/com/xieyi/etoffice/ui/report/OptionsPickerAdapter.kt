package com.xieyi.etoffice.ui.report

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.xieyi.etoffice.R

class OptionsPickerAdapter(context: Context, resourceId: Int, data: List<OptionItemModel>) :
    ArrayAdapter<OptionItemModel>(context, resourceId, data) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder: ViewHolder?
        var view: View

        if (convertView == null) {
            view = View.inflate(context, R.layout.option_item, null)
            viewHolder = ViewHolder()
            viewHolder.code = view.findViewById(R.id.option_cd)
            viewHolder.name = view.findViewById(R.id.option_name)

            view.tag = viewHolder

        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        val option = getItem(position)
        if (option != null) {
            viewHolder.name?.text = option.name
            viewHolder.code?.text = option.code
        }
        return view
    }

    inner class ViewHolder {
        var code: TextView? = null
        var name: TextView? = null
    }

}
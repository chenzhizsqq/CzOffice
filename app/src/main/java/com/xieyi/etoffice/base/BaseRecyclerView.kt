package com.xieyi.etoffice.base

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * ベースリサイタル
 * データ存在の確認
 */
open class BaseRecyclerView : RecyclerView {
    //データがない場合の表示画面
    private var emptyView: View? = null

    /**
     * データがない場合の表示画面を設定
     * @param emptyView:    画面
     */
    fun setEmptyView(emptyView: View?) {
        this.emptyView = emptyView
    }

    private val emptyObserver: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() {
            val adapter = adapter
            if (adapter != null && emptyView != null) {
                if (adapter.itemCount == 0) {
                    emptyView!!.visibility = VISIBLE
                    this@BaseRecyclerView.visibility = GONE
                } else {
                    emptyView!!.visibility = GONE
                    this@BaseRecyclerView.visibility = VISIBLE
                }
            }
        }
    }

    constructor(context: Context?) : super(context!!) {

    }
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!, attrs, defStyle
    ) {
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(emptyObserver)
        emptyObserver.onChanged()
    }
}

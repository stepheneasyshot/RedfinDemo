package com.stephen.redfindemo.feature.deviceinfo

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.stephen.redfindemo.R

class CommonKeyValueAdapter(private var infoList: List<Pair<String, String>>) :
    BaseQuickAdapter<Pair<String, String>, QuickViewHolder>() {

    override var items = infoList

    @SuppressLint("NotifyDataSetChanged")
    fun updateDetailList(newList: List<Pair<String, String>>) {
        items = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: QuickViewHolder,
        position: Int,
        item: Pair<String, String>?
    ) {
        holder.getView<TextView>(R.id.tv_infoname).text = item?.first
        holder.getView<TextView>(R.id.tv_infovalue).text = item?.second
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ) = QuickViewHolder(R.layout.item_namevalue, parent)

}




package com.stephen.redfindemo.feature.debug

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.stephen.redfindemo.R
import com.stephen.redfindemo.data.bean.ProcessModel

class PerformanceAdapter(val processList: List<ProcessModel>) :
    BaseQuickAdapter<ProcessModel, QuickViewHolder>() {

    override var items = processList

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: ProcessModel?) {
        holder.getView<TextView>(R.id.tv_pid).text = items[position].pid
        holder.getView<TextView>(R.id.tv_packagename).text = items[position].packageName
        holder.getView<TextView>(R.id.tv_cpuinfo).text = items[position].cpuinfo
        holder.getView<TextView>(R.id.tv_meminfo).text = items[position].meminfo
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ) = QuickViewHolder(R.layout.item_processinfo, parent)
}
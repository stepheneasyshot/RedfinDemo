package com.stephen.redfindemo.feature.main

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.stephen.redfindemo.R
import com.stephen.redfindemo.base.appContext

class MainAdapter(private var tabList: List<String>) : BaseQuickAdapter<String, QuickViewHolder>() {

    override var items = tabList

    private var choosedPosition = 0

    fun setChoosedPosition(position: Int) {
        choosedPosition = position
    }

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: String?) {
        holder.getView<TextView>(R.id.tv_mainitem).background =
            if (position == choosedPosition)
                ResourcesCompat.getDrawable(appContext.resources, R.drawable.bg_button, null)
            else null
        holder.getView<TextView>(R.id.tv_mainitem).text = items[position]
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ) = QuickViewHolder(R.layout.item_maintab, parent)
}
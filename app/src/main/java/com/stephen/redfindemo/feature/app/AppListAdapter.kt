package com.stephen.redfindemo.feature.app

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.QuickViewHolder
import com.stephen.redfindemo.R
import com.stephen.redfindemo.base.appContext
import com.stephen.redfindemo.data.bean.AppInfoModel

class AppListAdapter(applist: List<AppInfoModel>) :
    BaseQuickAdapter<AppInfoModel, QuickViewHolder>() {

    override var items = applist

    private var choosedPosition = 0

    fun setChoosedPosition(position: Int) {
        choosedPosition = position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDetailList(newList: List<AppInfoModel>) {
        items = newList
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: AppInfoModel?) {
        // 点击态
        holder.getView<ConstraintLayout>(R.id.csl_bg_appinfoitem).background =
            if (position == choosedPosition)
                ResourcesCompat.getDrawable(appContext.resources, R.drawable.bg_item_appinfo, null)
            else null
        // 图标，应用名等
        holder.getView<ImageView>(R.id.iv_appicon).setImageDrawable(items[position].appIcon)
        holder.getView<TextView>(R.id.tv_labelandversion).text = items[position].appLabel
        holder.getView<TextView>(R.id.tv_appversion).text =
            appContext.getString(R.string.app_version, items[position].appVersion)
        holder.getView<TextView>(R.id.tv_issystem).visibility =
            if (AppManager.isSystemApp(item?.packageName ?: "null")) View.VISIBLE else View.GONE
    }

    override fun onCreateViewHolder(
        context: Context,
        parent: ViewGroup,
        viewType: Int
    ) = QuickViewHolder(R.layout.item_appinfo, parent)
}
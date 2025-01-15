package com.stephen.redfindemo.data.bean

import android.graphics.drawable.Drawable

data class AppInfoModel(
    val appIcon: Drawable,
    val appLabel: String,
    val appVersion: String?,
    val packageName: String
) : java.io.Serializable {
    companion object {
        const val serialVersionUID = -55L
    }
}

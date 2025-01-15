package com.stephen.redfindemo.data.bean

import android.graphics.drawable.Drawable

data class ProcessModel(
    val pid: String,
    val packageName: String,
    val cpuinfo: String,
    val meminfo: String
)
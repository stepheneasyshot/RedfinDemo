package com.stephen.redfindemo.feature.debug

import android.content.Context
import android.content.Intent
import android.os.PowerManager
import com.stephen.redfindemo.base.appContext
import com.stephen.redfindemo.data.bean.ProcessModel
import com.stephen.commonhelper.utils.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object DebugManager {

    private val powerManager = appContext.getSystemService(Context.POWER_SERVICE) as PowerManager

    /**
     * 获取进程性能占用
     */
    suspend fun getProcessPerformanceInfo() = withContext(Dispatchers.IO) {
        infoLog()
        val processList = mutableListOf<ProcessModel>()
        repeat(20) {
            processList.add(ProcessModel("13445", "com.strphen.redfindemo", "45%", "120M"))
        }
        processList
    }

    fun rebootSystem() {
        infoLog("execute order == reboot")
        powerManager.reboot("User Action")
    }

    fun shutDownSystem() {
        infoLog("shutDownSystem")
        powerManager.shutdown(false, "user action", false)
    }

    fun resetFactory() {
        val intent = Intent("android.intent.action.FACTORY_RESET")
        intent.setPackage("android")
        intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        intent.putExtra("android.intent.extra.REASON", "MasterClearConfirm")
        appContext.sendBroadcast(intent)
    }
}
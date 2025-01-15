package com.stephen.redfindemo.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stephen.commonhelper.utils.infoLog

class ServiceStarter : BroadcastReceiver() {

    companion object {
        private const val BOOT_ACTION = "android.intent.action.BOOT_COMPLETED"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == BOOT_ACTION) {
            infoLog("onReceive: $BOOT_ACTION")
            context.startService(Intent(context, DeviceMountService::class.java))
        }
    }
}
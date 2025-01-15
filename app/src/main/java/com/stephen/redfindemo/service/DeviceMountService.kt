package com.stephen.redfindemo.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbManager
import com.stephen.commonhelper.utils.getUSBDir
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.base.appContext
import com.stephen.redfindemo.utils.showSimpleToast

class DeviceMountService : Service() {

    private lateinit var deviceMountReceiver: DeviceMountReceiver

    override fun onCreate() {
        super.onCreate()
        infoLog()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        infoLog()
        // 未初始化才去注册
        if (!::deviceMountReceiver.isInitialized) {
            deviceMountReceiver = DeviceMountReceiver()
            infoLog("registerReceiver: DeviceMountReceiver")
            registerReceiver(deviceMountReceiver, IntentFilter().apply {
                addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED)
                addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
            })
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?) = null

    override fun onDestroy() {
        super.onDestroy()
        infoLog()
        unregisterReceiver(deviceMountReceiver)
    }

    class DeviceMountReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            infoLog(intent?.action)
            when (intent?.action) {
                UsbManager.ACTION_USB_DEVICE_ATTACHED -> {
                    val usbLoadDiretory = getUSBDir(appContext)
                    infoLog("U盘挂载目录: $usbLoadDiretory")
                    showSimpleToast("usb device path: $usbLoadDiretory")
                }

                UsbManager.ACTION_USB_DEVICE_DETACHED -> showSimpleToast("DEVICE_DETACHED, 拔出！")
            }
        }
    }
}


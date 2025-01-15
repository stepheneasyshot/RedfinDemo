package com.stephen.redfindemo

import android.app.Application
import android.content.Intent
import android.os.UserHandle
import com.stephen.commonhelper.utils.LogSetting
import com.stephen.commonhelper.utils.UncaughtExceptionHandler
import com.stephen.commonhelper.utils.errorLog
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.service.DeviceMountService

class RedfinApplication : Application() {

    companion object {
        lateinit var instance: Application
    }

    init {
        instance = this
        System.loadLibrary("terminal-channel")
    }

    override fun onCreate() {
        super.onCreate()
        // 多用户判断
        if (UserHandle.myUserId() == 0)
            return
        // 测试抓取trace文件并分析冷启动，模拟耗时
//        sleep(3000L)
        initLogSetting()
        // 生命周期打点
        infoLog()
        // 异常兜底
        UncaughtExceptionHandler.init()
        // 启动USB设备检测服务
        startService(Intent(this, DeviceMountService::class.java))
    }

    private fun initLogSetting() {
        // 初始化LOG打印方法
        LogSetting.initLogSettings(
            "RedfinDemo[${BuildConfig.VERSION_NAME}]",
            if (BuildConfig.BUILD_TYPE == "release") LogSetting.LOG_INFO else LogSetting.LOG_VERBOSE
        )
    }

    override fun onTerminate() {
        super.onTerminate()
        errorLog("onTerminate")
    }
}
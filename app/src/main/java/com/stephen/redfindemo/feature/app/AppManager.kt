package com.stephen.redfindemo.feature.app

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import com.stephen.redfindemo.base.appContext
import com.stephen.redfindemo.data.bean.AppInfoModel
import com.stephen.redfindemo.feature.deviceinfo.DeviceInfoManager.formatSize
import com.stephen.commonhelper.utils.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

object AppManager {

    private var listener: NotifyChangeListener? = null

    private lateinit var receiver: AppChangeBroadcastReceiver

    private val packageManager: PackageManager = appContext.packageManager

    private val activityManager =
        appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    const val PACKAGE_ADDED = "android.intent.action.PACKAGE_ADDED"

    const val PACKAGE_REMOVED = "android.intent.action.PACKAGE_REMOVED"

    fun startListenPackageChange(notifyChangeListener: NotifyChangeListener) {
        this.listener = notifyChangeListener
        receiver = AppChangeBroadcastReceiver()
        infoLog("registerReceiver")
        // 安装和卸载时刷新列表
        val intentFilter = IntentFilter().apply {
            addAction(PACKAGE_ADDED)
            addAction(PACKAGE_REMOVED)
            addDataScheme("package")
        }
        appContext.registerReceiver(receiver, intentFilter)
    }

    fun endListenPackageChange() {
        infoLog()
        this.listener = null
        appContext.unregisterReceiver(receiver)
    }

    suspend fun getAppList() = withContext(Dispatchers.IO) {
        val appInfoList = packageManager.getInstalledApplications(0)
        val adapterList = mutableListOf<AppInfoModel>()
        appInfoList.filter { it.name != null }
            .sortedBy { it.loadLabel(packageManager).toString() }
            .forEach {
                val packageName = it.packageName
                val appIcon = getIconDrawable(packageName)
                val appLabel = getLabelByPackageName(packageName)
                val appVersionName = getVersioName(packageName)
                adapterList.add(AppInfoModel(appIcon, appLabel, appVersionName, packageName))
            }
        adapterList
    }

    /**
     * 应用图标
     */
    private fun getIconDrawable(packageName: String): Drawable =
        packageManager.getApplicationInfo(packageName, 0).loadIcon(packageManager)

    /**
     * 应用名
     */
    fun getLabelByPackageName(packageName: String) =
        packageManager.getApplicationInfo(packageName, 0).loadLabel(packageManager).toString()
            .substringAfterLast(".")

    /**
     * 杀掉进程
     */
    fun forceStopApp(packageName: String) {
        activityManager.forceStopPackage(packageName)
    }

    /**
     * app版本号
     */
    fun getVersioName(packageName: String): String =
        packageManager.getPackageInfo(packageName, 0).versionName

    /**
     * targetSdk
     */
    private fun getTartgetSdk(packageName: String) =
        packageManager.getApplicationInfo(packageName, 0).targetSdkVersion

    /**
     * compileSdk
     */
    private fun getCompileSdk(packageName: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            packageManager.getApplicationInfo(packageName, 0).compileSdkVersion
        } else {
            "require SDK_INT >= S"
        }

    /**
     * app安装位置
     */
    private fun getAppInstallLocation(packageName: String): String =
        packageManager.getApplicationInfo(packageName, 0).sourceDir

    /**
     * 卸载应用返回结果
     */
    fun uninstallApplication(packageName: String): Boolean {
        infoLog(packageName)
        return true
    }

    /**
     * 判断系统应用
     */
    fun isSystemApp(packageName: String) =
        ((packageManager.getApplicationInfo(packageName, 0).flags and ApplicationInfo.FLAG_SYSTEM)
                == ApplicationInfo.FLAG_SYSTEM)

    /**
     * 获取应用缓存大小
     */
    private fun getAppCacheSize(packageName: String) = CacheUtils.getAppCacheSize(packageName)

    /**
     * 获取运行内存占用
     */
    private fun getPrivateDirtyData(packageName: String): String {
        var memorySize = 0
        val packagePid = getPidByPkg(packageName)
        activityManager.runningAppProcesses.forEach {
            val pid = it.pid
            val uid = it.uid
            val processName = it.processName
            // 获取到匹配的包名和pid
            if (pid == packagePid) {
                val memoryInfo = activityManager.getProcessMemoryInfo(intArrayOf(pid))
                infoLog("memory.lenth:${memoryInfo.size}")
                memorySize =
                    memoryInfo[0].dalvikPrivateDirty
                infoLog("pid:$pid, uid:$uid, processName:$processName, memorySize:$memorySize")
            }
        }
        // 格式化到  MB
        return "${memorySize / 10f}MB"
    }

    /**
     * 根据包名获取pid
     */
    private fun getPidByPkg(packageName: String): Int {
        var pid = 0
        val processInfoList: List<ActivityManager.RunningAppProcessInfo> =
            activityManager.runningAppProcesses
        for (processInfo in processInfoList) {
            if (processInfo.processName.equals(packageName)) {
                pid = processInfo.pid
                break
            }
        }
        infoLog("packageName:$packageName, pid:$pid")
        return pid
    }
    /**
     * 应用大小
     */
    private fun getApkSize(packageName: String) =
        formatSize(File(packageManager.getApplicationInfo(packageName, 0).publicSourceDir).length())

    suspend fun getAppDetailInfo(packageName: String) = withContext(Dispatchers.IO) {
        infoLog(packageName)
        val appInfoList = mutableListOf<Pair<String, String>>()
        appInfoList.add(Pair("包名", packageName))
        appInfoList.add(Pair("安装位置", getAppInstallLocation(packageName)))
        appInfoList.add(Pair("是否系统应用", isSystemApp(packageName).toString()))
        appInfoList.add(Pair("APK大小", getApkSize(packageName)))
        appInfoList.add(Pair("运行内存占用", getPrivateDirtyData(packageName)))
        appInfoList.add(Pair("targetSDK", getTartgetSdk(packageName).toString()))
        appInfoList.add(Pair("compileSDK", getCompileSdk(packageName).toString()))
        packageManager.getPackageInfo(
            packageName,
            PackageManager.GET_PERMISSIONS
        ).requestedPermissions?.apply {
            if (isNotEmpty()) {
                val stringBuilder = StringBuilder()
                this.contentToString().substringAfter("[").substringBefore("]").split(",")
                    .forEach {
                        stringBuilder.append(it).append("\n")
                        infoLog("$packageName request: $it")
                    }
                appInfoList.add(Pair("APP权限", stringBuilder.toString()))
            }
        }
        appInfoList.add(Pair("Activity组件", "8"))
        appInfoList.add(Pair("Service组件", "2"))
        appInfoList.add(Pair("缓存数据大小", getAppCacheSize(packageName)))
        // 返回这个list给界面
        appInfoList
    }

    interface NotifyChangeListener {
        fun onChange(changeType: String)
    }

    class AppChangeBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.action?.let {
                infoLog(it)
                when (it) {
                    PACKAGE_REMOVED -> listener?.onChange(PACKAGE_REMOVED)
                    PACKAGE_ADDED -> listener?.onChange(PACKAGE_ADDED)
                    else -> infoLog()
                }
            }
        }
    }
}

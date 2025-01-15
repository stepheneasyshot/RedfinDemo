package com.stephen.redfindemo.feature.deviceinfo

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.core.content.ContextCompat
import com.stephen.redfindemo.base.appContext
import com.stephen.commonhelper.utils.errorLog
import com.stephen.commonhelper.utils.infoLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

object DeviceInfoManager {

    private var batteryLevel = 0

    private var batteryScale = 0

    private var chargeStatus = false

    private var batteryChangeListener: BatteryChangeListener? = null

    private lateinit var receiver: BatteryBroadcastReceiver

    fun startListenBattery(batteryChangeListener: BatteryChangeListener) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        receiver = BatteryBroadcastReceiver()
        appContext.registerReceiver(receiver, filter)
        this.batteryChangeListener = batteryChangeListener
    }

    fun endListenBattery() {
        infoLog()
        batteryChangeListener = null
        appContext.unregisterReceiver(receiver)
    }

    @Deprecated("New plan")
    suspend fun getDeviceInfo() = withContext(Dispatchers.IO) {
        infoLog("getDeviceInfo")
        val infoList = mutableListOf<Pair<String, String>>()
        infoList.add(Pair("Build Number", Build.VERSION.INCREMENTAL))
        infoList.add(Pair("Total ROM", formatSize(totalInternalMemorySize())))
        infoList.add(Pair("Free ROM", formatSize(availableInternalMemorySize())))
        if (getExternalMounts().size > 0) {
            val dirs: Array<File> = ContextCompat.getExternalFilesDirs(appContext, null)
            infoList.add(Pair("Total Ext ROM", formatSize(totalExternalMemorySize(dirs))))
            infoList.add(Pair("Free Ext ROM", formatSize(availableExternalMemorySize(dirs))))
        }
        // 返回这个list给界面
        infoList
    }

    fun getSpeakerStatus() {

    }

    fun getDeviceName() = "${Build.BRAND} ${Build.MODEL}"

    /**
     * 获取CPU型号
     * procinfo无信息，暂时硬编码
     */
    fun getCpuModel(): String {
        infoLog("getCpuModel")
        return "SnapDragon 765G"
    }

    /**
     * 获取电量
     */
    fun getBatteryPercent() = (batteryLevel * 100 / batteryScale.toFloat()).apply {
        infoLog("batteryPercent: $this")
    }

    /**
     * 已用的RAM运存
     */
    fun freeRamMemorySize(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager =
            appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.availMem
    }

    /**
     * 总RAM运存
     */
    fun totalRamMemorySize(): Long {
        val mi = ActivityManager.MemoryInfo()
        val activityManager =
            appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)
        return mi.totalMem
    }

    fun getRamProgressValue() =
        (freeRamMemorySize() * 100 / totalRamMemorySize()).toInt()

    /**
     * 内置剩余硬盘闪存
     */
    fun availableInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }

    /**
     * 内置总硬盘闪存
     */
    fun totalInternalMemorySize(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return totalBlocks * blockSize
    }

    fun getRomProgressValue() =
        (availableInternalMemorySize() * 100 / totalInternalMemorySize()).toInt()

    /**
     * 外部剩余硬盘内存
     */
    private fun availableExternalMemorySize(dirs: Array<File>): Long {
        return if (dirs.size > 1) {
            val stat = StatFs(dirs[1].path)
            val blockSize = stat.blockSizeLong
            val availableBlocks = stat.availableBlocksLong
            availableBlocks * blockSize
        } else {
            0
        }
    }

    /**
     * 外部总硬盘内存
     */
    private fun totalExternalMemorySize(dirs: Array<File>): Long {
        return if (dirs.size > 1) {
            val stat = StatFs(dirs[1].path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            totalBlocks * blockSize
        } else {
            0
        }
    }

    fun formatSize(size: Long): String {
        if (size <= 0) return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }

    /**
     * 判断外置存储区的大小
     */
    private fun getExternalMounts(): HashSet<String> {
        val out = HashSet<String>()
        val reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*"
        var s = ""
        try {
            val process = ProcessBuilder().command("mount")
                .redirectErrorStream(true).start()
            process.waitFor()
            val inputStream = process.inputStream
            val buffer = ByteArray(1024)
            while (inputStream.read(buffer) != -1) {
                s += String(buffer)
            }
            inputStream.close()
        } catch (e: Exception) {
            e.message?.let { errorLog(it) }
        }

        // parse output
        val lines = s.split("\n".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        for (line in lines) {
            if (!line.lowercase().contains("asec")) {
                if (line.matches(reg.toRegex())) {
                    val parts = line.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    for (part in parts) {
                        if (part.startsWith("/")) if (!part.lowercase()
                                .contains("vold")
                        ) out.add(
                            part
                        )
                    }
                }
            }
        }
        infoLog("outsize: $out")
        return out
    }

    class BatteryBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(c: Context, intent: Intent) {
//            deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0)
            batteryChangeListener?.onBatteryValueChange(batteryLevel, batteryScale)
//            health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
//            icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0)
//            plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
//            present = intent.extras!!.getBoolean(BatteryManager.EXTRA_PRESENT)
            chargeStatus = intent.getIntExtra(
                BatteryManager.EXTRA_STATUS,
                0
            ) == BatteryManager.BATTERY_STATUS_CHARGING
//            technology = intent.extras!!.getString(BatteryManager.EXTRA_TECHNOLOGY)
//            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10
//            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        }
    }

    interface BatteryChangeListener {
        fun onBatteryValueChange(level: Int, scale: Int)
    }
}
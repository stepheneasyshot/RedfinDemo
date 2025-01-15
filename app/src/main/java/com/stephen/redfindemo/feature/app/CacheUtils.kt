package com.stephen.redfindemo.feature.app

import android.app.usage.StorageStats
import android.app.usage.StorageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.storage.StorageManager
import android.text.TextUtils
import android.util.Log
import com.stephen.redfindemo.base.appContext
import java.io.IOException
import java.math.BigInteger
import java.text.DecimalFormat
import java.util.UUID

object CacheUtils {

    fun getAppCacheSize(packageName:String):String {
        val storageStatsManager =
            appContext.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        val storageManager = appContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        //获取所有应用的StorageVolume列表
        val storageVolumes = storageManager.storageVolumes
        for (item in storageVolumes) {
            val uuidStr = item.uuid
            val uuid = getUuid(uuidStr)
            val uid = getUid(appContext, packageName)
            //通过包名获取uid
            var storageStats: StorageStats
            try {
                storageStats = storageStatsManager.queryStatsForUid(uuid, uid)
                if (storageStats != null) {
                  return size(storageStats.cacheBytes)
                } else {
                    storageStats = storageStatsManager.queryStatsForUid(uuid, uid)
                    if (storageStats != null) {
                     return size(storageStats.cacheBytes)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ""
    }

    private fun getUuid(uu: String?): UUID {
        var uuid: UUID
        val uuStr = StorageManager.UUID_DEFAULT.toString().replace("-", "")
        if (TextUtils.isEmpty(uu)) {
            uuid = getUU(uuStr)
        } else {
            try {
                uuid = getUU(uu!!.replace("-", ""))
            } catch (e: Exception) {
                Log.i("CacheUtils", "uuid: " + e.message)
                uuid = getUU(uuStr)
                e.fillInStackTrace()
            }
        }
        return uuid
    }

    private fun getUU(uuStr: String): UUID {
        return UUID(
            BigInteger(uuStr.substring(0, 16), 16).toLong(),
            BigInteger(uuStr.substring(16), 16).toLong()
        )
    }

    private fun getUid(context: Context, pakName: String?): Int {
        try {
            return context.packageManager.getApplicationInfo(
                pakName!!,
                PackageManager.GET_META_DATA
            ).uid
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    /**
     * 将文件大小显示为GB,MB等形式
     */
    private fun size(size: Long): String {
        return if (size / (1024 * 1024 * 1024) > 0) {
            val tmpSize = size.toFloat() / (1024 * 1024 * 1024).toFloat()
            val df = DecimalFormat("#.##")
            df.format(tmpSize.toDouble()) + "GB"
        } else if (size / (1024 * 1024) > 0) {
            val tmpSize = size.toFloat() / (1024 * 1024).toFloat()
            val df = DecimalFormat("#.##")
            df.format(tmpSize.toDouble()) + "MB"
        } else if (size / 1024 > 0) {
            (size / 1024).toString() + "KB"
        } else size.toString() + "B"
    }
}

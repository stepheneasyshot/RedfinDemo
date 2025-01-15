package com.stephen.redfindemo.feature.deviceinfo

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.stephen.redfindemo.R
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentDeviceBaseBinding
import com.stephen.commonhelper.utils.infoLog
import kotlinx.coroutines.launch

class DeviceBaseFragment : BaseFragment<FragmentDeviceBaseBinding>(),
    DeviceInfoManager.BatteryChangeListener {

    init {
        DeviceInfoManager.startListenBattery(this)
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDeviceBaseBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()
        lifecycleScope.launch {
            // 基本版本，内存信息
            showDeviceBaseInfo()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showDeviceBaseInfo() {
        // 设备名
        binding?.tvDevicename?.text = DeviceInfoManager.getDeviceName()

        // Android版本
        binding?.idTvAndroidversion?.text =
            requireContext().getString(R.string.device_android, Build.VERSION.RELEASE)

        // 电量，给一个初值
        binding?.tvBattery?.text = "100%"

        // 处理器型号
        binding?.tvProcessor?.text = DeviceInfoManager.getCpuModel()

        // 版本号
        binding?.tvBuildnumber?.text =
            requireContext().getString(R.string.device_buildnumber, Build.VERSION.INCREMENTAL)

        // 运行内存
        binding?.tvRaminfo?.text = requireContext().getString(
            R.string.device_raminfo,
            DeviceInfoManager.formatSize(DeviceInfoManager.freeRamMemorySize()),
            DeviceInfoManager.formatSize(DeviceInfoManager.totalRamMemorySize())
        )
        binding?.sbDeviceram?.progress = DeviceInfoManager.getRamProgressValue()

        binding?.tvRominfo?.text = requireContext().getString(
            R.string.device_rominfo,
            DeviceInfoManager.formatSize(DeviceInfoManager.availableInternalMemorySize()),
            DeviceInfoManager.formatSize(DeviceInfoManager.totalInternalMemorySize())
        )
        binding?.sbDevicerom?.progress = DeviceInfoManager.getRomProgressValue()
    }

    // 电池电量刷新
    @SuppressLint("SetTextI18n")
    override fun onBatteryValueChange(level: Int, scale: Int) {
        infoLog("batteryLevel: $level, batteryScale:$scale")
        binding?.tvBattery?.text = "${DeviceInfoManager.getBatteryPercent().toInt()}%"
    }

    override fun onDestroy() {
        super.onDestroy()
        infoLog()
        DeviceInfoManager.endListenBattery()
    }
}
package com.stephen.redfindemo.feature.deviceinfo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.stephen.redfindemo.R
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentDeviceHardwareBinding
import com.stephen.commonhelper.utils.infoLog
import kotlinx.coroutines.launch

class HardwareInfoFragment : BaseFragment<FragmentDeviceHardwareBinding>() {

    companion object {
        const val AVAILABLE = "Available"
        const val UNAVAILABLE = "Unavailable"
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDeviceHardwareBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()
        lifecycleScope.launch {
            // 硬件状态信息
            showDeviceHarewareInfo()
        }
    }

    private fun showDeviceHarewareInfo() {
        binding?.tvSpakerstatus?.text =
            requireContext().getString(R.string.device_speaker, AVAILABLE)
        binding?.tvCamerastatus?.text =
            requireContext().getString(R.string.device_camera, AVAILABLE)
        binding?.tvCarpowerstatus?.text =
            requireContext().getString(R.string.device_power, UNAVAILABLE)
        binding?.tvCarwindowstatus?.text =
            requireContext().getString(R.string.device_window, AVAILABLE)
        binding?.tvCarseatstatus?.text =
            requireContext().getString(R.string.device_seat, UNAVAILABLE)
        binding?.tvAirconditionerstatus?.text =
            requireContext().getString(R.string.device_hvac, AVAILABLE)
    }

}
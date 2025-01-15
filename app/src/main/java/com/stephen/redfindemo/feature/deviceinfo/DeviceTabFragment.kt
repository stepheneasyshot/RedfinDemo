package com.stephen.redfindemo.feature.deviceinfo

import com.stephen.redfindemo.base.BaseTabFragment

class DeviceTabFragment : BaseTabFragment() {

    override fun initChildFragments() {
        childFragmentsTitle = listOf("基本信息", "硬件信息")
        childFragments = listOf(DeviceBaseFragment(), HardwareInfoFragment())
    }

}
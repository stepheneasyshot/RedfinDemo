package com.stephen.redfindemo.feature.signal

import com.stephen.redfindemo.base.BaseTabFragment

class SignalTabFragment : BaseTabFragment() {
    override fun initChildFragments() {
        childFragmentsTitle = listOf("空调服务", "车身控制")
        childFragments = listOf(HvacFragment(), BodyFragment())
    }
}
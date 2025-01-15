package com.stephen.redfindemo.feature.debug

import com.stephen.redfindemo.base.BaseTabFragment

class DebugTabFragment:BaseTabFragment() {
    override fun initChildFragments() {
        childFragmentsTitle = listOf("快捷操作")
        childFragments = listOf(QuickActionsFragment())
    }
}
package com.stephen.redfindemo.feature.app

import com.stephen.redfindemo.base.BaseTabFragment

class AppTabFragment : BaseTabFragment() {
    override fun initChildFragments() {
        childFragmentsTitle = listOf("APP列表")
        childFragments = listOf(AppListFragment())
    }
}
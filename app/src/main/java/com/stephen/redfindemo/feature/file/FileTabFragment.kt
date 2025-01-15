package com.stephen.redfindemo.feature.file

import com.stephen.redfindemo.base.BaseTabFragment

class FileTabFragment : BaseTabFragment() {
    override fun initChildFragments() {
        childFragmentsTitle = listOf("常用功能", "文件浏览")
        childFragments = listOf(NormalOperateFragment(), FileBrowseFragment())
    }
}
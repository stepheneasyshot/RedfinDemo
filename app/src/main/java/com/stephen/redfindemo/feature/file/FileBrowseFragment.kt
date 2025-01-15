package com.stephen.redfindemo.feature.file

import android.view.LayoutInflater
import android.view.ViewGroup
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentFileBrowseBinding
import com.stephen.commonhelper.utils.infoLog


class FileBrowseFragment : BaseFragment<FragmentFileBrowseBinding>() {
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFileBrowseBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()

    }

}
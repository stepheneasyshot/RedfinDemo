package com.stephen.redfindemo.feature.file

import android.view.LayoutInflater
import android.view.ViewGroup
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentFileNormalBinding
import com.stephen.commonhelper.utils.infoLog

class NormalOperateFragment : BaseFragment<FragmentFileNormalBinding>() {
    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFileNormalBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()
    }

}
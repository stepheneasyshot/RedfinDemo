package com.stephen.redfindemo.feature.signal

import android.view.LayoutInflater
import android.view.ViewGroup
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentSignalBodyBinding
import com.stephen.commonhelper.utils.infoLog

class BodyFragment : BaseFragment<FragmentSignalBodyBinding>() {

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignalBodyBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()
    }
}
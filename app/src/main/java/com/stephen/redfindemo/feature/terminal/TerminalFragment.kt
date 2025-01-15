package com.stephen.redfindemo.feature.terminal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentTerminalBinding
import com.stephen.commonhelper.utils.infoLog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TerminalFragment : BaseFragment<FragmentTerminalBinding>() {

    private var showBoolean = false

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTerminalBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()

        lifecycleScope.launch {
            while (true) {
                delay(600L)
                showBoolean = !showBoolean
                binding?.imgFakeimage?.visibility =
                    if (showBoolean) View.INVISIBLE else View.VISIBLE
            }
        }
    }

}
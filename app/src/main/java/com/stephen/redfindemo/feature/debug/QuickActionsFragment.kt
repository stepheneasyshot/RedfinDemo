package com.stephen.redfindemo.feature.debug

import android.view.LayoutInflater
import android.view.ViewGroup
import com.stephen.redfindemo.R
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.base.appContext
import com.stephen.redfindemo.databinding.FragmentQuickactionsBinding
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.widget.CommonDialog

class QuickActionsFragment : BaseFragment<FragmentQuickactionsBinding>(),
    CommonDialog.OnDialogButonClickListener {

    private lateinit var performanceView: PerformanceView

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentQuickactionsBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog("==========>afterViewCreated<=========")

        binding?.btnRebootsystem?.setOnClickListener {
            CommonDialog(requireContext(), R.string.debug_reboot_notify, this).show()
        }

        binding?.btnShutdown?.setOnClickListener {
            CommonDialog(requireContext(), R.string.debug_shutdown_notify, this).show()
        }

        binding?.btnPerformancewindow?.setOnClickListener {
            if (::performanceView.isInitialized) {
                if (!performanceView.isAttachedToWindow) performanceView.showPerformanceView()
                else infoLog("performanceView is attachedtowindow already")
            } else {
                performanceView = PerformanceView(appContext).apply {
                    showPerformanceView()
                }
            }
        }
    }

    /**
     * 弹窗按钮的两个回调
     */
    override fun onConfirmClick(dialog: CommonDialog) {
        when (dialog.titleResId) {
            R.string.debug_shutdown_notify -> DebugManager.shutDownSystem()
            R.string.debug_reboot_notify -> DebugManager.rebootSystem()
        }
        dialog.dismiss()
    }

    override fun onCancelClick(dialog: CommonDialog) {
        dialog.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::performanceView.isInitialized) {
            performanceView.releaseWindowManager()
        }
    }
}
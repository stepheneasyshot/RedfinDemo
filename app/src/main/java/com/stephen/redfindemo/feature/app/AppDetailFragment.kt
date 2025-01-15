package com.stephen.redfindemo.feature.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentAppDetailBinding
import com.stephen.redfindemo.feature.deviceinfo.CommonKeyValueAdapter
import com.stephen.commonhelper.utils.infoLog
import com.stephen.commonhelper.utils.jumpToAnotherApp
import com.stephen.redfindemo.base.appContext
import com.stephen.redfindemo.utils.showSimpleToast
import kotlinx.coroutines.launch

class AppDetailFragment(packageName: String) :
    BaseFragment<FragmentAppDetailBinding>() {

    private var mPackageName: String = packageName
    private var mappVersion: String = ""
    private var mappLabel: String = ""

    private val appDetailAdapter = CommonKeyValueAdapter(listOf())

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAppDetailBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()
        lifecycleScope.launch {
            // UI不换的按钮
            binding?.btnOpenapp?.setOnClickListener {
                jumpToAnotherApp(appContext, mPackageName)
            }
            binding?.btnKillprocess?.setOnClickListener {
                showSimpleToast("已将 $mappLabel 强行停止")
                AppManager.forceStopApp(mPackageName)
            }
            binding?.btnUninstall?.setOnClickListener {
                lifecycleScope.launch {
                    val uninstallResult = AppManager.uninstallApplication(mPackageName)
                    if (uninstallResult) showSimpleToast("$mappLabel 卸载成功")
                    else showSimpleToast("$mappLabel 卸载失败")
                }
            }

            binding?.rvAppdetailinfo?.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                addItemDecoration(
                    DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
                )
            }
            // UI跟随包名的控件
            updatePackageName(mPackageName)
        }
    }

    @SuppressLint("SetTextI18n")
    fun updatePackageName(packageName: String) {
        mPackageName = packageName
        mappLabel = AppManager.getLabelByPackageName(mPackageName)
        mappVersion = AppManager.getVersioName(mPackageName)
        binding?.tvLabelandversion?.text = "$mappLabel V_$mappVersion"

        binding?.btnUninstall?.apply {
            if (AppManager.isSystemApp(mPackageName)) {
                isEnabled = false
                alpha = 0.7f
            } else {
                isEnabled = true
                alpha = 1.0f
            }
        }
        // 只换列表，不作其他改动
        lifecycleScope.launch {
            binding?.rvAppdetailinfo?.apply {
                appDetailAdapter.updateDetailList(AppManager.getAppDetailInfo(mPackageName))
                adapter = appDetailAdapter
            }
        }
    }
}
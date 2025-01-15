package com.stephen.redfindemo.feature.main

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.stephen.commonhelper.base.BaseActivity
import com.stephen.commonhelper.utils.debugLog
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.R
import com.stephen.redfindemo.databinding.ActivityMainBinding
import com.stephen.redfindemo.feature.app.AppTabFragment
import com.stephen.redfindemo.feature.debug.DebugTabFragment
import com.stephen.redfindemo.feature.deviceinfo.DeviceTabFragment
import com.stephen.redfindemo.feature.file.FileTabFragment
import com.stephen.redfindemo.feature.signal.SignalTabFragment
import com.stephen.redfindemo.feature.terminal.TerminalFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val fragmentMap = mutableMapOf<String, Fragment>()

    companion object {
        val mainItemList =
            listOf(
                "Device Info",
                "App Info",
                "Debug Tools",
                "Signal Simulate",
                "File Operate",
                "Terminal"
            )
    }

    override fun setBinding() = ActivityMainBinding.inflate(layoutInflater)

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infoLog()
//        开机动画调试
//        BootAnimationView(this).showLoadingDialog()
        changeFragment()
        binding.rvMaintab.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = MainAdapter(mainItemList).apply {
                setOnItemClickListener { _, _, position ->
                    changeFragment(position)
                    setChoosedPosition(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    /**
     * 切换fragment
     * @param fragmentTag map里的tag
     */
    private fun changeFragment(fragmentTag: Int = 0) {
        debugLog("fragmentTag: $fragmentTag")
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragment = supportFragmentManager.findFragmentByTag(fragmentTag.toString())
        // 首先隐藏已有fragment
        fragmentMap.entries.forEach {
            fragmentTransaction.hide(it.value)
            it.value.onPause()
        }
        fragment?.let {
            fragmentTransaction.show(it)
            it.onResume()
        } ?: run {
            fragment = when (fragmentTag) {
                0 -> DeviceTabFragment()
                1 -> AppTabFragment()
                2 -> DebugTabFragment()
                3 -> SignalTabFragment()
                4 -> FileTabFragment()
                5 -> TerminalFragment()
                else -> DeviceTabFragment()
            }
            fragmentTransaction.add(
                R.id.fcv_redfin_mainactivity,
                fragment,
                fragmentTag.toString()
            )
            fragmentMap[fragmentTag.toString()] = fragment
        }
        fragmentTransaction.commit()
    }

    override fun onMultiWindowModeChanged(isInMultiWindowMode: Boolean, newConfig: Configuration) {
        super.onMultiWindowModeChanged(isInMultiWindowMode, newConfig)
        infoLog("isInMultiWindowMode $isInMultiWindowMode")
    }

    override fun onStop() {
        super.onStop()
        infoLog("===========>onStop<===========")
    }

    override fun onDestroy() {
        super.onDestroy()
        infoLog("============>onDestory<==========")
    }
}
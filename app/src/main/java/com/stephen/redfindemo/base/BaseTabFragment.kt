package com.stephen.redfindemo.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentBaseTabBinding
import com.stephen.commonhelper.utils.infoLog

abstract class BaseTabFragment : BaseFragment<FragmentBaseTabBinding>(),
    TabLayout.OnTabSelectedListener {

    private lateinit var fragmentManager: FragmentManager

    protected lateinit var childFragments: List<Fragment>

    protected lateinit var childFragmentsTitle: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager = childFragmentManager
    }

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentBaseTabBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        initChildFragments()
        binding?.viewpager?.apply {
            isUserInputEnabled = false
            adapter = FragmentsAdapter(fragmentManager, lifecycle, childFragments)
        }
        binding?.tablayout?.apply {
            addOnTabSelectedListener(this@BaseTabFragment)
            TabLayoutMediator(
                this,
                binding?.viewpager!!
            ) { tab, position -> tab.setText(childFragmentsTitle[position]) }.attach()
            selectTab(this.getTabAt(0))
        }
    }

    protected abstract fun initChildFragments()


    override fun onTabSelected(tab: TabLayout.Tab) {
        infoLog("tab position: ${tab.position}")
        binding?.viewpager?.setCurrentItem(tab.position, false)
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
        infoLog()
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        infoLog()
    }

}
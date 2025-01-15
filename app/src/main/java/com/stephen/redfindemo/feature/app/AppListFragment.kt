package com.stephen.redfindemo.feature.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.stephen.redfindemo.R
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.redfindemo.databinding.FragmentAppBinding
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.widget.LoadingView
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AppListFragment : BaseFragment<FragmentAppBinding>(), AppManager.NotifyChangeListener {

    private lateinit var appDetailFragment: AppDetailFragment

    private lateinit var appListAdapter: AppListAdapter

    private lateinit var loadingView: LoadingView

    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAppBinding.inflate(inflater, container, false)

    @SuppressLint("NotifyDataSetChanged")
    override fun afterViewCreated() {
        infoLog("===========>afterViewCreated<===========")

        AppManager.startListenPackageChange(this)

        loadingView = LoadingView(requireContext()).apply {
            showLoadingDialog()
        }

        lifecycleScope.launch {
            val appList = AppManager.getAppList()
            binding?.rvApplist?.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), GridLayoutManager.VERTICAL, false)
                appListAdapter = AppListAdapter(appList).apply {
                    setOnItemClickListener { _, _, position ->
                        changeFragmentPackageName(appList[position].packageName)
                        setChoosedPosition(position)
                        notifyDataSetChanged()
                    }
                }
                adapter = appListAdapter
            }
            appDetailFragment = AppDetailFragment(appList[0].packageName)
            childFragmentManager.beginTransaction()
                .add(R.id.fcv_applistpage, appDetailFragment)
                .commit()
            loadingView.cancelLoadingView()
        }
    }

    private fun changeFragmentPackageName(packageName: String) {
        infoLog("packageName:$packageName")
        appDetailFragment.updatePackageName(packageName)
    }

    override fun onChange(changeType: String) {
        when (changeType) {
            AppManager.PACKAGE_ADDED, AppManager.PACKAGE_REMOVED -> lifecycleScope.launch {
                binding?.rvApplist?.apply {
                    appListAdapter.updateDetailList(AppManager.getAppList())
                    adapter = appListAdapter
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // 取消加载弹窗
        loadingView.cancelLoadingView()
        // 取消加载，防止泄露
        lifecycleScope.cancel(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        infoLog()
        AppManager.endListenPackageChange()
    }
}
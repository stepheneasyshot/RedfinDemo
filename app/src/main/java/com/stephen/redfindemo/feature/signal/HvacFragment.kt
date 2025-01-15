package com.stephen.redfindemo.feature.signal

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.view.LayoutInflater
import android.view.ViewGroup
import com.stephen.commonhelper.base.BaseFragment
import com.stephen.commonhelper.utils.errorLog
import com.stephen.redfindemo.databinding.FragmentSignalHvacBinding
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.base.appContext

class HvacFragment : BaseFragment<FragmentSignalHvacBinding>() {


    private var mHvacController: HvacController? = null


    private val mServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            infoLog("HvacModel onServiceConnected")
            mHvacController = (service as HvacController.LocalBinder).service
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            infoLog("HvacModel onServiceDisconnected")
            mHvacController = null
        }

    }

    private fun initHvacController() {
        infoLog("HvacModel initHvacController")

        if (!this.requireContext().bindService(
                Intent(this.requireContext(), HvacController::class.java),
                mServiceConnection,
                Context.BIND_AUTO_CREATE
            )
        ) {
            errorLog("bindservice error")
        }
    }

    private fun turnOnAc() {
        infoLog("HvacModel turnOnAc")
        mHvacController?.acState = true
    }


    override fun onCreateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentSignalHvacBinding.inflate(inflater, container, false)

    override fun afterViewCreated() {
        infoLog()
        initHvacController()
        binding?.btnAcOnoff?.setOnClickListener {
            infoLog("HVAC ON/OFF")
            turnOnAc()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mHvacController != null)
            appContext.unbindService(mServiceConnection)
    }
}
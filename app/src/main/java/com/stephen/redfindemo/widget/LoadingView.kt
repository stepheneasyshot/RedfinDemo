package com.stephen.redfindemo.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.FrameLayout
import com.stephen.commonhelper.utils.getLayouutParams
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.R
import com.stephen.redfindemo.base.appContext

class LoadingView(context: Context) : FrameLayout(context) {

    private lateinit var windowLayoutParams: WindowManager.LayoutParams

    private val rootView =
        LayoutInflater.from(context).inflate(R.layout.dialog_loading, this, true)

    private var _windowManager: WindowManager? =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    fun showLoadingDialog() {
        _windowManager?.apply {
            infoLog("addView")
            windowLayoutParams = getLayouutParams(appContext, 2010, true)
            addView(rootView, windowLayoutParams)
        }
    }

    fun cancelLoadingView() {
        infoLog()
        _windowManager?.removeView(this)
        _windowManager = null
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        infoLog()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        infoLog()
    }
}
package com.stephen.redfindemo.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.stephen.commonhelper.utils.getLayouutParams
import com.stephen.commonhelper.utils.infoLog
import com.stephen.redfindemo.R
import com.stephen.redfindemo.base.appContext
import com.stephen.redfindemo.utils.EaseCubicInterpolator
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BootAnimationView(context: Context) : FrameLayout(context) {

    private lateinit var windowLayoutParams: WindowManager.LayoutParams

    private val rootView =
        LayoutInflater.from(context).inflate(R.layout.layout_bootanimation, this, true)

    private var _windowManager: WindowManager? =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    fun showLoadingDialog() {
        _windowManager?.apply {
            infoLog("addView")
            windowLayoutParams = getLayouutParams(appContext, 2010, false, 0, 0)
            addView(rootView, windowLayoutParams)
            initView()
        }
    }

    private fun initView() {
        infoLog()
        val logoText = rootView.findViewById<TextView>(R.id.tv_animationtext)
        val logoic = rootView.findViewById<ImageView>(R.id.iv_ring_logo)
        MainScope().launch {
            delay(2000L)
            startAnim(logoText, logoic)
            delay(3000L)
            cancelLoadingView()
        }
    }


    fun startAnim(textView: View, bgView: View) {
        MainScope().launch {
            //透明度变化
            val alpha1 = ObjectAnimator.ofFloat(textView, "alpha", 0f, 1f)
            val alpha2 = ObjectAnimator.ofFloat(bgView, "alpha", 0f, 1f)
            AnimatorSet().apply {
                duration = 2400L
                interpolator = EaseCubicInterpolator(0.25f, 1f, 0.5f, 1f)
                playTogether(alpha1, alpha2)
                start()
            }
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
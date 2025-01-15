package com.stephen.redfindemo.feature.debug

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stephen.redfindemo.R
import com.stephen.commonhelper.utils.infoLog
import com.stephen.commonhelper.utils.getLayouutParams
import com.stephen.redfindemo.base.appContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PerformanceView(context: Context) : FrameLayout(context) {

    private var _windowManager: WindowManager? = null

    private var startTouchX = 0

    private var startTouchY = 0

    private var windowParamsX = 800

    private var windowParamsY = 200

    private lateinit var windowLayoutParams: WindowManager.LayoutParams

    private val rootView =
        LayoutInflater.from(context).inflate(R.layout.layout_performanceview, this, true)

    fun showPerformanceView() {
        infoLog()
        initView()
        _windowManager =
            appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        _windowManager?.apply {
            windowLayoutParams = getLayouutParams(appContext, 2036, false, windowParamsX, windowParamsY)
            addView(rootView, windowLayoutParams)
        }
    }

    /**
     * 初始化性能监测窗口
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        infoLog()
        rootView.findViewById<ImageButton>(R.id.btn_closewindow).setOnClickListener {
            _windowManager?.removeView(this)
        }

        rootView.findViewById<LinearLayout>(R.id.ll_pw_titlebar).setOnTouchListener { _, event ->
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startTouchX = x
                    startTouchY = y
                    infoLog("MotionEvent ACTION_DOWN, startTouchX:$startTouchX, startTouchY: $startTouchY ")
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_MOVE -> {
                    windowParamsX += x - startTouchX
                    windowParamsY += y - startTouchY
                    windowLayoutParams = getLayouutParams(appContext, 2036, false, windowParamsX, windowParamsY)
                    _windowManager?.updateViewLayout(rootView, windowLayoutParams)
                    startTouchX = x
                    startTouchY = y
                    return@setOnTouchListener true
                }

                MotionEvent.ACTION_UP -> {
                    infoLog("ll_pw_titlebar ACTION_UP")
                    return@setOnTouchListener true
                }

                else -> return@setOnTouchListener true
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            rootView.findViewById<RecyclerView>(R.id.rv_processlist)?.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addItemDecoration(
                    DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
                )
                adapter = PerformanceAdapter(DebugManager.getProcessPerformanceInfo())
            }
        }
    }

    /**
     * 取消外部windowmanager的引用
     */
    fun releaseWindowManager() {
        infoLog()
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
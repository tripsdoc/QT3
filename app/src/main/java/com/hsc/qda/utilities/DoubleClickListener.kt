package com.hsc.qda.utilities

import android.os.Handler
import android.os.SystemClock
import android.view.View

abstract class DoubleClickListener: View.OnClickListener {
    companion object {
        const val DEFAULT_QUALIFICATION_SPAN: Long = 500
    }

    private var timestampLastClick: Long = 0
    private var doubleClickQualificationSpanInMillis: Long = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var isSingleEvent = false

    init {
        doubleClickQualificationSpanInMillis = DEFAULT_QUALIFICATION_SPAN
        timestampLastClick = 0
        handler = Handler()
        runnable = Runnable {
            if (isSingleEvent) {
                onSingleClick()
            }
        }
    }

    override fun onClick(p0: View?) {
        if ((SystemClock.elapsedRealtime() - timestampLastClick) < doubleClickQualificationSpanInMillis) {
            isSingleEvent = false
            handler.removeCallbacks(runnable)
            onDoubleClick()
            return
        }

        isSingleEvent = true
        handler.postDelayed(runnable, DEFAULT_QUALIFICATION_SPAN)
        timestampLastClick = SystemClock.elapsedRealtime()
    }

    abstract fun onDoubleClick()
    abstract fun onSingleClick()
}
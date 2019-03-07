package com.calendar.core

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.ScrollView

class SynchronizedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ScrollView(context, attrs, defStyleAttr), ScrollSynchronizer.SynchronizableScroll {

    init {
        this.viewTreeObserver.addOnGlobalLayoutListener(SyncRegisterer())
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        ScrollSynchronizer.update(this, this.scrollX, this.scrollY)
    }

    override fun onScrollSync(x: Int, y: Int) {
        this.scrollTo(x, y)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ScrollSynchronizer.register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ScrollSynchronizer.unRegister(this)
    }

    private inner class SyncRegisterer : ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            this@SynchronizedScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            ScrollSynchronizer.register(this@SynchronizedScrollView)
        }
    }
}
package com.jmrj.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.ScrollView


class SynchronizedScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ScrollView(context, attrs, defStyleAttr), ScrollSyncronizer.SyncronizableScroll {

    init {
        this.viewTreeObserver.addOnGlobalLayoutListener(SyncRegisterer())
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        ScrollSyncronizer.update(this, this.scrollX, this.scrollY)
    }

    override fun onScrollSync(x: Int, y: Int) {
        this.scrollTo(x, y)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ScrollSyncronizer.register(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ScrollSyncronizer.unRegister(this)
    }

    private inner class SyncRegisterer : ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            this@SynchronizedScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            ScrollSyncronizer.register(this@SynchronizedScrollView)
        }
    }
}
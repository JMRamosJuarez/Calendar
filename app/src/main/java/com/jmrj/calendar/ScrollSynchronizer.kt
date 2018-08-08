package com.jmrj.calendar

object ScrollSynchronizer {

    private val scrolls: MutableList<SynchronizableScroll> = mutableListOf()

    var shouldScrollToCurrentHour: Boolean = true

    private var mOffSetX = 0
    private var mOffSetY = 0

    fun register(s: SynchronizableScroll) {
        s.onScrollSync(mOffSetX, mOffSetY)
        if (!this.scrolls.contains(s)) {
            this.scrolls.add(s)
        }
    }

    fun unRegister(s: SynchronizableScroll) {
        this.scrolls.remove(s)
    }

    fun unRegisterAll() {
        this.mOffSetX = 0
        this.mOffSetY = 0
        this.scrolls.clear()
        this.shouldScrollToCurrentHour = true
    }

    fun update(s: SynchronizableScroll, x: Int, y: Int) {
        this.mOffSetX = x
        this.mOffSetY = y
        for (item in this.scrolls) {
            if (item != s) {
                item.onScrollSync(this.mOffSetX, this.mOffSetY)
            }
        }
    }

    interface SynchronizableScroll {
        fun onScrollSync(x: Int, y: Int)
    }
}
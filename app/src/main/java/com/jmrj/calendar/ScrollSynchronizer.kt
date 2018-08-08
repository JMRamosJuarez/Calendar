package com.jmrj.calendar

object ScrollSynchronizer {

    private val ITEMS: MutableList<SynchronizableScroll> = mutableListOf()

    private var mOffSetX = 0
    private var mOffSetY = 0

    fun register(s: SynchronizableScroll) {
        s.onScrollSync(mOffSetX, mOffSetY)
        if (!ITEMS.contains(s)) {
            ITEMS.add(s)
        }
    }

    fun unRegister(s: SynchronizableScroll) {
        ITEMS.remove(s)
    }

    fun unRegisterAll() {
        this.mOffSetX = 0
        this.mOffSetY = 0
        ITEMS.clear()
    }

    fun update(s: SynchronizableScroll, x: Int, y: Int) {
        mOffSetX = x
        mOffSetY = y
        for (item in ITEMS) {
            if (item != s) {
                item.onScrollSync(mOffSetX, mOffSetY)
            }
        }
    }

    interface SynchronizableScroll {
        fun onScrollSync(x: Int, y: Int)
    }
}
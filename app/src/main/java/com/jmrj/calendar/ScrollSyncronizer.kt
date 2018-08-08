package com.jmrj.calendar

object ScrollSyncronizer {

    private val items: MutableList<SyncronizableScroll> = mutableListOf()

    private var mOffSetX = 0
    private var mOffSetY = 0

    fun register(s: SyncronizableScroll) {
        s.onScrollSync(mOffSetX, mOffSetY)
        if (!items.contains(s)) {
            items.add(s)
        }
    }

    fun unRegister(s: SyncronizableScroll) {
        items.remove(s)
    }

    fun unRegisterAll() {
        this.mOffSetX = 0
        this.mOffSetY = 0
        items.clear()
    }

    fun update(s: SyncronizableScroll, x: Int, y: Int) {
        mOffSetX = x
        mOffSetY = y
        for (item in items) {
            if (item != s) {
                item.onScrollSync(mOffSetX, mOffSetY)
            }
        }
    }

    interface SyncronizableScroll {
        fun onScrollSync(x: Int, y: Int)
    }
}
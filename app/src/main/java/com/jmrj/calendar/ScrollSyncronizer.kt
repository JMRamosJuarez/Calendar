package com.jmrj.calendar

object ScrollSyncronizer {

    private val items: MutableList<SyncronizableScroll> = mutableListOf()

    private var mOffSetX = 0
    private var mOffSetY = 0

    fun register(s: SyncronizableScroll) {
        s.onScrollSync(this.mOffSetX, this.mOffSetY)
        this.items.add(s)
    }

    fun unRegister(s: SyncronizableScroll) {
        this.items.remove(s)
    }

    fun update(s: SyncronizableScroll, x: Int, y: Int) {
        this.mOffSetX = x
        this.mOffSetY = y
        for (item in this.items) {
            if (item != s) {
                item.onScrollSync(this.mOffSetX, this.mOffSetY)
            }
        }
    }

    interface SyncronizableScroll {
        fun onScrollSync(x: Int, y: Int)
    }
}
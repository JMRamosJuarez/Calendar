package com.calendar.core

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.SparseArray
import android.view.ViewGroup

abstract class SmartFragmentStatePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val registeredFragments: SparseArray<Fragment> by lazy {
        val r: SparseArray<Fragment> = SparseArray()
        r
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment: Fragment = super.instantiateItem(container, position) as Fragment
        this.registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        this.registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }

    fun getRegisteredFragment(position: Int): Fragment {
        return this.registeredFragments.get(position)
    }
}
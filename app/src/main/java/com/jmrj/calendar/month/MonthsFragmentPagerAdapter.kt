package com.jmrj.calendar.month

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

internal class MonthsFragmentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = MonthViewFragment.newInstance(position)

    override fun getCount(): Int = 12
}
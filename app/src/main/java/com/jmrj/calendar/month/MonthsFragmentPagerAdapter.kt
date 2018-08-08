package com.jmrj.calendar.month

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.jmrj.calendar.SmartFragmentStatePagerAdapter

internal class MonthsFragmentPagerAdapter(fm: FragmentManager?) : SmartFragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = MonthViewFragment.newInstance(position)

    override fun getCount(): Int = 12
}
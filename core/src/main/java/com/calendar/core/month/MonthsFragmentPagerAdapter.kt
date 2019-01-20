package com.calendar.core.month

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.calendar.core.SmartFragmentStatePagerAdapter

class MonthsFragmentPagerAdapter(fm: FragmentManager?) : SmartFragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment = MonthViewFragment.newInstance(position)

    override fun getCount(): Int = 12
}
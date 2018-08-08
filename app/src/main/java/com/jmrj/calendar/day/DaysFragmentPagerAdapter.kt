package com.jmrj.calendar.day

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.jmrj.calendar.SmartFragmentStatePagerAdapter
import java.util.*

internal class DaysFragmentPagerAdapter(fm: FragmentManager?) : SmartFragmentStatePagerAdapter(fm) {

    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())

    override fun getItem(position: Int): Fragment = DayViewFragment.newInstance(position)

    override fun getCount(): Int = this.calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
}
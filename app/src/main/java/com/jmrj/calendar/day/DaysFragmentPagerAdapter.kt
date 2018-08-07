package com.jmrj.calendar.day

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

internal class DaysFragmentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())

    override fun getItem(position: Int): Fragment = DayViewFragment()

    override fun getCount(): Int = this.calendar.getActualMaximum(Calendar.DAY_OF_YEAR)
}
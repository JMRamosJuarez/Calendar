package com.jmrj.calendar.week

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

class WeeksFragmentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())

    override fun getItem(position: Int): Fragment = WeekFragment()

    override fun getCount(): Int = this.calendar.getActualMaximum(Calendar.WEEK_OF_YEAR)
}
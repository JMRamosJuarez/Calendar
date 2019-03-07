package com.calendar.core.month

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.calendar.core.CalendarEvent
import com.calendar.core.SmartFragmentStatePagerAdapter

class MonthsFragmentPagerAdapter(fm: FragmentManager?, private val calendarEvents: Map<Int, List<CalendarEvent>>) : SmartFragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment =
            MonthViewFragment.newInstance(position, this.calendarEvents[position] ?: emptyList())

    override fun getCount(): Int = 12
}
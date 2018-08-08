package com.jmrj.calendar.week

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.jmrj.calendar.SmartFragmentStatePagerAdapter
import java.util.*

class WeeksFragmentPagerAdapter(fm: FragmentManager?) : SmartFragmentStatePagerAdapter(fm) {

    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())

    override fun getItem(position: Int): Fragment = WeekViewFragment.newInstance(position)

    override fun getCount(): Int = this.calendar.getActualMaximum(Calendar.WEEK_OF_YEAR)
}
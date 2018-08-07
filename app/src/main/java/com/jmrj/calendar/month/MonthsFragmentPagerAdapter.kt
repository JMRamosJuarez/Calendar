package com.jmrj.calendar.month

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

internal class MonthsFragmentPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    private val calendar: Calendar = Calendar.getInstance(Locale.getDefault())

    override fun getItem(position: Int): Fragment = MonthViewFragment()

    override fun getCount(): Int = this.calendar.getActualMaximum(Calendar.MONTH)
}
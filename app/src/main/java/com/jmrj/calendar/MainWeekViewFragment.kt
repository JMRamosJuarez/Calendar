package com.jmrj.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.week.WeeksFragmentPagerAdapter
import kotlinx.android.synthetic.main.main_week_view_layout.*
import java.util.*

class MainWeekViewFragment : Fragment(), DateSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    private val adapter: WeeksFragmentPagerAdapter by lazy { WeeksFragmentPagerAdapter(this.childFragmentManager) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.weeks_view_pager.adapter = this.adapter
        val item: Int = this.calendar.get(Calendar.WEEK_OF_YEAR)
        this.weeks_view_pager.currentItem = item
    }

    override fun onDateSelected(date: Date) {
        Log.i("DATE_SELECTED", date.toString())
    }
}
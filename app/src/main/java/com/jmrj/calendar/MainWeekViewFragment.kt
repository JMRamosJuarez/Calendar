package com.jmrj.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.core.CalendarEvent
import com.calendar.core.DateSelectedListener
import com.calendar.core.EventSelectedListener
import com.calendar.core.week.WeeksFragmentPagerAdapter
import kotlinx.android.synthetic.main.main_week_view_layout.*
import java.util.*

class MainWeekViewFragment : Fragment(), DateSelectedListener, EventSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val events: Map<Int, List<CalendarEvent>> = mapOf(
                Pair(4, listOf(
                        CalendarEvent("#b8ab9b", "Event title", Date(1548126000000), Date(1548129600000))
                ))
        )
        this.weeks_view_pager.adapter =
                WeeksFragmentPagerAdapter(this.childFragmentManager, events)
        val item: Int = this.calendar.get(Calendar.WEEK_OF_YEAR)
        this.weeks_view_pager.currentItem = item
    }

    override fun onDateSelected(date: Date) {
        Log.i("DATE_SELECTED", date.toString())
    }

    override fun onEventSelected(calendarEvent: CalendarEvent) {
        Log.i("DATE_SELECTED", calendarEvent.title)
    }
}
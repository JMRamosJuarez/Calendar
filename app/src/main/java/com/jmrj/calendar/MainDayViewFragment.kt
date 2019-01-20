package com.jmrj.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.day.DayView
import com.jmrj.calendar.day.DaysFragmentPagerAdapter
import kotlinx.android.synthetic.main.main_day_view_fragment_layout.*
import java.util.*

class MainDayViewFragment : Fragment(), DayView.EventSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_day_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val events: Map<Int, List<CalendarEvent>> = mapOf(
                Pair(calendar.get(Calendar.DAY_OF_YEAR), listOf(
                        CalendarEvent("#b8ab9b", "Event title", Date(1547942700000), Date(1547945700000)),
                        CalendarEvent("#88b9b4", "Second Event title", Date(1547954100000), Date(1547959500000))
                ))
        )
        this.days_view_pager.adapter = DaysFragmentPagerAdapter(this.childFragmentManager, events)
        this.days_view_pager.currentItem = this.calendar.get(Calendar.DAY_OF_YEAR)
    }

    override fun onEventSelected(calendarEvent: CalendarEvent) {

    }
}
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

class MainWeekViewFragment : Fragment(), DateSelectedListener, EventSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val events: Map<Int, List<CalendarEvent>> = mapOf(
                Pair(4, listOf(
                        CalendarEvent("#b8ab9b", "Event title", Date(1547985900000), Date(1547990100000)),
                        CalendarEvent("#88b9b4", "Second Event title", Date(1548084300000), Date(1548264600000))
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
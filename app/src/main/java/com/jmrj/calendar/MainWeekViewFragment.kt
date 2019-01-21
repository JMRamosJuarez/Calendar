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
import java.text.SimpleDateFormat
import java.util.*

class MainWeekViewFragment : Fragment(), DateSelectedListener, EventSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    private val dateFormat: SimpleDateFormat by lazy { SimpleDateFormat("dd/mm/yyy hh:mm", Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val events: Map<Int, List<CalendarEvent>> = mapOf(
                Pair(3, listOf(
                        CalendarEvent("#9c27b0", "Date", this.dateFormat.parse("19/01/2019 19:00"), this.dateFormat.parse("22/01/2019 20:57"))
                )),
                Pair(4, listOf(
                        CalendarEvent("#9c27b0", "Date", this.dateFormat.parse("19/01/2019 19:00"), this.dateFormat.parse("22/01/2019 20:57"))
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
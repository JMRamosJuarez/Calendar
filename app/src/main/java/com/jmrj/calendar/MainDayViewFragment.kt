package com.jmrj.calendar

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.core.CalendarEvent
import com.calendar.core.EventSelectedListener
import com.calendar.core.day.DaysFragmentPagerAdapter
import kotlinx.android.synthetic.main.main_day_view_fragment_layout.*
import java.util.*

class MainDayViewFragment : Fragment(), EventSelectedListener {

    private var eventSelectedListener: EventSelectedListener? = null

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_day_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedDay = this.arguments?.getInt("DAY_OF_THE_YEAR", 0) ?: this.calendar.get(Calendar.DAY_OF_YEAR)
        val events: Map<Int, List<CalendarEvent>> = mapOf(
                Pair(selectedDay, listOf(
                        CustomEvent(1, "#b8ab9b", "#000000", "Event title", Date(1547942700000), Date(1547945700000)),
                        CustomEvent(2, "#88b9b4", "#000000", "Second Event title", Date(1547954100000), Date(1547959500000))
                ))
        )
        val adapter = DaysFragmentPagerAdapter(this.childFragmentManager, events)
        this.days_view_pager.adapter = adapter
        this.days_view_pager.currentItem = selectedDay
    }

    override fun onEventSelected(calendarEvent: CalendarEvent) {
        this.eventSelectedListener?.onEventSelected(calendarEvent)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is EventSelectedListener) {
            this.eventSelectedListener = context
        }
    }
}
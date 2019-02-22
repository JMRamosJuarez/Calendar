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
import com.calendar.core.month.MonthsFragmentPagerAdapter
import kotlinx.android.synthetic.main.main_month_view_fragment_layout.*
import java.text.SimpleDateFormat
import java.util.*

class MainMonthViewFragment : Fragment(), DateSelectedListener, EventSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    private val dateFormat: SimpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_month_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deepPurple1 = CalendarEvent("#80673AB7", "#000000",
                "Deep purple",
                this.dateFormat.parse("01/02/2019 19:00"),
                this.dateFormat.parse("04/02/2019 20:57"))

        val blue = CalendarEvent("#802196F3", "#000000",
                "Blue",
                this.dateFormat.parse("02/02/2019 09:00"),
                this.dateFormat.parse("05/02/2019 11:00"))

        val teal = CalendarEvent("#80009688", "#000000",
                "Teal",
                this.dateFormat.parse("03/02/2019 12:35"),
                this.dateFormat.parse("06/02/2019 13:57"))

        val green = CalendarEvent("#804CAF50", "#000000",
                "Green",
                this.dateFormat.parse("04/02/2019 01:35"),
                this.dateFormat.parse("07/02/2019 06:57"))

        val orange = CalendarEvent("#80FF9800", "#000000",
                "Orange",
                this.dateFormat.parse("05/02/2019 12:15"),
                this.dateFormat.parse("08/02/2019 14:07"))

        val brown = CalendarEvent("#80795548", "#000000",
                "Brown",
                this.dateFormat.parse("06/02/2019 15:15"),
                this.dateFormat.parse("09/02/2019 17:59"))

        val events: Map<Int, List<CalendarEvent>> = mapOf(
                Pair(1, listOf(
                        deepPurple1,
                        blue,
                        teal,
                        green,
                        orange,
                        brown
                ))
        )

        this.months_view_pager.adapter = MonthsFragmentPagerAdapter(this.childFragmentManager, events)
        this.months_view_pager.currentItem = this.calendar.get(Calendar.MONTH)
    }

    override fun onDateSelected(date: Date) {
        Log.i("DATE_SELECTED", date.toString())
    }

    override fun onEventSelected(calendarEvent: CalendarEvent) {
        Log.i("EVENT_SELECTED", calendarEvent.title)
    }
}
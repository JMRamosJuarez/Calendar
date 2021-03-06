package com.jmrj.calendar

import android.content.Context
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

    private var eventSelectedListener: EventSelectedListener? = null

    private var dateSelectedListener: DateSelectedListener? = null

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    private val dateFormat: SimpleDateFormat by lazy { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val deepPurple = CustomEvent(1,"#80673AB7", "#000000",
                "Deep purple",
                this.dateFormat.parse("19/01/2019 19:00"),
                this.dateFormat.parse("22/01/2019 20:57"))

        val blue = CustomEvent(2,"#802196F3", "#000000",
                "Blue",
                this.dateFormat.parse("14/01/2019 09:00"),
                this.dateFormat.parse("22/01/2019 11:00"))

        val teal = CustomEvent(3,"#80009688", "#000000",
                "Teal",
                this.dateFormat.parse("21/01/2019 12:35"),
                this.dateFormat.parse("22/01/2019 13:57"))

        val green = CustomEvent(4,"#804CAF50", "#000000",
                "Green",
                this.dateFormat.parse("21/01/2019 01:35"),
                this.dateFormat.parse("22/01/2019 06:57"))

        val orange = CustomEvent(5,"#80FF9800", "#000000",
                "Orange",
                this.dateFormat.parse("22/01/2019 12:15"),
                this.dateFormat.parse("23/01/2019 14:07"))

        val brown = CustomEvent(6,"#80795548", "#000000",
                "Brown",
                this.dateFormat.parse("20/01/2019 15:15"),
                this.dateFormat.parse("24/01/2019 17:59"))

        val events: Map<Int, List<CalendarEvent>> = mapOf(
                Pair(3, listOf(
                        deepPurple,
                        blue
                )),
                Pair(4, listOf(
                        deepPurple,
                        blue,
                        teal,
                        orange,
                        green,
                        brown
                ))
        )

        this.weeks_view_pager.adapter =
                WeeksFragmentPagerAdapter(this.childFragmentManager, events)

        val item: Int = this.calendar.get(Calendar.WEEK_OF_YEAR)

        this.weeks_view_pager.currentItem = item
    }

    override fun onDateSelected(date: Date) {
        this.dateSelectedListener?.onDateSelected(date)
    }

    override fun onEventSelected(calendarEvent: CalendarEvent) {
        this.eventSelectedListener?.onEventSelected(calendarEvent)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is EventSelectedListener) {
            this.eventSelectedListener = context
        }
        if (context is DateSelectedListener) {
            this.dateSelectedListener = context
        }
    }
}
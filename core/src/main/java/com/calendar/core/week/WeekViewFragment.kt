package com.calendar.core.week

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.core.*
import com.calendar.core.EventsHolder.CREATOR.EVENTS_HOLDER
import kotlinx.android.synthetic.main.week_view_layout.*
import java.util.*

class WeekViewFragment : Fragment(), WeekView.OnDayOfWeekSelectedListener {

    companion object {
        fun newInstance(weekOfTheYear: Int, events: List<CalendarEvent>): WeekViewFragment {
            val f = WeekViewFragment()
            val arguments = Bundle().apply {
                putParcelable(EVENTS_HOLDER, EventsHolder(weekOfTheYear, events))
            }
            f.arguments = arguments
            return f
        }
    }

    private var dateSelectedListener: DateSelectedListener? = null

    private var eventSelectedListener: EventSelectedListener? = null

    private val locale: Locale by lazy { Locale.getDefault() }

    private val eventsHolder: EventsHolder?
        get() = this.arguments?.getParcelable(EVENTS_HOLDER)

    private val weekCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(this.locale)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventsHolder = this.eventsHolder
        val selectedWeek = eventsHolder?.index ?: 0
        val events = eventsHolder?.events ?: emptyList()

        this.weekCalendar.set(Calendar.WEEK_OF_YEAR, selectedWeek)

        /** This line is required for the calendar to calculate the correct date,
         *  the method this.weekCalendar.set(Calendar.WEEK_OF_YEAR, selectedWeek)
         *  only set the value week of the year but not re-calculate the date until
         *  the method this.weekCalendar.get(Calendar.WEEK_OF_YEAR) is called
         */
        this.weekCalendar.get(Calendar.WEEK_OF_YEAR)

        this.days_of_week_view.setWeekOfTheYear(selectedWeek)
        this.week_view.setWeekOfTheYear(selectedWeek, events)
        this.week_view.dayOfTheWeekSelectedListener = this
        this.week_view.eventSelectedListener = this.eventSelectedListener
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = this.parentFragment
        if (parentFragment is DateSelectedListener) {
            this.dateSelectedListener = parentFragment
        }
        if (parentFragment is EventSelectedListener) {
            this.eventSelectedListener = parentFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.dateSelectedListener = null
        this.eventSelectedListener = null
    }

    override fun onDayOfWeekSelected(dayOfWeek: Int) {
        this.weekCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        this.dateSelectedListener?.onDateSelected(this.weekCalendar.time)
    }
}
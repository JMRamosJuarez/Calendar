package com.calendar.core.day

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.core.CalendarEvent
import com.calendar.core.EventSelectedListener
import com.calendar.core.R
import kotlinx.android.synthetic.main.day_view_fragment_layout.*

internal class DayViewFragment : Fragment() {

    companion object {
        fun newInstance(dayOfTheYear: Int, events: List<CalendarEvent>): DayViewFragment {
            val f = DayViewFragment()
            val arguments = Bundle().apply {
                putInt(CalendarEvent.INDEX, dayOfTheYear)
                putParcelableArrayList(CalendarEvent.EVENTS, ArrayList(events))
            }
            f.arguments = arguments
            return f
        }
    }

    private var eventSelectedListener: EventSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dayOfTheYear = this.arguments?.getInt(CalendarEvent.INDEX, 0) ?: 0
        val events: List<CalendarEvent> =
                this.arguments?.getParcelableArrayList(CalendarEvent.EVENTS) ?: emptyList()
        this.current_day_view.setDayOfTheYear(dayOfTheYear)
        this.day_view.setEvents(dayOfTheYear, events)
        this.day_view.eventSelectedListener = this.eventSelectedListener
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = this.parentFragment
        if (parentFragment is EventSelectedListener) {
            this.eventSelectedListener = parentFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.eventSelectedListener = null
    }
}
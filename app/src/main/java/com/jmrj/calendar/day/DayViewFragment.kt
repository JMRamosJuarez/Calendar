package com.jmrj.calendar.day

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.CalendarEvent
import com.jmrj.calendar.EventSelectedListener
import com.jmrj.calendar.EventsHolder
import com.jmrj.calendar.EventsHolder.CREATOR.EVENTS_HOLDER
import com.jmrj.calendar.R
import kotlinx.android.synthetic.main.day_view_fragment_layout.*

internal class DayViewFragment : Fragment() {

    companion object {
        fun newInstance(dayOfTheYear: Int, events: List<CalendarEvent>): DayViewFragment {
            val f = DayViewFragment()
            val arguments = Bundle().apply {
                putParcelable(EVENTS_HOLDER, EventsHolder(dayOfTheYear, events))
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
        val eventsHolder: EventsHolder? = this.arguments?.getParcelable(EVENTS_HOLDER)
        this.current_day_view.setDayOfTheYear(eventsHolder?.index ?: 0)
        this.day_view.setEvents(eventsHolder?.index ?: 0,
                eventsHolder?.events ?: emptyList())
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
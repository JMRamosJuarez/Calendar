package com.calendar.core.month

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.core.CalendarEvent
import com.calendar.core.DateSelectedListener
import com.calendar.core.EventSelectedListener
import com.calendar.core.R
import kotlinx.android.synthetic.main.month_view_fragment_layout.*

class MonthViewFragment : Fragment() {

    companion object {
        fun newInstance(monthOfTheYear: Int, calendarEvents: List<CalendarEvent>): MonthViewFragment {
            val f = MonthViewFragment()
            val arguments = Bundle().apply {
                putInt(CalendarEvent.INDEX, monthOfTheYear)
                putParcelableArrayList(CalendarEvent.EVENTS, ArrayList(calendarEvents))
            }
            f.arguments = arguments
            return f
        }
    }

    private var dateSelectedListener: DateSelectedListener? = null

    private var eventSelectedListener: EventSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.month_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val month = this.arguments?.getInt(CalendarEvent.INDEX, 0) ?: 0
        val events: List<CalendarEvent> =
                this.arguments?.getParcelableArrayList(CalendarEvent.EVENTS) ?: emptyList()
        this.month_view.setMonth(month, events)
        this.month_view.dateSelectedListener = this.dateSelectedListener
        this.month_view.eventSelectedListener = this.eventSelectedListener
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
}
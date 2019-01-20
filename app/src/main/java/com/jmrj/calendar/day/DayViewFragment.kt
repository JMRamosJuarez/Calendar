package com.jmrj.calendar.day

import android.content.Context
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.CalendarEvent
import com.jmrj.calendar.R
import kotlinx.android.synthetic.main.day_view_fragment_layout.*

internal class DayViewFragment : Fragment() {

    companion object {
        private const val EVENTS_HOLDER = "EVENTS_HOLDER"
        fun newInstance(dayOfTheYear: Int, events: List<CalendarEvent>): DayViewFragment {
            val f = DayViewFragment()
            val arguments = Bundle().apply {
                putParcelable(EVENTS_HOLDER, EventsHolder(dayOfTheYear, events))
            }
            f.arguments = arguments
            return f
        }
    }

    private class EventsHolder(val dayOfTheYear: Int, val events: List<CalendarEvent>) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.createTypedArrayList(CalendarEvent))

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(dayOfTheYear)
            parcel.writeTypedList(events)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<EventsHolder> {
            override fun createFromParcel(parcel: Parcel): EventsHolder {
                return EventsHolder(parcel)
            }

            override fun newArray(size: Int): Array<EventsHolder?> {
                return arrayOfNulls(size)
            }
        }
    }

    private var eventSelectedListener: DayView.EventSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val eventsHolder: EventsHolder? = this.arguments?.getParcelable(EVENTS_HOLDER)
        this.current_day_view.setDayOfTheYear(eventsHolder?.dayOfTheYear ?: 0)
        this.day_view.setEvents(eventsHolder?.dayOfTheYear ?: 0,
                eventsHolder?.events ?: emptyList())
        this.day_view.eventSelectedListener = this.eventSelectedListener
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = this.parentFragment
        if (parentFragment is DayView.EventSelectedListener) {
            this.eventSelectedListener = parentFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.eventSelectedListener = null
    }
}
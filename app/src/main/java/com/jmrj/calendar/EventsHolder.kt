package com.jmrj.calendar

import android.os.Parcel
import android.os.Parcelable

class EventsHolder(val index: Int, val events: List<CalendarEvent>) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.createTypedArrayList(CalendarEvent))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(index)
        parcel.writeTypedList(events)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EventsHolder> {

        const val EVENTS_HOLDER = "EVENTS_HOLDER"

        override fun createFromParcel(parcel: Parcel): EventsHolder {
            return EventsHolder(parcel)
        }

        override fun newArray(size: Int): Array<EventsHolder?> {
            return arrayOfNulls(size)
        }
    }
}
package com.calendar.core

import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import java.util.*

class CalendarEvent(val hexColor: String, val title: String, val startDate: Date, val endDate: Date) : Parcelable {

    val eventPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor(this.hexColor)
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            Date(parcel.readLong()),
            Date(parcel.readLong()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(hexColor)
        parcel.writeString(title)
        parcel.writeLong(this.startDate.time)
        parcel.writeLong(this.endDate.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalendarEvent> {
        override fun createFromParcel(parcel: Parcel): CalendarEvent {
            return CalendarEvent(parcel)
        }

        override fun newArray(size: Int): Array<CalendarEvent?> {
            return arrayOfNulls(size)
        }
    }
}
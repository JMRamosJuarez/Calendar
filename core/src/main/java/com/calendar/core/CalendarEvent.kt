package com.calendar.core

import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import java.util.*

class CalendarEvent(private val eventHexColor: String,
                    private val textHexColor: String,
                    val title: String,
                    val startDate: Date,
                    val endDate: Date) : Parcelable {

    val eventPaint: Paint by lazy {
        val p = Paint()
        p.color = if (this.eventHexColor.isBlank()) Color.parseColor("#FFFFFF") else Color.parseColor(this.eventHexColor)
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p
    }

    val textPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = if (this.textHexColor.isBlank()) Color.parseColor("#000000") else Color.parseColor(this.textHexColor)
        p.textAlign = Paint.Align.CENTER
        p.textSize = 22f
        p
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            Date(parcel.readLong()),
            Date(parcel.readLong()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventHexColor)
        parcel.writeString(textHexColor)
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

    override fun toString(): String {
        return this.title
    }
}
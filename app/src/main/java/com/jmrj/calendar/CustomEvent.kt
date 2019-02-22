package com.jmrj.calendar

import android.graphics.Color
import android.graphics.Paint
import android.os.Parcel
import android.os.Parcelable
import com.calendar.core.CalendarEvent
import java.util.*

class CustomEvent(private val eventHexColor: String,
                  private val titleHexColor: String,
                  private val title: String,
                  private val startDate: Date,
                  private val endDate: Date) : Parcelable, CalendarEvent {

    private val eventPaint: Paint by lazy {
        val p = Paint()
        p.color = if (this.eventHexColor.isBlank()) Color.parseColor("#FFFFFF") else Color.parseColor(this.eventHexColor)
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p
    }

    private val titlePaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = if (this.titleHexColor.isBlank()) Color.parseColor("#000000") else Color.parseColor(this.titleHexColor)
        p.textAlign = Paint.Align.CENTER
        p.textSize = 22f
        p
    }

    override fun eventPaint(): Paint = this.eventPaint

    override fun titlePaint(): Paint = this.titlePaint

    override fun title(): String = this.title

    override fun startDate(): Date = this.startDate

    override fun endDate(): Date = this.endDate

    override fun toString(): String {
        return this.title
    }

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            Date(parcel.readLong()),
            Date(parcel.readLong()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(eventHexColor)
        parcel.writeString(titleHexColor)
        parcel.writeString(title)
        parcel.writeLong(startDate.time)
        parcel.writeLong(endDate.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomEvent> {
        override fun createFromParcel(parcel: Parcel): CustomEvent {
            return CustomEvent(parcel)
        }

        override fun newArray(size: Int): Array<CustomEvent?> {
            return arrayOfNulls(size)
        }
    }
}
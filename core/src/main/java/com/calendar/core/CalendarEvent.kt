package com.calendar.core

import android.graphics.Paint
import android.os.Parcelable
import java.util.*

interface CalendarEvent : Parcelable {

    companion object {
        const val INDEX = "INDEX"
        const val EVENTS = "EVENTS"
    }

    fun title(): String

    fun startDate(): Date

    fun endDate(): Date

    fun eventPaint(): Paint

    fun titlePaint(): Paint
}
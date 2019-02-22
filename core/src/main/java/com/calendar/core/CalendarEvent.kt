package com.calendar.core

import android.graphics.Paint
import java.util.*

interface CalendarEvent {

    fun title(): String

    fun startDate(): Date

    fun endDate(): Date

    fun eventPaint(): Paint

    fun titlePaint(): Paint
}
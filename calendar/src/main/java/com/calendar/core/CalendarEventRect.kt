package com.calendar.core

import android.graphics.Rect
import android.graphics.RectF
import java.util.*

class CalendarEventRect : RectF {
    constructor(l: Float, t: Float, r: Float, b: Float) : super(l, t, r, b)
    constructor(rect: Rect) : super(rect)
    constructor(rectF: RectF) : super(rectF)

    var calendarEvent: CalendarEvent? = null
    var calendarEvents: List<CalendarEvent> = emptyList()
    var date: Date? = null
    var dayOfTheMonth: Int = -1
    var dayOfTheWeek: Int = -1
    var isCurrentDayOfMonth: Boolean = false
    var isInCurrentMonth: Boolean = false
    var isCurrentDayOfTheWeek: Boolean = false
    var showMore: Boolean = false
}
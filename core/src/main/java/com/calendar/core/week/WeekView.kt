package com.calendar.core.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.calendar.core.*
import java.util.*

class WeekView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val locale: Locale by lazy { Locale.getDefault() }

    private val weekCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(this.locale)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    private val mutableWeekCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(this.locale)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    private val currentTimeCalendar: Calendar by lazy { Calendar.getInstance() }

    private val currentDayOfTheWeek: Int by lazy { this.currentTimeCalendar.get(Calendar.DAY_OF_WEEK) }

    private val currentWeek: Int by lazy { this.currentTimeCalendar.get(Calendar.WEEK_OF_YEAR) }

    private var selectedWeek: Int = 0

    private val X_PARTITION_RATIO = 1 / 7f
    private val Y_PARTITION_RATIO = 1 / 24f

    private val linesPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.STROKE
        p.strokeWidth = 0.5f
        p.color = Color.DKGRAY
        p
    }

    private val currentHourCirclePaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor("#86774b")
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p
    }

    private val currentHourPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor("#86774b")
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.strokeWidth = 2f
        p
    }

    private val currentDayOfWeekPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.LTGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.alpha = 50
        p
    }

    private val whiteTextPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 24f
        p
    }

    var dayOfTheWeekSelectedListener: OnDayOfWeekSelectedListener? = null

    var eventSelectedListener: EventSelectedListener? = null

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(this.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {

                val selectedX = e?.x ?: 0f
                val selectedY = e?.y ?: 0f

                val selectedDayRect = this@WeekView.daysAreas.find { rect ->
                    rect.dayOfTheWeek > -1 && rect.contains(selectedX, selectedY)
                }

                val selectedEventRect = this@WeekView.eventRects.find { rect ->
                    rect.calendarEvent != null && rect.contains(selectedX, selectedY)
                }

                val dayOfTheWeek = selectedDayRect?.dayOfTheWeek ?: -1

                val event = selectedEventRect?.calendarEvent

                if (event != null && this@WeekView.eventSelectedListener != null) {
                    this@WeekView.eventSelectedListener?.onEventSelected(event)
                } else if (dayOfTheWeek > -1) {
                    this@WeekView.dayOfTheWeekSelectedListener?.onDayOfWeekSelected(dayOfTheWeek)
                }

                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return true
            }
        })
    }

    private val daysAreas: List<CalendarEventRect> by lazy { this.createAreas() }

    private var events: List<CalendarEvent> = emptyList()

    private var eventRects: List<CalendarEventRect> = emptyList()

    private var parentScrollView: SynchronizedScrollView? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.drawHorizontalLines(canvas)

        this.drawVerticalLines(canvas)

        this.drawCurrentDayOfWeekColumn(this.daysAreas, canvas)

        if (this.eventRects.isEmpty()) {
            this.eventRects = this.createEventRects(this.events)
        }

        for (eventR in this.eventRects) {
            this.drawEventRect(eventR, canvas)
        }

        if (this.currentWeek == this.selectedWeek) {
            this.drawCurrentHour(canvas)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return this.gestureDetector.onTouchEvent(event)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7) * 32
        setMeasuredDimension(w, h)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        this.scrollToCurrentHour()
    }

    private fun scrollToCurrentHour() {
        if (this.parentScrollView == null && this.currentWeek == this.selectedWeek && ScrollSynchronizer.shouldScrollToCurrentHour) {
            ScrollSynchronizer.shouldScrollToCurrentHour = false
            this.parentScrollView = this.parent as? SynchronizedScrollView
            val y = (this.height.toFloat() * (Y_PARTITION_RATIO * this.getHourInDecimalFormat(this.currentTimeCalendar.timeInMillis))).toInt() - (this.height.toFloat() * Y_PARTITION_RATIO).toInt()
            this.parentScrollView?.onScrollSync(0, y)
        }
    }

    private fun drawCurrentDayOfWeekColumn(areas: List<CalendarEventRect>, canvas: Canvas) {
        for (index in areas.indices) {
            val area = areas[index]
            if (area.isCurrentDayOfTheWeek) {
                canvas.drawRect(area, this.currentDayOfWeekPaint)
            }
        }
    }

    private fun createAreas(): List<CalendarEventRect> {
        val areas: MutableList<CalendarEventRect> = mutableListOf()
        for (i in 0 until 24) {
            val top = this.height * (Y_PARTITION_RATIO * i)
            val bottom = this.height * (Y_PARTITION_RATIO * (i + 1f))
            for (j in 0 until 7) {
                val left = this.width * (X_PARTITION_RATIO * j)
                val right = this.width * (X_PARTITION_RATIO * (j + 1f))
                val rect = CalendarEventRect(left + 1f, top + 1f, right - 1f, bottom - 1f)
                rect.isCurrentDayOfTheWeek = ((j + 1) == this.currentDayOfTheWeek) && this.selectedWeek == this.currentWeek
                rect.dayOfTheWeek = j + 1
                areas.add(rect)
            }
        }
        return areas
    }

    private fun createEventRects(calendarEvents: List<CalendarEvent>): List<CalendarEventRect> {
        val result: MutableList<CalendarEventRect> = mutableListOf()
        for (event in calendarEvents) {
            result.add(this.createEventRect(event))
        }
        return result
    }

    private fun createEventRect(calendarEvent: CalendarEvent): CalendarEventRect {

        val startTime = calendarEvent.startDate.time

        val endTime = calendarEvent.endDate.time

        val decimalStartTime = this.getHourInDecimalFormat(startTime)

        val decimalEndTime = this.getHourInDecimalFormat(endTime)

        val top = (this.height * Y_PARTITION_RATIO) * decimalStartTime

        val bottom = (this.height * Y_PARTITION_RATIO) * decimalEndTime

        val decimalStartDay = this.getStartDaysInDecimalFormat(startTime)

        val decimalEndDay = this.getEndDaysInDecimalFormat(endTime)

        val left = (this.width * decimalStartDay)

        val right = (this.width * decimalEndDay)

        val eventRect = CalendarEventRect(left, top, right, bottom)

        eventRect.calendarEvent = calendarEvent

        return eventRect
    }

    private fun drawEventRect(calendarEventRect: CalendarEventRect, canvas: Canvas) {

        val calendarEvent = calendarEventRect.calendarEvent

        if (calendarEvent != null) {

            canvas.drawRoundRect(calendarEventRect, 4f, 4f, calendarEvent.eventPaint)

            canvas.drawText(calendarEvent.title, calendarEventRect.centerX(), calendarEventRect.centerY() + (this.whiteTextPaint.textSize / 3), this.whiteTextPaint)
        }
    }

    private fun drawVerticalLines(canvas: Canvas) {
        for (i in 0 until 8) {
            //Vertical
            canvas.drawLine(this.width * (X_PARTITION_RATIO * i), 0f, this.width * (X_PARTITION_RATIO * i), this.height.toFloat(), this.linesPaint)
        }
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        for (i in 1 until 24) {
            //Horizontal
            canvas.drawLine(0f, this.height * (Y_PARTITION_RATIO * i), this.width.toFloat(), this.height * (Y_PARTITION_RATIO * i), this.linesPaint)
        }
    }

    private fun drawCurrentHour(canvas: Canvas) {
        val currentHourDecimalFormat = this.getHourInDecimalFormat(this.currentTimeCalendar.timeInMillis)
        canvas.drawLine((this.width / 7f) * (this.currentDayOfTheWeek - 1f),
                this.height * (Y_PARTITION_RATIO * currentHourDecimalFormat),
                (this.width / 7f) * (this.currentDayOfTheWeek),
                this.height * (Y_PARTITION_RATIO * currentHourDecimalFormat),
                this.currentHourPaint)
        canvas.drawCircle((this.width / 7f) * (this.currentDayOfTheWeek - 1f),
                this.height * (Y_PARTITION_RATIO * currentHourDecimalFormat),
                10f, this.currentHourCirclePaint)
    }

    private fun getHourInDecimalFormat(milliseconds: Long): Float {
        val c = Calendar.getInstance()
        c.timeInMillis = milliseconds
        val minutes = c.get(Calendar.MINUTE) / 100f
        val result = (minutes * 100f) / 60f
        val hours = c.get(Calendar.HOUR_OF_DAY)
        return hours + result
    }

    private fun getStartDaysInDecimalFormat(milliseconds: Long): Float {
        val c = Calendar.getInstance()
        c.timeInMillis = milliseconds
        val days = (c.get(Calendar.DAY_OF_WEEK) - 1f) / 100f
        return (days * 100f) / 7f
    }

    private fun getEndDaysInDecimalFormat(milliseconds: Long): Float {
        val c = Calendar.getInstance()
        c.timeInMillis = milliseconds
        val days = c.get(Calendar.DAY_OF_WEEK) / 100f
        return (days * 100f) / 7f
    }

    fun setWeekOfTheYear(weekOfTheYear: Int, events: List<CalendarEvent>) {
        this.selectedWeek = weekOfTheYear
        this.weekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.mutableWeekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.events = events
        this.invalidate()
    }

    interface OnDayOfWeekSelectedListener {

        fun onDayOfWeekSelected(dayOfWeek: Int)
    }
}
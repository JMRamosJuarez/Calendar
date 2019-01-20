package com.jmrj.calendar.day

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.jmrj.calendar.CalendarEvent
import com.jmrj.calendar.CalendarEventRect
import com.jmrj.calendar.ScrollSynchronizer
import com.jmrj.calendar.SynchronizedScrollView
import java.util.*

internal class DayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val linesPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.DKGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.strokeWidth = 0.5f
        p
    }

    private val textPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor("#9c9c9c")
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 24f
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

    private val currentHourPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor("#86774b")
        p.isAntiAlias = true
        p.style = Paint.Style.STROKE
        p.strokeWidth = 2f
        p
    }

    private val currentHourCirclePaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor("#86774b")
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p
    }

    var eventSelectedListener: EventSelectedListener? = null

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(this.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {

                val selectedX = e?.x ?: 0f
                val selectedY = e?.y ?: 0f

                val selectedRect = this@DayView.eventRects.find { rect ->
                    rect.calendarEvent != null && rect.contains(selectedX, selectedY)
                }

                val event = selectedRect?.calendarEvent

                if (event != null) {
                    this@DayView.eventSelectedListener?.onEventSelected(event)
                }

                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return true
            }
        })
    }

    private val currentTimeCalendar: Calendar by lazy { Calendar.getInstance() }

    private val currentDayOfTheYear: Int by lazy { this.currentTimeCalendar.get(Calendar.DAY_OF_YEAR) }

    private var selectedDayOfTheYear: Int = 0

    private var events: List<CalendarEvent> = emptyList()

    private var parentScrollView: SynchronizedScrollView? = null

    private val Y_PARTITION_RATIO = 1 / 24f

    private var eventRects: List<CalendarEventRect> = emptyList()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.drawHorizontalLines(canvas)

        if (this.eventRects.isEmpty()) {
            this.eventRects = this.createEventRects(this.events)
        }

        for (eventR in this.eventRects) {
            this.drawEventRect(eventR, canvas)
        }

        this.drawCurrentHour(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7) * 24
        setMeasuredDimension(w, h)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        this.scrollToCurrentHour()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return this.gestureDetector.onTouchEvent(event)
    }

    private fun scrollToCurrentHour() {
        if (this.parentScrollView == null && this.isCurrentDay() && ScrollSynchronizer.shouldScrollToCurrentHour) {
            ScrollSynchronizer.shouldScrollToCurrentHour = false
            this.parentScrollView = this.parent as? SynchronizedScrollView
            val y = (this.height.toFloat() * (Y_PARTITION_RATIO * this.getHourInDecimalFormat(this.currentTimeCalendar.timeInMillis))).toInt() - (this.height.toFloat() * Y_PARTITION_RATIO).toInt()
            this.parentScrollView?.onScrollSync(0, y)
        }
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        for (i in 1 until 24) {
            canvas.drawLine(this.width / 7f, this.height * (Y_PARTITION_RATIO * i), this.width.toFloat() - (this.width.toFloat() / 28f), this.height * (Y_PARTITION_RATIO * i), this.linesPaint)
            canvas.drawText(String.format("%02d:00", i), this.width / 14f, (this.height * (Y_PARTITION_RATIO * i)) + (this.textPaint.textSize / 3), this.textPaint)
        }
    }

    private fun drawCurrentHour(canvas: Canvas) {
        if (this.isCurrentDay()) {
            val currentHourDecimal = this.getHourInDecimalFormat(this.currentTimeCalendar.timeInMillis)
            val y = this.height * (Y_PARTITION_RATIO * currentHourDecimal)
            canvas.drawLine(
                    this.width / 7f,
                    y,
                    this.width - (this.width / 28f),
                    y,
                    this.currentHourPaint)
            canvas.drawCircle(
                    (this.width / 7f) + 10f,
                    y,
                    10f,
                    this.currentHourCirclePaint)
        }
    }

    private fun createEventRects(calendarEvents: List<CalendarEvent>): List<CalendarEventRect> {
        val result: MutableList<CalendarEventRect> = mutableListOf()
        for (event in calendarEvents) {
            result.add(this.createEventRect(event))
        }
        return result
    }

    private fun createEventRect(calendarEvent: CalendarEvent): CalendarEventRect {

        val startTime = this.getHourInDecimalFormat(calendarEvent.startDate.time)

        val endTime = this.getHourInDecimalFormat(calendarEvent.endDate.time)

        val top = this.height * (Y_PARTITION_RATIO * startTime)

        val bottom = this.height * (Y_PARTITION_RATIO * endTime)

        val eventRect = CalendarEventRect(this.width / 7f, top, this.width - (this.width / 28f), bottom)

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

    private fun isCurrentDay(): Boolean = this.currentDayOfTheYear == this.selectedDayOfTheYear

    private fun getHourInDecimalFormat(milliseconds: Long): Float {
        val c = Calendar.getInstance()
        c.timeInMillis = milliseconds
        val minutes = c.get(Calendar.MINUTE) / 100f
        val result = (minutes * 100f) / 60f
        val hours = c.get(Calendar.HOUR_OF_DAY)
        return hours + result
    }

    fun setEvents(dayOfTheYear: Int, events: List<CalendarEvent>) {
        this.selectedDayOfTheYear = dayOfTheYear
        this.events = events
    }

    interface EventSelectedListener {

        fun onEventSelected(calendarEvent: CalendarEvent)
    }
}
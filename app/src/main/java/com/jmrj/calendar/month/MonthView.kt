package com.jmrj.calendar.month

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import java.util.*

class MonthView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val locale: Locale by lazy { Locale.getDefault() }

    private val monthCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(this.locale)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.WEEK_OF_MONTH, 1)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    private val mutableMonthCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(this.locale)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.set(Calendar.WEEK_OF_MONTH, 1)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    private val currentDayCalendar: Calendar by lazy { Calendar.getInstance(this.locale) }

    private val daysOfTheWeekCalendar: Calendar by lazy { Calendar.getInstance(this.locale) }

    private val currentMonth: Int by lazy { this.currentDayCalendar.get(Calendar.MONTH) }
    private val currentDay: Int by lazy { this.currentDayCalendar.get(Calendar.DAY_OF_MONTH) }
    private val currentDayOfTheWeek: Int by lazy { this.currentDayCalendar.get(Calendar.DAY_OF_WEEK) }

    private var monthOfTheYear: Int = 0

    private val linesPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.STROKE
        p.strokeWidth = 0.5f
        p.color = Color.LTGRAY
        p
    }

    private val dayPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.TRANSPARENT
        p
    }

    private val selectedDayPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.RED
        p
    }

    private val currentDatePaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.parseColor("#86774b")
        p
    }

    private val whiteTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.WHITE
        p.textAlign = Paint.Align.CENTER
        p.textSize = 18f
        p
    }

    private val dayOutOfCurrentMonthTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.parseColor("#9c9c9c")
        p.textAlign = Paint.Align.CENTER
        p.textSize = 18f
        p
    }

    private val dayOfCurrentMonthTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.parseColor("#4a4a4a")
        p.textAlign = Paint.Align.CENTER
        p.textSize = 18f
        p
    }

    private val daysOfTheWeekTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.parseColor("#9c9c9c")
        p.textAlign = Paint.Align.CENTER
        p.textSize = 32f
        p
    }

    private val currentDayOfTheWeekTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.parseColor("#86774b")
        p.textAlign = Paint.Align.CENTER
        p.textSize = 32f
        p.typeface = Typeface.DEFAULT_BOLD
        p
    }

    private val marginTop: Float
        get() = this.height * (1 / 14f)

    private var daysAreas: List<RectF> = emptyList()

    private val X_PARTITION_RATIO = 1 / 7f
    private val Y_PARTITION_RATIO = 1 / 6f

    private var selectedX: Float = 0f
    private var selectedY: Float = 0f

    private val gestureDetector: GestureDetector by lazy {
        GestureDetector(this.context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                this@MonthView.selectedX = e?.x ?: 0f
                this@MonthView.selectedY = e?.y ?: 0f
                this@MonthView.invalidate()
                return true
            }

            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return true
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val marginTop = this.marginTop

        this.daysAreas = this.createAreas(marginTop)

        this.drawDaysOfTheWeek(this.currentDayOfTheWeek, canvas)

        this.drawAreas(this.daysAreas, canvas)

        this.drawHorizontalLines(marginTop, canvas)

        this.drawVerticalLines(marginTop, canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return this.gestureDetector.onTouchEvent(event)
    }

    private fun drawDaysOfTheWeek(currentDayOfTheWeek: Int, canvas: Canvas) {
        val top = 0f
        val bottom = this.height * (1 / 14f)
        for (i in 0 until 7) {
            val left = this.width.toFloat() * (X_PARTITION_RATIO * i)
            val right = this.width.toFloat() * (X_PARTITION_RATIO * (i + 1))
            val rect = RectF(left + 1f, top + 1f, right - 1f, bottom - 1f)
            canvas.drawText(this.getDayOfTheWeek(i), rect.centerX(), rect.centerY() + (this.daysOfTheWeekTextPaint.textSize / 3), if ((this.currentMonth == this.monthOfTheYear) && currentDayOfTheWeek == (i + 1)) this.currentDayOfTheWeekTextPaint else this.daysOfTheWeekTextPaint)
        }
    }

    private fun drawAreas(areas: List<RectF>, canvas: Canvas) {
        this.mutableMonthCalendar.timeInMillis = this.monthCalendar.timeInMillis
        for (index in areas.indices) {
            val area = areas[index]
            val month = this.mutableMonthCalendar.get(Calendar.MONTH)
            val dayOfTheMonth = this.mutableMonthCalendar.get(Calendar.DAY_OF_MONTH)
            val isCurrentDate = this.isCurrentDate(month, dayOfTheMonth)
            canvas.drawRect(area, if (area.contains(this.selectedX, this.selectedY)) this.selectedDayPaint else this.dayPaint)

            val dayRect = RectF(area.left + 5, area.top + 5, area.right - 5, area.top + 40f)

            if (isCurrentDate) {
                canvas.drawRoundRect(dayRect, 4f, 4f, this.currentDatePaint)
            }

            canvas.drawText("$dayOfTheMonth", dayRect.centerX(), dayRect.centerY() + (this.whiteTextPaint.textSize / 3), if (isCurrentDate) this.whiteTextPaint else if (month == this.monthOfTheYear) this.dayOfCurrentMonthTextPaint else this.dayOutOfCurrentMonthTextPaint)
            this.mutableMonthCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    private fun createAreas(marginTop: Float): List<RectF> {
        val areas: MutableList<RectF> = mutableListOf()
        for (i in 0 until 6) {
            val top = ((this.height - marginTop) * (Y_PARTITION_RATIO * i)) + marginTop
            val bottom = ((this.height - marginTop) * (Y_PARTITION_RATIO * (i + 1))) + marginTop
            for (j in 0 until 7) {
                val left = this.width.toFloat() * (X_PARTITION_RATIO * j)
                val right = this.width.toFloat() * (X_PARTITION_RATIO * (j + 1))
                val rect = RectF(left + 1f, top + 1f, right - 1f, bottom - 1f)
                areas.add(rect)
            }
        }
        return areas
    }

    private fun drawVerticalLines(marginTop: Float, canvas: Canvas) {
        for (i in 0 until 8) {
            //Vertical
            canvas.drawLine(this.width * (X_PARTITION_RATIO * i), marginTop, this.width * (X_PARTITION_RATIO * i), this.height.toFloat(), this.linesPaint)
        }
    }

    private fun drawHorizontalLines(marginTop: Float, canvas: Canvas) {
        for (i in 0 until 6) {
            //Horizontal
            canvas.drawLine(0f, ((this.height - marginTop) * (Y_PARTITION_RATIO * i)) + marginTop, this.width.toFloat(), ((this.height - marginTop) * (Y_PARTITION_RATIO * i)) + marginTop, this.linesPaint)
        }
    }

    private fun getDayOfTheWeek(index: Int): String {
        return when (index) {
            0 -> {
                this.daysOfTheWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                val dayName = this.daysOfTheWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale)
                dayName.first().toUpperCase().toString()
            }
            1 -> {
                this.daysOfTheWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONTH)
                val dayName = this.daysOfTheWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale)
                dayName.first().toUpperCase().toString()
            }
            2 -> {
                this.daysOfTheWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY)
                val dayName = this.daysOfTheWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale)
                dayName.first().toUpperCase().toString()
            }
            3 -> {
                this.daysOfTheWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
                val dayName = this.daysOfTheWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale)
                dayName.first().toUpperCase().toString()
            }
            4 -> {
                this.daysOfTheWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY)
                val dayName = this.daysOfTheWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale)
                dayName.first().toUpperCase().toString()
            }
            5 -> {
                this.daysOfTheWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
                val dayName = this.daysOfTheWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale)
                dayName.first().toUpperCase().toString()
            }
            6 -> {
                this.daysOfTheWeekCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY)
                val dayName = this.daysOfTheWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale)
                dayName.first().toUpperCase().toString()
            }
            else -> "X"
        }
    }

    private fun isCurrentDate(month: Int, day: Int): Boolean {
        return this.currentMonth == month && this.currentDay == day && month == this.monthOfTheYear
    }

    fun setMonth(monthOfTheYear: Int) {
        this.monthOfTheYear = monthOfTheYear
        this.monthCalendar.set(Calendar.MONTH, monthOfTheYear)
        this.mutableMonthCalendar.set(Calendar.MONTH, monthOfTheYear)
        this.invalidate()
    }
}
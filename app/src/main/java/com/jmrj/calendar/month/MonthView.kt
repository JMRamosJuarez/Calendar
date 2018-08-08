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

    private val currentDayCalendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    private val currentMonth: Int by lazy { this.currentDayCalendar.get(Calendar.MONTH) }
    private val currentDay: Int by lazy { this.currentDayCalendar.get(Calendar.DAY_OF_MONTH) }

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

    private val circlePaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.BLUE
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

    private val grayTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.GRAY
        p.textAlign = Paint.Align.CENTER
        p.textSize = 18f
        p
    }

    private val dayOfCurrentMonthTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.BLUE
        p.textAlign = Paint.Align.CENTER
        p.textSize = 18f
        p
    }

    private val daysOfTheWeekTextPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.DKGRAY
        p.textAlign = Paint.Align.CENTER
        p.typeface = Typeface.DEFAULT_BOLD
        p.textSize = 22f
        p
    }

    private val marginTop: Float
        get() = this.height * (1 / 28f)

    private val daysAreas: List<RectF>
        get() = this.createAreas(this.marginTop)

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

        val areas = this.daysAreas

        val marginTop = this.marginTop

        this.drawDaysOfTheWeek(marginTop, canvas)

        this.drawAreas(areas, canvas)

        this.drawHorizontalLines(marginTop, canvas)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return this.gestureDetector.onTouchEvent(event)
    }

    private fun drawDaysOfTheWeek(marginTop: Float, canvas: Canvas) {
        for (i in 0 until 7) {
            canvas.drawText(this.getDayOfTheWeek(i), (this.width * (X_PARTITION_RATIO * i)) + 28f, marginTop, this.daysOfTheWeekTextPaint)
        }
    }

    private fun drawAreas(areas: List<RectF>, canvas: Canvas) {
        this.mutableMonthCalendar.timeInMillis = this.monthCalendar.timeInMillis
        for (index in areas.indices) {
            val area = areas[index]
            val dayOfTheMonth = this.mutableMonthCalendar.get(Calendar.DAY_OF_MONTH)
            val month = this.mutableMonthCalendar.get(Calendar.MONTH)
            canvas.drawRect(area, if (area.contains(this.selectedX, this.selectedY)) this.selectedDayPaint else this.dayPaint)
            if (this.isCurrentDate(month, dayOfTheMonth)) {
                canvas.drawCircle(area.left + 28f, area.top + 28, this.grayTextPaint.textSize, this.circlePaint)
            }
            canvas.drawText("$dayOfTheMonth", area.left + 28f, area.top + 33.5f, if (this.isCurrentDate(month, dayOfTheMonth)) this.whiteTextPaint else if (month == this.monthOfTheYear) this.dayOfCurrentMonthTextPaint else this.grayTextPaint)
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
        for (i in 1 until 6) {
            //Horizontal
            canvas.drawLine(0f, ((this.height - marginTop) * (Y_PARTITION_RATIO * i)) + marginTop, this.width.toFloat(), ((this.height - marginTop) * (Y_PARTITION_RATIO * i)) + marginTop, this.linesPaint)
        }
    }

    private fun getDayOfTheWeek(index: Int): String {
        return when (index) {
            0 -> "D"
            1 -> "L"
            2 -> "M"
            3 -> "M"
            4 -> "J"
            5 -> "V"
            6 -> "S"
            else -> "N"
        }
    }

    private fun isCurrentDate(month: Int, day: Int) : Boolean {
        return this.currentMonth == month && this.currentDay == day && month == this.monthOfTheYear
    }

    fun setMonth(monthOfTheYear: Int) {
        this.monthOfTheYear = monthOfTheYear
        this.monthCalendar.set(Calendar.MONTH, monthOfTheYear)
        this.mutableMonthCalendar.set(Calendar.MONTH, monthOfTheYear)
        this.invalidate()
    }
}
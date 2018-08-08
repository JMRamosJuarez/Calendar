package com.jmrj.calendar.week

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
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
        p.color = Color.BLUE
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p
    }

    private val currentHourPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.BLUE
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

    private val dayPaint: Paint by lazy {
        val p = Paint()
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.color = Color.TRANSPARENT
        p
    }

    private val daysAreas: List<ColumnRect>
        get() = this.createAreas()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.drawHorizontalLines(canvas)
        this.drawVerticalLines(canvas)

        val areas = this.daysAreas

        this.drawAreas(areas, canvas)

        if (this.currentWeek == this.selectedWeek) {
            this.drawCurrentHour(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7) * 32
        setMeasuredDimension(w, h)
    }

    private fun drawAreas(areas: List<ColumnRect>, canvas: Canvas) {
        for (index in areas.indices) {
            val area = areas[index]
            canvas.drawRect(area, if (area.isCurrentDayOfTheWeek) this.currentDayOfWeekPaint else this.dayPaint)
        }
    }

    private fun createAreas(): List<ColumnRect> {
        val areas: MutableList<ColumnRect> = mutableListOf()
        for (i in 0 until 24) {
            val top = this.height * (Y_PARTITION_RATIO * i)
            val bottom = this.height * (Y_PARTITION_RATIO * (i + 1f))
            for (j in 0 until 7) {
                val left = this.width.toFloat() * (X_PARTITION_RATIO * j)
                val right = this.width.toFloat() * (X_PARTITION_RATIO * (j + 1f))
                val rect = ColumnRect(left + 1f, top + 1f, right - 1f, bottom - 1f)
                rect.isCurrentDayOfTheWeek = ((j + 1) == this.currentDayOfTheWeek) && this.selectedWeek == this.currentWeek
                areas.add(rect)
            }
        }
        return areas
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
        canvas.drawLine((this.width.toFloat() / 7f) * (this.currentDayOfTheWeek - 1),
                this.height.toFloat() * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()),
                (this.width.toFloat() / 7f) * (this.currentDayOfTheWeek),
                this.height.toFloat() * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()),
                this.currentHourPaint)
        canvas.drawCircle((this.width.toFloat() / 7f) * (this.currentDayOfTheWeek - 1),
                this.height * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()),
                10f, this.currentHourCirclePaint)
    }

    private fun getCurrentHourInDecimalFormat(): Float {
        val minutes = this.currentTimeCalendar.get(Calendar.MINUTE) / 100f
        val hours = this.currentTimeCalendar.get(Calendar.HOUR_OF_DAY)
        val result = (minutes * 100f) / 60f
        return hours + result
    }

    fun setWeekOfTheYear(weekOfTheYear: Int) {
        this.selectedWeek = weekOfTheYear
        this.weekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.mutableWeekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.invalidate()
    }

    class ColumnRect : RectF {
        constructor(l: Float, t: Float, r: Float, b: Float) : super(l, t, r, b)
        constructor(rect: Rect) : super(rect)
        constructor(rectF: RectF) : super(rectF)

        var isCurrentDayOfTheWeek: Boolean = false
    }
}
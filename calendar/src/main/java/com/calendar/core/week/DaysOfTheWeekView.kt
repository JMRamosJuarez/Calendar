package com.calendar.core.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.util.*

class DaysOfTheWeekView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

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

    private val previousDayNumberPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.LTGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 48f
        p
    }

    private val previousDayNamePaint: Paint by lazy {
        val p = Paint()
        p.color = Color.LTGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 24f
        p
    }

    private val currentDayNumberPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor("#86774b")
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 48f
        p
    }

    private val currentDayNamePaint: Paint by lazy {
        val p = Paint()
        p.color = Color.parseColor("#86774b")
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 24f
        p
    }

    private val nextDayNumberPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.DKGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 48f
        p
    }

    private val nextDayNamePaint: Paint by lazy {
        val p = Paint()
        p.color = Color.DKGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 24f
        p
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.drawDaysOfTheWeek(canvas)
        this.drawBottomLine(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7) * 32
        val rh = h * Y_PARTITION_RATIO
        setMeasuredDimension(w, rh.toInt())
    }

    private fun drawDaysOfTheWeek(canvas: Canvas) {

        this.mutableWeekCalendar.timeInMillis = this.weekCalendar.timeInMillis

        for (i in 0 until 8) {

            canvas.drawLine(this.width * (X_PARTITION_RATIO * i), 0f, this.width * (X_PARTITION_RATIO * i), this.height.toFloat(), this.linesPaint)

            val left = this.width.toFloat() * (X_PARTITION_RATIO * i)
            val right = this.width.toFloat() * (X_PARTITION_RATIO * (i + 1))

            val area = RectF(left + 1f, 1f, right - 1f, this.height.toFloat() - 1f)

            val day = this.mutableWeekCalendar.get(Calendar.DAY_OF_MONTH)

            val isCurrentDayOfTheWeek = ((i + 1) == this.currentDayOfTheWeek) && this.selectedWeek == this.currentWeek

            canvas.drawText(String.format("%02d", day),
                    area.centerX(),
                    area.centerY(),
                    when {
                        this.selectedWeek < this.currentWeek -> this.previousDayNumberPaint
                        this.selectedWeek > this.currentWeek || (i + 1) > this.currentDayOfTheWeek -> this.nextDayNumberPaint
                        isCurrentDayOfTheWeek -> this.currentDayNumberPaint
                        else -> this.previousDayNumberPaint
                    })

            canvas.drawText(this.mutableWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale),
                    area.centerX(),
                    area.centerY() + (this.previousDayNamePaint.textSize / 3f) + 16f,
                    when {
                        this.selectedWeek < this.currentWeek -> this.previousDayNamePaint
                        this.selectedWeek > this.currentWeek || (i + 1) > this.currentDayOfTheWeek -> this.nextDayNamePaint
                        isCurrentDayOfTheWeek -> this.currentDayNamePaint
                        else -> this.previousDayNamePaint
                    })

            this.mutableWeekCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    private fun drawBottomLine(canvas: Canvas) {
        canvas.drawLine(0f, this.height.toFloat(), this.width.toFloat(), this.height.toFloat(), this.linesPaint)
    }

    fun setWeekOfTheYear(weekOfTheYear: Int) {
        this.selectedWeek = weekOfTheYear
        this.weekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.mutableWeekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.invalidate()
    }
}
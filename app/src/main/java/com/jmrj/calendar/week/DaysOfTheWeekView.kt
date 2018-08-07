package com.jmrj.calendar.week

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

//    private val mutableCalendar: Calendar by lazy {
//        val calendar: Calendar = Calendar.getInstance(this.locale)
//        calendar.set(Calendar.MONTH, Calendar.JANUARY)
//        calendar.set(Calendar.DAY_OF_MONTH, 1)
//        calendar.clear(Calendar.HOUR)
//        calendar.clear(Calendar.HOUR_OF_DAY)
//        calendar.clear(Calendar.MINUTE)
//        calendar.clear(Calendar.SECOND)
//        calendar.clear(Calendar.MILLISECOND)
//        calendar
//    }

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

    private val dayNumberPaint: Paint by lazy {
        val p = Paint()
        p.color = Color.DKGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 48f
        p
    }

    private val dayNamePaint: Paint by lazy {
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
        this.drawVerticalLines(canvas)
        this.drawBottomLine(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7) * 32
        val rh = h * Y_PARTITION_RATIO
        setMeasuredDimension(w, rh.toInt())
    }

    private fun drawVerticalLines(canvas: Canvas) {

        this.mutableWeekCalendar.timeInMillis = this.weekCalendar.timeInMillis

        for (i in 0 until 8) {
            //Vertical
            canvas.drawLine(this.width * (X_PARTITION_RATIO * i), 0f, this.width * (X_PARTITION_RATIO * i), this.height.toFloat(), this.linesPaint)

            val left = this.width.toFloat() * (X_PARTITION_RATIO * i)
            val right = this.width.toFloat() * (X_PARTITION_RATIO * (i + 1))
            val area = RectF(left + 1f, 1f, right - 1f, this.height.toFloat() - 1f)

            val day = this.mutableWeekCalendar.get(Calendar.DAY_OF_MONTH)

            canvas.drawText(String.format("%02d", day), area.centerX(), area.centerY(), this.dayNumberPaint)
            canvas.drawText(this.mutableWeekCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale), area.centerX(), area.centerY() + (this.dayNumberPaint.textSize / 3f) + 16f, this.dayNamePaint)

            this.mutableWeekCalendar.add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    private fun drawBottomLine(canvas: Canvas) {
        canvas.drawLine(0f, this.height.toFloat(), this.width.toFloat(), this.height.toFloat(), this.linesPaint)
    }

    fun setWeekOfTheYear(weekOfTheYear: Int) {
        this.weekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.mutableWeekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.invalidate()
    }
}
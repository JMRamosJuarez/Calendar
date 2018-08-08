package com.jmrj.calendar.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import java.util.*

class WeekView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val weekCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.MONTH, Calendar.JANUARY)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    private val currentTimeCalendar: Calendar by lazy { Calendar.getInstance() }

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
        p.style = Paint.Style.STROKE
        p.strokeWidth = 2f
        p
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.drawHorizontalLines(canvas)
        this.drawVerticalLines(canvas)
        this.drawCurrentHour(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7) * 32
        setMeasuredDimension(w, h)
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
        canvas.drawLine(this.width / 7f, this.height * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()), this.width.toFloat() - (this.width.toFloat() / 28f), this.height * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()), this.currentHourPaint)
        canvas.drawCircle((this.width / 7f) + 10f, this.height * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()), 10f, this.currentHourCirclePaint)
    }

    private fun getCurrentHourInDecimalFormat(): Float {
        val minutes = this.currentTimeCalendar.get(Calendar.MINUTE) / 100f
        val hours = this.currentTimeCalendar.get(Calendar.HOUR_OF_DAY)
        val result = (minutes * 100f) / 60f
        return hours + result
    }

    fun setWeekOfTheYear(weekOfTheYear: Int) {
        this.weekCalendar.set(Calendar.WEEK_OF_YEAR, weekOfTheYear)
        this.invalidate()
    }
}
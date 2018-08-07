package com.jmrj.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import java.util.*

class DayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

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
        p.color = Color.DKGRAY
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.textAlign = Paint.Align.CENTER
        p.textSize = 24f
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

    private val currentHourCirclePaint: Paint by lazy {
        val p = Paint()
        p.color = Color.BLUE
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p
    }

    private val currentTimeCalendar: Calendar by lazy { Calendar.getInstance() }

    private val Y_PARTITION_RATIO = 1 / 24f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        this.drawHorizontalLines(canvas)
        this.drawCurrentHour(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7) * 24
        setMeasuredDimension(w, h)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        for (i in 1 until 24) {
            canvas.drawLine(this.width / 7f, this.height * (Y_PARTITION_RATIO * i), this.width.toFloat() - (this.width.toFloat() / 28f), this.height * (Y_PARTITION_RATIO * i), this.linesPaint)
            canvas.drawText(String.format("%02d:00", i), this.width / 14f, (this.height * (Y_PARTITION_RATIO * i)) + (this.textPaint.textSize / 3), this.textPaint)
        }
    }

    private fun drawCurrentHour(canvas: Canvas) {
        canvas.drawLine(this.width / 7f, this.height * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()), this.width.toFloat() - (this.width.toFloat() / 28f), this.height * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()), this.currentHourPaint)
        canvas.drawCircle((this.width / 7f) + 10f, this.height * (Y_PARTITION_RATIO * this.getCurrentHourInDecimalFormat()), 10f, this.currentHourCirclePaint)
    }

    private fun drawEvent(startDateInDecimalFormat: Float, endDateInDecimalFormat: Float, paint: Paint, canvas: Canvas) {
        val eventRect = RectF(this.width / 7f,
                this.height * (Y_PARTITION_RATIO * startDateInDecimalFormat),
                this.width - (this.width / 28f),
                this.height * (Y_PARTITION_RATIO * endDateInDecimalFormat))
        canvas.drawRect(eventRect, paint)
    }

    private fun getCurrentHourInDecimalFormat(): Float {
        val minutes = this.currentTimeCalendar.get(Calendar.MINUTE) / 100f
        val hours = this.currentTimeCalendar.get(Calendar.HOUR_OF_DAY)
        val result = (minutes * 100f) / 60f
        return hours + result
    }

    private fun getHourInDecimalFormat(milliseconds: Long): Float {
        val c = Calendar.getInstance()
        c.timeInMillis = milliseconds
        val minutes = c.get(Calendar.MINUTE) / 100f
        val hours = c.get(Calendar.HOUR_OF_DAY)
        val result = (minutes * 100f) / 60f
        return hours + result
    }
}
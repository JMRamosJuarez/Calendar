package com.jmrj.calendar.day

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*

internal class CurrentDayView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val dayCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(Calendar.MONTH, Calendar.SEPTEMBER)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    private val locale: Locale by lazy { Locale.getDefault() }

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

    private val whitePaint: Paint by lazy {
        val p = Paint()
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.style = Paint.Style.FILL
        p.shader = LinearGradient(0f, this.height - (this.height / 7f), 0f, this.width * (1 / 7f), Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP)
        p
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, this.width * (1 / 7f), this.height.toFloat(), this.whitePaint)
        this.drawDay(this.dayCalendar, canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = resolveSize(0, widthMeasureSpec)
        val h = (w / 7)
        setMeasuredDimension(w, h + 12)
    }

    fun setDay(dayOfTheYear: Int) {
        this.dayCalendar.set(Calendar.DAY_OF_YEAR, dayOfTheYear)
        this.invalidate()
    }

    private fun drawDay(currentDayCalendar: Calendar, canvas: Canvas) {
        val y = ((this.height / 4) + (this.dayNumberPaint.textSize / 3)) + 8
        canvas.drawText(String.format("%02d", currentDayCalendar.get(Calendar.DAY_OF_MONTH)), this.width / 14f, y, this.dayNumberPaint)
        canvas.drawText(currentDayCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, this.locale), this.width / 14f, (y + (this.dayNumberPaint.textSize / 3)) + 16, this.dayNamePaint)
    }
}
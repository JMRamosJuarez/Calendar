package com.jmrj.calendar.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DaysOfTheWeekView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

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
        for (i in 0 until 8) {
            //Vertical
            canvas.drawLine(this.width * (X_PARTITION_RATIO * i), 0f, this.width * (X_PARTITION_RATIO * i), this.height.toFloat(), this.linesPaint)
            val left = this.width.toFloat() * (X_PARTITION_RATIO * i)
            val right = this.width.toFloat() * (X_PARTITION_RATIO * (i + 1))
            val area = RectF(left + 1f, 1f, right - 1f, this.height.toFloat() - 1f)
            canvas.drawText(String.format("%02d", i), area.centerX(), area.centerY(), this.dayNumberPaint)
            canvas.drawText("lun.", area.centerX(), area.centerY() + (this.dayNumberPaint.textSize / 3f) + 16f, this.dayNamePaint)
        }
    }

    private fun drawBottomLine(canvas: Canvas) {
        canvas.drawLine(0f, this.height.toFloat(), this.width.toFloat(), this.height.toFloat(), this.linesPaint)
    }
}
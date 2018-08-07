package com.jmrj.calendar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class WeekView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.drawHorizontalLines(canvas)
        this.drawVerticalLines(canvas)
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
}
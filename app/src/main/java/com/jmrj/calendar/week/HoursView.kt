package com.jmrj.calendar.week

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class HoursView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val Y_PARTITION_RATIO = 1 / 24f

    private val textPaint: Paint by lazy {
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
        this.drawHorizontalLines(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val ws = MeasureSpec.getSize(this.resources.displayMetrics.widthPixels)
        val hs = ws * (1 / 8f)
        setMeasuredDimension(hs.toInt(), (hs * 32).toInt())
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        for (i in 1 until 24) {
            //Horizontal
            canvas.drawText(String.format("%02d:00", i), this.width / 2f, (this.height * (Y_PARTITION_RATIO * i)) + (this.textPaint.textSize / 3), this.textPaint)
        }
    }
}
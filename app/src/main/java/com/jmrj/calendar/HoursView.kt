package com.jmrj.calendar

import android.content.Context
import android.util.AttributeSet
import android.view.View

class HoursView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val ws = MeasureSpec.getSize(this.resources.displayMetrics.widthPixels)
        val hs = ws * (1 / 7f)
        setMeasuredDimension(hs.toInt(), (hs * 32).toInt())
    }
}
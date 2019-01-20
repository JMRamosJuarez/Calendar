package com.calendar.core.month

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.core.DateSelectedListener
import com.calendar.core.R
import kotlinx.android.synthetic.main.month_view_fragment_layout.*
import java.util.*

class MonthViewFragment : Fragment(), MonthView.OnDayOfMonthSelectedListener {

    companion object {
        private const val MONTH_OF_THE_YEAR = "MONTH_OF_THE_YEAR"
        fun newInstance(monthOfTheYear: Int): MonthViewFragment {
            val f = MonthViewFragment()
            val arguments = Bundle().apply {
                putInt(MONTH_OF_THE_YEAR, monthOfTheYear)
            }
            f.arguments = arguments
            return f
        }
    }

    private val locale: Locale by lazy { Locale.getDefault() }

    private val month: Int
        get() = this.arguments?.getInt(MONTH_OF_THE_YEAR, 0) ?: 0

    private val monthCalendar: Calendar by lazy {
        val calendar: Calendar = Calendar.getInstance(this.locale)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
        calendar.clear(Calendar.HOUR)
        calendar.clear(Calendar.HOUR_OF_DAY)
        calendar.clear(Calendar.MINUTE)
        calendar.clear(Calendar.SECOND)
        calendar.clear(Calendar.MILLISECOND)
        calendar
    }

    private var onDateSelectedListener: DateSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.month_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.monthCalendar.set(Calendar.MONTH, this.month)
        this.month_view.setMonth(this.month)
        this.month_view.dayOfMonthSelectedListener = this
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = this.parentFragment
        if (parentFragment is DateSelectedListener) {
            this.onDateSelectedListener = parentFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.onDateSelectedListener = null
    }

    override fun onDayOfMonthSelected(dayOfMonth: Int) {
        this.monthCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        this.onDateSelectedListener?.onDateSelected(this.monthCalendar.time)
    }
}
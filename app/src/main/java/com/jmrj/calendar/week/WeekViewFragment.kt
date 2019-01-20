package com.jmrj.calendar.week

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.DateSelectedListener
import com.jmrj.calendar.R
import kotlinx.android.synthetic.main.week_view_layout.*
import java.util.*

class WeekViewFragment : Fragment(), WeekView.OnDayOfWeekSelectedListener {

    companion object {
        private const val WEEK_OF_THE_YEAR = "WEEK_OF_THE_YEAR"
        fun newInstance(weekOfTheYear: Int): WeekViewFragment {
            val f = WeekViewFragment()
            val arguments = Bundle().apply {
                putInt(WEEK_OF_THE_YEAR, weekOfTheYear)
            }
            f.arguments = arguments
            return f
        }
    }

    private var dateSelectedListener: DateSelectedListener? = null

    private val locale: Locale by lazy { Locale.getDefault() }

    private val week: Int
        get() = this.arguments?.getInt(WEEK_OF_THE_YEAR, 0) ?: 0

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedWeek = this.week
        this.weekCalendar.set(Calendar.WEEK_OF_YEAR, selectedWeek)
        Log.i("MONTH_IN_WEEK", this.weekCalendar.time.toString())
        this.days_of_week_view.setWeekOfTheYear(selectedWeek)
        this.week_view.setWeekOfTheYear(selectedWeek)
        this.week_view.dayOfTheWeekSelectedListener = this
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val parentFragment = this.parentFragment
        if (parentFragment is DateSelectedListener) {
            this.dateSelectedListener = parentFragment
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.dateSelectedListener = null
    }

    override fun onDayOfWeekSelected(dayOfWeek: Int) {
        this.weekCalendar.set(Calendar.DAY_OF_WEEK, dayOfWeek)
        this.dateSelectedListener?.onDateSelected(this.weekCalendar.time)
    }
}
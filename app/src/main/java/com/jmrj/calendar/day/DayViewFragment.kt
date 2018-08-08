package com.jmrj.calendar.day

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.R
import kotlinx.android.synthetic.main.day_view_fragment_layout.*

internal class DayViewFragment : Fragment() {

    companion object {
        private const val DAY_OF_THE_YEAR = "DAY_OF_THE_YEAR"
        fun newInstance(dayOfTheYear: Int): DayViewFragment {
            val f = DayViewFragment()
            val arguments = Bundle().apply {
                putInt(DAY_OF_THE_YEAR, dayOfTheYear)
            }
            f.arguments = arguments
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.day_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val day = this.arguments?.getInt(DAY_OF_THE_YEAR, 0) ?: 0
        this.current_day_view.setDay(day)
        this.day_view.setDayOfTheYear(day)
    }
}
package com.jmrj.calendar.week

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.R
import kotlinx.android.synthetic.main.week_view_layout.*

class WeekViewFragment : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val week = this.arguments?.getInt(WEEK_OF_THE_YEAR, 0) ?: 0
        this.days_of_week_view.setWeekOfTheYear(week)
    }
}
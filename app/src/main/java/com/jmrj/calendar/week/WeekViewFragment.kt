package com.jmrj.calendar.week

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.R

class WeekViewFragment : Fragment() {

    companion object {
        private const val WEEK_OF_THE_YEAR = "WEEK_OF_THE_YEAR"
        fun newInstance(monthOfTheYear: Int): WeekViewFragment {
            val f = WeekViewFragment()
            val arguments = Bundle().apply {
                putInt(WEEK_OF_THE_YEAR, monthOfTheYear)
            }
            f.arguments = arguments
            return f
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.week_view_layout, container, false)
    }
}
package com.jmrj.calendar.month

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.R
import kotlinx.android.synthetic.main.month_view_fragment_layout.*

class MonthViewFragment : Fragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.month_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val month = this.arguments?.getInt(MONTH_OF_THE_YEAR, 0) ?: 0
        this.month_view.setMonth(month)
    }
}
package com.jmrj.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.calendar.core.DateSelectedListener
import com.calendar.core.month.MonthsFragmentPagerAdapter
import kotlinx.android.synthetic.main.main_month_view_fragment_layout.*
import java.util.*

class MainMonthViewFragment : Fragment(), DateSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_month_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.months_view_pager.adapter = MonthsFragmentPagerAdapter(this.childFragmentManager)
        this.months_view_pager.currentItem = this.calendar.get(Calendar.MONTH)
    }

    override fun onDateSelected(date: Date) {
        Log.i("DATE_SELECTED", date.toString())
    }
}
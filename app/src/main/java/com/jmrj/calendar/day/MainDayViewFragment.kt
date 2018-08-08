package com.jmrj.calendar.day

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.R
import com.jmrj.calendar.ScrollSynchronizer
import kotlinx.android.synthetic.main.main_day_view_fragment_layout.*
import java.util.*

class MainDayViewFragment : Fragment() {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_day_view_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.days_view_pager.adapter = DaysFragmentPagerAdapter(this.childFragmentManager)
        this.days_view_pager.currentItem = this.calendar.get(Calendar.DAY_OF_YEAR)
    }

    override fun onDetach() {
        super.onDetach()
        ScrollSynchronizer.unRegisterAll()
    }
}
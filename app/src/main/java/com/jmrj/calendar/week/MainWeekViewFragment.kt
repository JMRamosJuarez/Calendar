package com.jmrj.calendar.week

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jmrj.calendar.R
import com.jmrj.calendar.ScrollSynchronizer
import kotlinx.android.synthetic.main.main_week_view_layout.*
import java.util.*

class MainWeekViewFragment : Fragment() {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_week_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.weeks_view_pager.adapter = WeeksFragmentPagerAdapter(this.childFragmentManager)
        this.weeks_view_pager.currentItem = this.calendar.get(Calendar.WEEK_OF_YEAR)
    }

    override fun onDetach() {
        super.onDetach()
        ScrollSynchronizer.unRegisterAll()
    }
}
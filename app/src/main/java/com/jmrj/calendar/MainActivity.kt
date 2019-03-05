package com.jmrj.calendar

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.calendar.core.CalendarEvent
import com.calendar.core.DateSelectedListener
import com.calendar.core.EventSelectedListener
import com.calendar.core.ScrollSynchronizer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), EventSelectedListener, DateSelectedListener {

    private val calendar: Calendar by lazy { Calendar.getInstance(Locale.getDefault()) }

    private val dayViewFragment: MainDayViewFragment
        get() = MainDayViewFragment()

    private val weekViewFragment: MainWeekViewFragment
        get() = MainWeekViewFragment()

    private val monthViewFragment: MainMonthViewFragment
        get() = MainMonthViewFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        replaceFragment(this.monthViewFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_day -> {
                replaceFragment(this.dayViewFragment)
                true
            }
            R.id.action_week -> {
                replaceFragment(this.weekViewFragment)
                true
            }
            R.id.action_month -> {
                replaceFragment(this.monthViewFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDateSelected(date: Date) {
        this.calendar.time = date
        val dayOfTheYear = this.calendar.get(Calendar.DAY_OF_YEAR)
        val dayFragment = MainDayViewFragment()
        dayFragment.arguments = Bundle().apply {
            putInt("DAY_OF_THE_YEAR", dayOfTheYear)
        }
        this.replaceFragment(dayFragment)
    }

    override fun onEventSelected(calendarEvent: CalendarEvent) {
        //Show event detail
    }

    private fun replaceFragment(fragment: Fragment) {
        ScrollSynchronizer.unRegisterAll()
        this.supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit()
    }
}


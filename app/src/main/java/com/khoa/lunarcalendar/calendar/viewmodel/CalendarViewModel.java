package com.khoa.lunarcalendar.calendar.viewmodel;


import android.content.Context;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModel;

import com.khoa.lunarcalendar.calendar.model.DataFragmentMonth;
import com.khoa.lunarcalendar.calendar.model.MyDate;
import com.khoa.lunarcalendar.calendar.model.MyMonth;
import com.khoa.lunarcalendar.calendar.view.adapter.EventViewPagerFragment;
import com.khoa.lunarcalendar.calendar.view.adapter.MonthViewPagerAdapter;

import java.util.Calendar;
import java.util.Date;

public class CalendarViewModel extends ViewModel {

    public MyMonth currentMonth;
    public MyDate currentDate;
    public ObservableField txtMonth;
    public ObservableField txtYear;
    private Context context;

    public MonthViewPagerAdapter adapterViewPagerCalendar;
    public EventViewPagerFragment adapterViewPagerEvent;

    public ObservableInt fabBackVisible;


    public void init(Context context, FragmentManager fm) {
        this.context = context;
        fabBackVisible = new ObservableInt();
        txtMonth = new ObservableField();
        txtYear = new ObservableField();
        adapterViewPagerEvent = new EventViewPagerFragment(context, fm);
        adapterViewPagerCalendar = new MonthViewPagerAdapter(fm);
    }

    // set current month
    public void setCurrentMonth(final int position) {

        // get month is showing
        currentMonth = adapterViewPagerCalendar.getMonth(position);
        updateEventsInMonth();

        txtMonth.set("Tháng " + currentMonth.getMonth());
        txtYear.set(currentMonth.getYear()+"");

        // set visible fab back
        if (currentMonth.getMonth() != (Calendar.getInstance().get(Calendar.MONTH) + 1)) {
            fabBackVisible.set(View.VISIBLE);
        } else {
            fabBackVisible.set(View.INVISIBLE);
        }

        // set current day in current month
        try {
            DataFragmentMonth dataFragmentMonth = adapterViewPagerCalendar.dataFragments.get(position);
            if (dataFragmentMonth == null) setCurrentDay(new MyDate(new Date()));
            else {
                if (dataFragmentMonth.selectedDayIndex != -1) {
                    setCurrentDay(dataFragmentMonth.selectedDay);
                } else if (position == MonthViewPagerAdapter.firstPosition) {
                    setCurrentDay(new MyDate(new Date()));
                } else {
                    setCurrentDay(null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentDay(MyDate myDate) {
        currentDate = myDate;
        updateEventsInDay();
    }

    private void updateEventsInDay() {
        adapterViewPagerEvent.eventInDayFragment.setMonthAndDate(currentMonth, currentDate);
    }

    private void updateEventsInMonth() {
        adapterViewPagerEvent.eventInMonthFragment.setMyMonth(currentMonth);
    }

}
package com.khoa.lunarcalendar.calendar.model;

import java.util.Date;

public class DataFragmentMonth {
    public Date date;
    public MyMonth myMonth;
    public MyDate selectedDay;
    public int selectedDayIndex;

    public DataFragmentMonth(Date date) {
        this.date = date;
        selectedDayIndex = -1;
    }
}

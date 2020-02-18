package com.khoa.lunarcalendar.calendar.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyDate {

    public final static int TIME_ZONE = 7;

    private int dayOfWeek;
    private int day;
    private int month;
    private int year;
    private int lunarDay;
    private int lunarMonth;
    private int lunarYear;
    private ArrayList<MyEvent> eventList;
    private boolean isToday;

    public MyDate(Date date) {
        try {
            // tìm ngày trong tuần
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            this.day = cal.get(Calendar.DAY_OF_MONTH);
            this.month = cal.get(Calendar.MONTH) + 1;
            this.year = cal.get(Calendar.YEAR);
            this.dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            // set chủ nhật là 8
            if(this.dayOfWeek==1) this.dayOfWeek=8;

            // tính ngày âm
            Date lunarDate = LunarCalendar.convertSolar2Lunar(this.day, this.month, this.year, TIME_ZONE);
            cal.setTime(lunarDate);
            this.lunarDay = cal.get(Calendar.DAY_OF_MONTH);
            this.lunarMonth = cal.get(Calendar.MONTH) + 1;
            this.lunarYear = cal.get(Calendar.YEAR);

            // set isToday
            Date today = new Date();
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String strToday = df.format(today);
            String strDate = df.format(date);
            if (strToday.equals(strDate)) this.isToday = true;
            else this.isToday = false;

            // set arraylist event id
            this.eventList = new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyDate(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;

        // set arraylist event id
        this.eventList = new ArrayList<>();

        try {
            // tìm ngày trong tuần
            String strDate = day + "/" + month + "/" + year;
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Date date = df.parse(strDate);
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            cal.setTime(date);
            this.dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            // set chủ nhật là 8
            if(this.dayOfWeek==1) this.dayOfWeek=8;

            /*
            // tính ngày âm
            Date lunarDate = LunarCalendar.convertSolar2Lunar(this.day, this.month, this.year, TIME_ZONE);
            cal.setTime(lunarDate);
            this.lunarDay = cal.get(Calendar.DAY_OF_MONTH);
            this.lunarMonth = cal.get(Calendar.MONTH) + 1;
            this.lunarYear = cal.get(Calendar.YEAR);

            // set isToday
            Date today = new Date();
            String strToday = df.format(today);
            strDate = df.format(date);
            if (strToday.equals(strDate)) {
                this.isToday = true;
            }
            else this.isToday = false;
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setInfoForMyDate(){
        // tính ngày âm
        Date lunarDate = LunarCalendar.convertSolar2Lunar(day, month, year, TIME_ZONE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(lunarDate);
        lunarDay = cal.get(Calendar.DAY_OF_MONTH);
        lunarMonth = cal.get(Calendar.MONTH) + 1;
        lunarYear = cal.get(Calendar.YEAR);

        // set isToday
        String strDate = String.format("%02d/%02d/%04d", day, month, year);
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String strToday = df.format(today);
        if (strToday.equals(strDate)) {
            isToday = true;
        }
        else isToday = false;
    }

    public MyDate() {
    }

    public Date toDate() {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse(day + "/" + month + "/" + year);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return dayOfWeek + " : " + day + "/" + month + "/" + year + " - " + lunarDay + "/" + lunarMonth + "/" + lunarYear;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getLunarDay() {
        return lunarDay;
    }

    public void setLunarDay(int lunarDay) {
        this.lunarDay = lunarDay;
    }

    public int getLunarMonth() {
        return lunarMonth;
    }

    public void setLunarMonth(int lunarMonth) {
        this.lunarMonth = lunarMonth;
    }

    public int getLunarYear() {
        return lunarYear;
    }

    public void setLunarYear(int lunarYear) {
        this.lunarYear = lunarYear;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public ArrayList<MyEvent> getEventList() {
        return eventList;
    }

    public void setEventList(ArrayList<MyEvent> eventList) {
        this.eventList = eventList;
    }
}

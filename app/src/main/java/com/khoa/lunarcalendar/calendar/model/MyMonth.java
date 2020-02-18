package com.khoa.lunarcalendar.calendar.model;

import java.util.ArrayList;
import java.util.Calendar;

public class MyMonth {

    private int year;
    private int month;
    private ArrayList<MyDate> dateList;
    private ArrayList<MyEvent> myEventList;

    public MyMonth(int year, int month, ArrayList<MyDate> dateList) {
        this.year = year;
        this.month = month;
        this.dateList = dateList;
        this.myEventList = new ArrayList<>();
    }

    public MyMonth(int year, int month) {
        dateList = new ArrayList<>();
        this.myEventList = new ArrayList<>();
        this.month = month;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public void addDay(){

        if(dateList.isEmpty()) return;

        try {
            Calendar calendar = Calendar.getInstance();
            // thêm các ngày ở đầu tháng
            calendar.setTime(dateList.get(0).toDate());
            int firstDay = dateList.get(0).getDayOfWeek();
            for (int i = 2; i < firstDay; i++) {
//                calendar.add(Calendar.DATE, -1);
//                Date date = calendar.getTime();
//                list.add(0, new MyDate(date));
                dateList.add(0, null);
            }

            // thêm các ngày ở cuối tháng
            calendar.setTime(dateList.get(dateList.size() - 1).toDate());
            int lastDay = dateList.get(dateList.size() - 1).getDayOfWeek();
            for (int i = lastDay + 1; i <= 8; i++) {
//                calendar.add(Calendar.DATE, 1);
//                Date date = calendar.getTime();
//                list.add(new MyDate(date));
                dateList.add(null);
            }

//            Log.e("Loi", "thang: " + month + ", start: " + firstDay + ", end: " + lastDay);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public ArrayList<MyDate> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<MyDate> dateList) {
        this.dateList = dateList;
    }

    public ArrayList<MyEvent> getMyEventList() {
        return myEventList;
    }

    public void setMyEventList(ArrayList<MyEvent> myEventList) {
        this.myEventList = myEventList;
    }
}

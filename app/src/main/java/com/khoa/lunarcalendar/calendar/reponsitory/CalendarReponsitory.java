package com.khoa.lunarcalendar.calendar.reponsitory;

import android.content.Context;

import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.model.MyMonth;
import com.khoa.lunarcalendar.calendar.reponsitory.dao.DatabaseAccess;

import java.util.ArrayList;

public class CalendarReponsitory {

    public static String DELETE_EVENT = "delete_event";
    public static String UPDATE_EVENT = "update_event";
    public static String ADD_EVENT = "add_event";

    private DatabaseAccess databaseAccess;

    public CalendarReponsitory(Context context) {
        databaseAccess = DatabaseAccess.getInstance(context);
    }

    public ArrayList<MyEvent> getAllEvent(){
        return databaseAccess.getAllEvent();
    }

    public MyEvent getEvent(int id){
        return databaseAccess.getEvent(id);
    }

    public void insertEventAnnual(MyEvent myEvent){
        databaseAccess.insertEventAnnual(myEvent);
    }

    public boolean deleteEvent(int id){
        return databaseAccess.deleteEvent(id);
    }

    public ArrayList<MyEvent> getEventInMonth(MyMonth month){
        return databaseAccess.getEventInMonth(month);
    }
}

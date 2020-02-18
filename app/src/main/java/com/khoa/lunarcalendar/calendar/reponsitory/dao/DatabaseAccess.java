package com.khoa.lunarcalendar.calendar.reponsitory.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.khoa.lunarcalendar.calendar.model.LunarCalendar;
import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.model.MyMonth;
import com.khoa.lunarcalendar.calendar.model.SpecialEvent;
import com.khoa.lunarcalendar.calendar.ultis.CalendarUltis;

import java.util.ArrayList;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the databases connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the databases connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void deleteSpecialEvent(int id) {
        open();
        database.execSQL("Delete From 'event_special' Where id=" + id);
        close();
    }

    public boolean deleteEvent(int id) {
        open();
        return database.delete("event_annual", "id=" + id, null) > 0;
    }

    public void insertEventAnnual(MyEvent event) {
        open();
        ContentValues values = new ContentValues();
//        values.put("id", event.id);
        values.put("calendar", event.getCalendar());
        values.put("title", event.getTitle());
        values.put("image_url", event.getImageURL());
        values.put("start_date", event.getStartDate());
        values.put("end_date", event.getEndDate());
        values.put("content", event.getContent());
        values.put("created_at", event.getCreatedAt());
        values.put("updated_at", event.getUpdateAt());
        database.insert("event_annual", null, values);
        close();
    }

    public ArrayList<MyEvent> getAllEvent() {
        ArrayList<MyEvent> list = new ArrayList<>();
        open();
        Cursor cursor = database.rawQuery("SELECT * FROM 'event_annual'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String calendar = cursor.getString(1);
            String titile = cursor.getString(2);
            String imageURL = cursor.getString(3);
            String startDate = cursor.getString(4);
            String endDate = cursor.getString(5);
            String content = cursor.getString(6);
            String createdAt = cursor.getString(7);
            String updateAt = cursor.getString(8);
            list.add(new MyEvent(id, calendar, titile, imageURL, startDate, endDate, content, createdAt, updateAt));
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return list;
    }

    public MyEvent getEvent(int eventId) {
        MyEvent event = null;
        open();
        Cursor cursor = database.rawQuery("SELECT * FROM 'event_annual' where id=" + eventId, null);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String calendar = cursor.getString(1);
            String titile = cursor.getString(2);
            String imageURL = cursor.getString(3);
            String startDate = cursor.getString(4);
            String endDate = cursor.getString(5);
            String content = cursor.getString(6);
            String createdAt = cursor.getString(7);
            String updateAt = cursor.getString(8);
            event = new MyEvent(id, calendar, titile, imageURL, startDate, endDate, content, createdAt, updateAt);
        }
        cursor.close();
        close();
        return event;
    }

    public ArrayList<SpecialEvent> getAllSpecialEvent() {
        ArrayList<SpecialEvent> list = new ArrayList<>();
        open();
        Cursor cursor = database.rawQuery("SELECT * FROM 'event_special'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(0);
            String calendar = cursor.getString(1);
            String titile = cursor.getString(2);
            String imageURL = cursor.getString(3);
            String date = cursor.getString(4);
            String content = cursor.getString(5);
            String createdAt = cursor.getString(6);
            String updateAt = cursor.getString(7);
            list.add(new SpecialEvent(id, calendar, titile, imageURL, date, content, createdAt, updateAt));
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return list;
    }

    public ArrayList<MyEvent> getEventInMonth(MyMonth month) {
        ArrayList<MyEvent> list = new ArrayList<>();
        try {
            open();
            Cursor cursor = database.rawQuery("SELECT * FROM 'event_annual' " +
                    "where calendar='lunar' " +
                    "or (calendar='solar' AND (cast(strftime('%m', start_date) as integer) = " + month.getMonth()
                    + " or cast(strftime('%m', end_date) as integer) = " + month.getMonth() + "))", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(0);
                String calendar = cursor.getString(1);
                String titile = cursor.getString(2);
                String imageURL = cursor.getString(3);
                String startDate = cursor.getString(4);
                String endDate = cursor.getString(5);
                String content = cursor.getString(6);
                String createdAt = cursor.getString(7);
                String updateAt = cursor.getString(8);

                MyEvent myEvent = new MyEvent(id, calendar, titile, imageURL, startDate, endDate, content, createdAt, updateAt);
                myEvent.setYear(month.getYear());

                if (calendar.equals("lunar")) {
                    int[] startTime = CalendarUltis.parseTime(startDate);
                    int[] endTime = CalendarUltis.parseTime(endDate);

                    int[] startDMY = LunarCalendar.convertLunar2Solar(startTime[2], startTime[1], month.getYear());
                    int[] endDMY = LunarCalendar.convertLunar2Solar(endTime[2], endTime[1], month.getYear());

                    if (startDMY[1] == month.getMonth() || endDMY[1] == month.getMonth()) {
                        list.add(myEvent);
                    }
                } else {
                    list.add(myEvent);
                }
                cursor.moveToNext();
            }
            cursor.close();
            close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}
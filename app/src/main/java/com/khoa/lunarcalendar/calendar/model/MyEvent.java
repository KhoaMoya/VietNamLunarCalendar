package com.khoa.lunarcalendar.calendar.model;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEvent implements Serializable {
    private int id;
    private String calendar;
    private String title;
    private String imageURL;
    private String startDate;
    private String endDate;
    private String content;
    private String createdAt;
    private String updateAt;
    private int year;

    public MyEvent(int id, String calendar, String title, String imageURL, String startDate, String endDate, String content, String createdAt, String updateAt) {
        this.id = id;
        this.calendar = calendar;
        this.title = title;
        this.imageURL = imageURL;
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
        this.createdAt = createdAt;
        this.updateAt = updateAt;

    }

    @SuppressLint("DefaultLocale")
    public boolean isTakesPlaceInDate(MyDate myDate) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dfMonthDay = new SimpleDateFormat("MM-dd HH:mm:ss");
            String strSolarDate = String.format("%02d-%02d 00:00:00", myDate.getMonth(), myDate.getDay());
            String strLunarDate = String.format("%02d-%02d 00:00:00", myDate.getLunarMonth(), myDate.getLunarDay());

            // diễn ra trong 1 ngày
            if (startDate.equals(endDate)) {
                Date dateHappen = df.parse(this.startDate);
                String strDateHappen = dfMonthDay.format(dateHappen);

                // check solar day
                if (calendar.equals("solar") && strDateHappen.compareTo(strSolarDate) == 0)
                    return true;
                // check lunar day
                if (calendar.equals("lunar") && strDateHappen.compareTo(strLunarDate) == 0)
                    return true;
            }
            // diễn ra trong nhiều ngày
            else {
                Date startDate = df.parse(this.startDate);
                Date endDate = df.parse(this.endDate);

                String strStartDate = dfMonthDay.format(startDate);
                String strEndDate = dfMonthDay.format(endDate);

                // check solar day
                if (calendar.equals("solar")) {
                    if (strSolarDate.compareTo(strStartDate) > 0 && strSolarDate.compareTo(strEndDate) < 0)
                        return true;
                }
                // check lunar day
                if (calendar.equals("lunar")) {
                    if (strLunarDate.compareTo(strStartDate) > 0 && strLunarDate.compareTo(strEndDate) < 0)
                        return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return id + "," + calendar + ", " + title + ", " + startDate + ", " + endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}

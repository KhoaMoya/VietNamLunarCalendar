package com.khoa.lunarcalendar.calendar.model;

import androidx.annotation.NonNull;

public class SpecialEvent {
    public int id;
    public String calendar;
    public String title;
    public String imageURL;
    public String date;
    public String content;
    public String createdAt;
    public String updateAt;

    public SpecialEvent(int id, String calendar, String title, String imageURL, String date, String content, String createdAt, String updateAt) {
        this.id = id;
        this.calendar = calendar;
        this.title = title;
        this.imageURL = imageURL;
        this.date = date;
        this.content = content;
        this.createdAt = createdAt;
        this.updateAt = updateAt;
    }

    @NonNull
    @Override
    public String toString() {
        return id + "," + calendar + ", " + title;
    }
}

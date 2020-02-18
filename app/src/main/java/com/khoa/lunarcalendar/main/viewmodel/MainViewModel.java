package com.khoa.lunarcalendar.main.viewmodel;

import androidx.lifecycle.ViewModel;

import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.view.fragment.CalendarFragment;
import com.khoa.lunarcalendar.calendar.view.fragment.DetailEventFragment;

public class MainViewModel extends ViewModel {

    public int EDIT_EVENT_REQUEST_CODE = 1;

    private static CalendarFragment calendarFragment;
    private DetailEventFragment detailEventFragment;

    public CalendarFragment getCalendarFragment(){
        if(calendarFragment==null) calendarFragment = new CalendarFragment();
        return calendarFragment;
    }

    public DetailEventFragment createDetailEventFragment(MyEvent myEvent){
        this.detailEventFragment = new DetailEventFragment(myEvent);
        return detailEventFragment;
    }

    public DetailEventFragment getDetailEventFragment(){
        return detailEventFragment;
    }

}

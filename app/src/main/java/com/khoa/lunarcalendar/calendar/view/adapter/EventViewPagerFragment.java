package com.khoa.lunarcalendar.calendar.view.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.khoa.lunarcalendar.calendar.view.fragment.EventInDayFragment;
import com.khoa.lunarcalendar.calendar.view.fragment.EventInMonthFragment;

public class EventViewPagerFragment extends FragmentStatePagerAdapter {

    private Context context;
    public EventInDayFragment eventInDayFragment;
    public EventInMonthFragment eventInMonthFragment;

    public EventViewPagerFragment(Context context, @NonNull FragmentManager fm) {
        super(fm);
        this.eventInDayFragment = new EventInDayFragment(context);
        this.eventInMonthFragment = new EventInMonthFragment(context);

        this.context = context;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        // Event in day
        if (position == 0) {
            return eventInDayFragment;
        }
        // event in month
        else if (position == 1) {
            return eventInMonthFragment;
        } else {
            return new EventInMonthFragment(context);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

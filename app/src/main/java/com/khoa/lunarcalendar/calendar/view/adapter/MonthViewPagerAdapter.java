package com.khoa.lunarcalendar.calendar.view.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.khoa.lunarcalendar.calendar.model.DataFragmentMonth;
import com.khoa.lunarcalendar.calendar.model.MyMonth;
import com.khoa.lunarcalendar.calendar.view.fragment.MonthFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class MonthViewPagerAdapter extends FragmentStatePagerAdapter {

    public final static int firstPosition = 50;
    public HashMap<Integer, DataFragmentMonth> dataFragments;

    public MonthViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
        dataFragments = new HashMap<>();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(dataFragments.containsKey(position)){
            return MonthFragment.newInstance(dataFragments.get(position));
        } else {
            int monthPosition = position - firstPosition;
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, monthPosition);
            Date newDate = calendar.getTime();
            DataFragmentMonth data = new DataFragmentMonth(newDate);
            MonthFragment fragment = MonthFragment.newInstance(data);
            dataFragments.put(position, data);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 100;
    }

    public MyMonth getMonth(int position){
        int monthPosition = position - firstPosition;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, monthPosition);
        int month = cal.get(Calendar.MONTH)+1;
        int year = cal.get(Calendar.YEAR);
        return new MyMonth(year, month);
    }

}

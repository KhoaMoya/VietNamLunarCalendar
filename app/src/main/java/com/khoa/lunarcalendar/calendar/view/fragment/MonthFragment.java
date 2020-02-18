package com.khoa.lunarcalendar.calendar.view.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.calendar.model.DataFragmentMonth;
import com.khoa.lunarcalendar.calendar.model.MessageEvent;
import com.khoa.lunarcalendar.calendar.model.MyDate;
import com.khoa.lunarcalendar.calendar.ultis.CalendarUltis;
import com.khoa.lunarcalendar.databinding.MonthFragmentBinding;

import org.greenrobot.eventbus.EventBus;

public class MonthFragment extends Fragment {

    private MonthFragmentBinding mBinding;
    private DataFragmentMonth dataFragment;


    public MonthFragment(DataFragmentMonth dataFragment) {
        this.dataFragment = dataFragment;
    }

    public static MonthFragment newInstance(DataFragmentMonth dataFragment) {
        return new MonthFragment(dataFragment);
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.month_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mBinding.layoutDay.setVisibility(View.INVISIBLE);
        new UpdateCalendarAsyncTask().execute();
    }

    @SuppressLint({"WrongThread"})
    private class UpdateCalendarAsyncTask extends AsyncTask<Void, LinearLayout, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                if (dataFragment.myMonth == null) {
                    int month = dataFragment.date.getMonth() + 1;
                    int year = dataFragment.date.getYear() + 1900;
                    dataFragment.myMonth = CalendarUltis.getMonth(month, year);
                }
                int index = 0;
                while (index < dataFragment.myMonth.getDateList().size()) {
                    LinearLayout weekLayout = new LinearLayout(getContext());
//                int height = mBinding.layoutDay.getHeight() / (myMonth.getDateList().size()/7);
//                int width = mBinding.layoutDay.getWidth();
                    LinearLayout.LayoutParams weekLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
                    weekLayoutParams.weight = 1;
                    weekLayout.setLayoutParams(weekLayoutParams);
                    weekLayout.setOrientation(LinearLayout.HORIZONTAL);

                    for (int i = 0; i < 7; i++) {
                        MyDate date = dataFragment.myMonth.getDateList().get(index);
                        View dayView = createDayView(weekLayout, date);
                        dayView.post(setLunarRunnable(dayView, index));
                        weekLayout.addView(dayView);
                        index++;
                    }
                    publishProgress(weekLayout);
                }
                publishProgress(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(LinearLayout... values) {
            super.onProgressUpdate(values);
            if (values != null) {
                mBinding.layoutDay.addView(values[0]);
            } else {
                mBinding.layoutDay.setVisibility(View.VISIBLE);
            }
        }

    }

    private View createDayView(ViewGroup container, MyDate date) {
        View dayView = null;
        if (date == null) {
            dayView = LayoutInflater.from(getContext()).inflate(R.layout.item_day_empty_calendar, container, false);
        } else {
            dayView = LayoutInflater.from(getContext()).inflate(R.layout.item_day_calendar, container, false);
        }
        return dayView;
    }

    private Runnable setLunarRunnable(final View dayView, final int index) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    TextView solarDay = dayView.findViewById(R.id.txt_solar_day);
                    if (solarDay == null) return;
                    TextView lunarDay = dayView.findViewById(R.id.txt_lunar_day);

                    if(dataFragment.myMonth.getDateList().get(index).getLunarDay()==0){
                        dataFragment.myMonth.getDateList().get(index).setInfoForMyDate();
                    }

                    final MyDate myDate =  dataFragment.myMonth.getDateList().get(index);

                    solarDay.setText(myDate.getDay() + "");

                    if (myDate.getLunarDay() == 1) {
                        lunarDay.setText(myDate.getLunarDay() + "/" + myDate.getLunarMonth());
                    } else {
                        lunarDay.setText(myDate.getLunarDay() + "");
                    }

                    // đổi mày ngày chủ nhật
//            if (date.getDayOfWeek() == 8) {
//                solarDay.setTextColor(getResources().getColor(R.color.colorRed));
//                lunarDay.setTextColor(getResources().getColor(R.color.colorRed50));
//            }

                    // đổi background ngày đã chon
                    if(dataFragment.selectedDayIndex!=-1){
                        if(index == dataFragment.selectedDayIndex){
                            dayView.setBackground(getResources().getDrawable(R.drawable.background_selected_day_calendar));
                        }
                    }

                    // đổi màu ngày hôm nay
                    if (myDate.isToday()) {
                        dayView.setBackground(getResources().getDrawable(R.drawable.background_today_calendar));
                        dayView.setElevation(10f);
                        solarDay.setTextColor(getResources().getColor(R.color.colorWhite));
                        lunarDay.setTextColor(getResources().getColor(R.color.colorWhite));
                    }

                    dayView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clearClick();
                            if(myDate.isToday()){
                                dataFragment.selectedDayIndex = -1;
                                dataFragment.selectedDay = null;
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.DAY_CLICK, myDate));
                            }
                            else {
                                // huỷ chọn
                                if (index == dataFragment.selectedDayIndex) {
                                    dayView.setBackground(getResources().getDrawable(R.drawable.background_day_calendar));
                                    dataFragment.selectedDay = null;
                                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.DAY_CLICK, null));
                                    dataFragment.selectedDayIndex = -1;
                                }
                                // chọn mới
                                else {
                                    dayView.setBackground(getResources().getDrawable(R.drawable.background_selected_day_calendar));
                                    dataFragment.selectedDay = myDate;
                                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.DAY_CLICK, myDate));
                                    dataFragment.selectedDayIndex = index;
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void clearClick() {
        if(dataFragment.selectedDayIndex!=-1) {
            int weekIndex = dataFragment.selectedDayIndex / 7;
            int dayIndex = dataFragment.selectedDayIndex % 7;
            LinearLayout weekView = (LinearLayout) mBinding.layoutDay.getChildAt(weekIndex);
            View dayView = weekView.getChildAt(dayIndex);
            dayView.setBackground(getResources().getDrawable(R.drawable.background_day_calendar));
        }
    }
}
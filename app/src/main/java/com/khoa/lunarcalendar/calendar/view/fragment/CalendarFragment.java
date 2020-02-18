package com.khoa.lunarcalendar.calendar.view.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.ViewPagerBottomSheetBehavior;
import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.calendar.model.MessageEvent;
import com.khoa.lunarcalendar.calendar.model.MyDate;
import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.model.SpecialEvent;
import com.khoa.lunarcalendar.calendar.reponsitory.dao.DatabaseAccess;
import com.khoa.lunarcalendar.calendar.ultis.ZoomOutPageTransformer;
import com.khoa.lunarcalendar.calendar.view.adapter.MonthViewPagerAdapter;
import com.khoa.lunarcalendar.calendar.viewmodel.CalendarViewModel;
import com.khoa.lunarcalendar.databinding.CalendarFragmentBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class CalendarFragment extends Fragment {

    private CalendarFragmentBinding mBinding;
    private CalendarViewModel mViewModel;


    public static CalendarFragment newInstance() {
        return new CalendarFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.calendar_fragment, container, false);
        mViewModel = ViewModelProviders.of(this).get(CalendarViewModel.class);
        if (savedInstanceState == null) mViewModel.init(getContext(), getChildFragmentManager());
        mBinding.setViewmodel(mViewModel);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupBinding();
        setUpViewPagerCalendar();
        setupViewPagerBottomSheet();

        mViewModel.setCurrentMonth(MonthViewPagerAdapter.firstPosition);
    }

    private void setupBinding() {
        mBinding.fabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.viewpagerCalendar.setCurrentItem(MonthViewPagerAdapter.firstPosition, true);
            }
        });

        mBinding.bottomSheetEvent.txtEventMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.bottomSheetEvent.viewpagerEvent.setCurrentItem(1);
            }
        });

        mBinding.bottomSheetEvent.txtEventDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBinding.bottomSheetEvent.viewpagerEvent.setCurrentItem(0);
            }
        });

    }

    private void setupViewPagerBottomSheet() {

        // set peek height bottom sheet && height bottom sheet
        final ViewPagerBottomSheetBehavior<View> layoutBehavior = ViewPagerBottomSheetBehavior.from((View) mBinding.bottomSheetEvent.bottomsheetLayout);

        mBinding.fabBack.post(new Runnable() {
            @Override
            public void run() {
                // set peek height bottom sheet
                int bottomRoot = mBinding.rootLayout.getBottom();
                int bottomFabBack = mBinding.fabBack.getBottom();

                int peekHeight = bottomRoot - bottomFabBack - 15;
                layoutBehavior.setPeekHeight(peekHeight, true);
                layoutBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                // set height bottom sheet
                int topCalendar = mBinding.viewpagerCalendar.getTop();
                int heightSheet = bottomRoot - topCalendar;
                ViewGroup.LayoutParams params = mBinding.bottomSheetEvent.bottomsheetLayout.getLayoutParams();
                params.height = heightSheet;
                mBinding.bottomSheetEvent.bottomsheetLayout.setLayoutParams(params);
            }
        });

        // set adapter for view pager event
        mBinding.bottomSheetEvent.viewpagerEvent.setAdapter(mViewModel.adapterViewPagerEvent);

        // set scrolling child for viewpager
        mBinding.bottomSheetEvent.viewpagerEvent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBinding.bottomSheetEvent.viewpagerEvent.post(new Runnable() {
                    @Override
                    public void run() {
                        layoutBehavior.updateScrollingChild();
                    }
                });

                if (position == 0) {
                    mBinding.bottomSheetEvent.txtEventDay.setTextColor(getResources().getColor(R.color.colorBlack));
                    mBinding.bottomSheetEvent.txtEventMonth.setTextColor(getResources().getColor(R.color.colorBlack20));
                } else if (position == 1) {
                    mBinding.bottomSheetEvent.txtEventDay.setTextColor(getResources().getColor(R.color.colorBlack20));
                    mBinding.bottomSheetEvent.txtEventMonth.setTextColor(getResources().getColor(R.color.colorBlack));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // viewpager calendar
    private void setUpViewPagerCalendar() {
//        mBinding.viewpagerCalendar.setClipToPadding(false);
//        mBinding.viewpagerCalendar.setPadding(10,0,10,0);
//        mBinding.viewpagerCalendar.setPageMargin(10);
        mBinding.viewpagerCalendar.setOffscreenPageLimit(2);
        mBinding.viewpagerCalendar.setAdapter(mViewModel.adapterViewPagerCalendar);

        mBinding.viewpagerCalendar.setCurrentItem(MonthViewPagerAdapter.firstPosition, false);


        // page transformer
        mBinding.viewpagerCalendar.setPageTransformer(true, new ZoomOutPageTransformer());

        mBinding.viewpagerCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mViewModel.setCurrentMonth(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickFabAddEvent();
            }
        });
    }

    public boolean isExpandedBottomSheet() {
        final ViewPagerBottomSheetBehavior<View> layoutBehavior = ViewPagerBottomSheetBehavior.from((View) mBinding.bottomSheetEvent.bottomsheetLayout);
        return layoutBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED;
    }

    public void closeBottomSheet() {
        final ViewPagerBottomSheetBehavior<View> layoutBehavior = ViewPagerBottomSheetBehavior.from((View) mBinding.bottomSheetEvent.bottomsheetLayout);
        layoutBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Subscribe
    public void onListenerMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.action) {
            // day click listener
            case DAY_CLICK:
                MyDate clickedDate = (MyDate) messageEvent.data;
                mViewModel.setCurrentDay(clickedDate);
                if (mBinding.bottomSheetEvent.viewpagerEvent.getCurrentItem() == 1) {
                    mBinding.bottomSheetEvent.viewpagerEvent.setCurrentItem(0);
                }
                break;
            // recycler view event scroll listenser
            case SCROLL_EVENT_LIST:
                int visibly = (int) messageEvent.data;
                if (visibly == View.VISIBLE) {
                    mBinding.fabAddEvent.show();
                } else {
                    mBinding.fabAddEvent.hide();
                }
                break;
            case UPDATE_TEXT_EVENT_LIST_DAY_SIZE:
                mBinding.bottomSheetEvent.txtEventDay.setText("Ngày (" + messageEvent.data + ")");
                break;
            case UPDATE_TEXT_EVENT_LIST_MONTH_SIZE:
                mBinding.bottomSheetEvent.txtEventMonth.setText("Tháng (" + messageEvent.data + ")");
                break;

            default:
                break;
        }
    }

    private void clickFabAddEvent() {
        fadeOutFabAddEvent();
        mBinding.reveal.setVisibility(VISIBLE);

        int cx = mBinding.reveal.getWidth();
        int cy = mBinding.reveal.getHeight();


        int x = (int) (getFabWidth() / 2 + mBinding.fabAddEvent.getX());
        int y = (int) (getFabWidth() / 2 + mBinding.fabAddEvent.getY());

        float finalRadius = Math.max(cx, cy) * 1.3f;

        Animator reveal = ViewAnimationUtils
                .createCircularReveal(mBinding.reveal, x, y, getFabWidth(), finalRadius);

        reveal.setDuration(400);
        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                reset(animation);
//                finish();
            }

            private void reset(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.reveal.setVisibility(INVISIBLE);
                mBinding.fabAddEvent.setAlpha(1f);
                mBinding.calendarLayout.setVisibility(VISIBLE);
                mBinding.bottomSheetEvent.bottomsheetLayout.setVisibility(VISIBLE);
            }
        });

        reveal.start();
        delayedStartNextActivity();
    }

    private int getFabWidth() {
        return (int) getResources().getDimension(R.dimen.fab_size);
    }

    private void fadeOutFabAddEvent() {
        mBinding.fabAddEvent.animate().alpha(0f).setDuration(100).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBinding.calendarLayout.setVisibility(INVISIBLE);
                mBinding.bottomSheetEvent.bottomsheetLayout.setVisibility(INVISIBLE);
            }
        }).start();
    }
    private void delayedStartNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.SHOW_ADD_EVENT_ACTIVITY, ""));
            }
        }, 100);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void testDatabase() {
        try {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
            ArrayList<MyEvent> eventList = databaseAccess.getAllEvent();
            ArrayList<SpecialEvent> specialEventList = databaseAccess.getAllSpecialEvent();

//            for(MyEvent event : eventList) Log.e("Loi", event.toString());
            Log.e("Loi", specialEventList.size() + "");

//            for(int i=0; i<eventList.size(); i++){
//                for (int j=0; j<specialEventList.size(); j++){
//                    String str1 = specialEventList.get(j).titile.toLowerCase();
//                    String str2 = eventList.get(i).titile.toLowerCase();
//                    if(str1.equalsIgnoreCase(str2) || str1.contains(str2) || str2.contains(str1)){
//                        Log.e("Loi", eventList.get(i).titile);
//                        databaseAccess.deleteSpecialEvent(specialEventList.get(j).id);
//                    }
//                }
//            }
//              14, 19, 26,38
//            databaseAccess.deleteSpecialEvent(14);
//            databaseAccess.deleteSpecialEvent(19);
//            databaseAccess.deleteSpecialEvent(26);
//            databaseAccess.deleteSpecialEvent(38);

            Log.e("Loi", eventList.size() + "");
            for (int i = 0; i < specialEventList.size(); i++) {
                SpecialEvent event = specialEventList.get(i);
                String newDate = event.date;
                String[] splits = newDate.split("/");
                newDate = "1970-" + splits[1] + "-" + splits[0] + " 00:00:00";
//                Log.e("Loi", newDate + " , " + specialEventList.get(i).toString());
//int id, String calendar, String title, String imageURL, String startDate, String endDate, String content, String createdAt, String updateAt
                MyEvent myEvent = new MyEvent(event.id, event.calendar, event.title, event.imageURL, newDate, newDate, event.content, event.createdAt, event.updateAt);
                databaseAccess.insertEventAnnual(myEvent);
            }
            eventList = databaseAccess.getAllEvent();
            Log.e("Loi", eventList.size() + "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
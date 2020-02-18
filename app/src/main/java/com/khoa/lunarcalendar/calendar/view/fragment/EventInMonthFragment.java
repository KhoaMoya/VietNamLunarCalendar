package com.khoa.lunarcalendar.calendar.view.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.calendar.model.MessageEvent;
import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.model.MyMonth;
import com.khoa.lunarcalendar.calendar.reponsitory.CalendarReponsitory;
import com.khoa.lunarcalendar.calendar.view.adapter.EventRecyclerViewApdapter;
import com.khoa.lunarcalendar.databinding.EventFragmentBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class EventInMonthFragment extends Fragment {

    private EventFragmentBinding mBinding;
    private EventRecyclerViewApdapter mAdapterEvent;
    private MyMonth myMonth;
    private ArrayList<MyEvent> eventList;
    private Context context;

    public EventInMonthFragment(Context context) {
        this.context = context;
        this.mAdapterEvent = new EventRecyclerViewApdapter(context);
        this.eventList = new ArrayList<>();
    }

    public void setMyMonth(MyMonth myMonth) {
        this.myMonth = myMonth;
        new AsyntaskLoadEvent().execute();
    }

    public int getSizeEventList() {
        return eventList.size();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.event_fragment, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupBinding();
    }

    private void setupBinding() {

        mBinding.recyclerviewEvent.setLayoutManager(new LinearLayoutManager(context));
        mBinding.recyclerviewEvent.setHasFixedSize(true);
        mBinding.recyclerviewEvent.setAdapter(mAdapterEvent);

        mBinding.recyclerviewEvent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                // show fab add event
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.SCROLL_EVENT_LIST, new Integer(View.VISIBLE)));
                }
                // hide fab add event
                else {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.SCROLL_EVENT_LIST, new Integer(View.INVISIBLE)));
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void updateEventList(ArrayList<MyEvent> myEvents) {
        if (myEvents == null) {
            showMessage("Có sự cố");
            mAdapterEvent.setEventList(new ArrayList<MyEvent>());
        } else {
            if (myEvents.isEmpty()) {
                showMessage("Không có sự kiện");
            } else {
                mBinding.txtMessage.setVisibility(View.INVISIBLE);
                mAdapterEvent.setEventList(myEvents);
            }
        }

        updateTextSizeEventList();
    }

    private void showMessage(String message) {
        mBinding.txtMessage.setVisibility(View.VISIBLE);
        mBinding.txtMessage.setText(message);
//        mBinding.txtMessage.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_appear));
    }

    private void updateTextSizeEventList(){
        EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.UPDATE_TEXT_EVENT_LIST_MONTH_SIZE, eventList.size()));
    }

    public class AsyntaskLoadEvent extends AsyncTask<Void, ArrayList<MyEvent>, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ArrayList<MyEvent> list = new CalendarReponsitory(context).getEventInMonth(myMonth);
            eventList = list;
            publishProgress(list);
            return null;
        }

        @Override
        protected final void onProgressUpdate(ArrayList<MyEvent>... values) {
            if(values!=null) {
                updateEventList(values[0]);
            } else {
                updateEventList(null);
            }
            super.onProgressUpdate(values);
        }
    }

    @Subscribe
    public void onListenerMessageEvent(MessageEvent messageEvent){
        switch (messageEvent.action){
            case DELETED_EVENT:
                deleteEvent(Integer.valueOf((Integer) messageEvent.data));
                break;
            case EDITED_EVENT:

                break;
            default:
                break;
        }
    }

    private void deleteEvent(int id){
        int index = -1;
        for(int i=0; i<eventList.size(); i++){
            if(eventList.get(i).getId() == id) {
                index = i;
                eventList.remove(i);
                break;
            }
        }
        if(index >=0 ) {
            mAdapterEvent.notifyEventDeleted(index);
        }
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
}

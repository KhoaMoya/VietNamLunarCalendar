package com.khoa.lunarcalendar.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.calendar.model.MessageEvent;
import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.view.activity.AddEnventActivity;
import com.khoa.lunarcalendar.calendar.view.activity.EditEventActivity;
import com.khoa.lunarcalendar.calendar.view.fragment.CalendarFragment;
import com.khoa.lunarcalendar.calendar.view.fragment.DetailEventFragment;
import com.khoa.lunarcalendar.main.viewmodel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.swipebackfragment.SwipeBackActivity;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, mViewModel.getCalendarFragment(), CalendarFragment.class.getSimpleName())
                    .addToBackStack(CalendarFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Subscribe
    public void onListenerMessageEvent(MessageEvent messageEvent) {
        switch (messageEvent.action) {
            case SHOW_DETAIL_EVENT_FRAGMNET:
                showDetailEvent((MyEvent) messageEvent.data);
                break;
            case SHOW_EDIT_EVENT_ACTIVITY:
                showEditEventActivity((MyEvent) messageEvent.data);
                break;
            case SHOW_ADD_EVENT_ACTIVITY:
                showAddEventActivity();
                break;
            default:
                break;
        }
    }


    private void showDetailEvent(MyEvent myEvent) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(DetailEventFragment.class.getSimpleName())
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .hide(mViewModel.getCalendarFragment())
                .add(R.id.container, mViewModel.createDetailEventFragment(myEvent), DetailEventFragment.class.getSimpleName())
                .commit();
    }

    private void showEditEventActivity(MyEvent myEvent) {
        Intent intent = new Intent(MainActivity.this, EditEventActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("My_Event", myEvent);
        intent.putExtras(bundle);
        startActivityForResult(intent, mViewModel.EDIT_EVENT_REQUEST_CODE);
    }

    private void showAddEventActivity(){
        Intent intent = new Intent(MainActivity.this, AddEnventActivity.class);
        startActivity(intent);
//        overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == mViewModel.EDIT_EVENT_REQUEST_CODE){
            Log.e("Loi", resultCode+"");
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                MyEvent editedEvent = (MyEvent) bundle.get("My_Event");
                if(editedEvent!=null){

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            CalendarFragment calendarFragment = mViewModel.getCalendarFragment();
            if (calendarFragment.isExpandedBottomSheet()) {
                calendarFragment.closeBottomSheet();
            } else {
                finish();
            }
        } else {
            super.onBackPressed();
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

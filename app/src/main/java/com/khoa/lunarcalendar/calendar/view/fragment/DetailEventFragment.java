package com.khoa.lunarcalendar.calendar.view.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.calendar.dialog.DialogUltis;
import com.khoa.lunarcalendar.calendar.model.MessageEvent;
import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.ultis.CalendarUltis;
import com.khoa.lunarcalendar.calendar.ultis.GlideApp;
import com.khoa.lunarcalendar.databinding.DetailEventFragmentBinding;

import org.greenrobot.eventbus.EventBus;

import me.yokeyword.swipebackfragment.SwipeBackFragment;

public class DetailEventFragment extends SwipeBackFragment {

    private DetailEventFragmentBinding mBinding;
    private MyEvent myEvent;

    public DetailEventFragment(MyEvent myEvent) {
        this.myEvent = myEvent;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.detail_event_fragment, container, false);

        return attachToSwipeBack(mBinding.getRoot());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createOptionMenu(mBinding.imgMenu);
    }

    private void createOptionMenu(View view) {
        MenuBuilder menuBuilder = new MenuBuilder(getContext());
        MenuInflater inflater = new MenuInflater(getContext());
        inflater.inflate(R.menu.event_menu, menuBuilder);
        final MenuPopupHelper optionsMenu = new MenuPopupHelper(getContext(), menuBuilder, view);
        optionsMenu.setForceShowIcon(true);

        // Set Item Click Listener
        menuBuilder.setCallback(new MenuBuilder.Callback() {
            @Override
            public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        new DialogUltis(getContext()).showDialogConfirm(myEvent.getId(), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.DELETED_EVENT, myEvent.getId()));
                                getActivity().onBackPressed();
                            }
                        });
                        return true;
                    case R.id.action_edit:
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.SHOW_EDIT_EVENT_ACTIVITY, myEvent));
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onMenuModeChange(MenuBuilder menu) {
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionsMenu.show();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupBinding();
    }

    private void setupBinding() {
        GlideApp.with(getContext())
                .asBitmap()
                .load("http://static.appotapay.com" + myEvent.getImageURL())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        int w = resource.getWidth();
                        int h = resource.getHeight();
                        int wView = mBinding.imgEvent.getWidth();
                        int hView = wView * h / w;
                        ViewGroup.LayoutParams params = mBinding.imgEvent.getLayoutParams();
                        params.height = hView;
                        params.width = wView;
                        mBinding.imgEvent.setLayoutParams(params);
                        mBinding.imgEvent.setImageBitmap(resource);
                    }
                });


        mBinding.txtTitle.setText(myEvent.getTitle());
        mBinding.txtContent.setText(Html.fromHtml(myEvent.getContent()));

        if (myEvent.getCalendar().equals("solar")) {
            mBinding.imgTypeDay.setImageDrawable(getResources().getDrawable(R.drawable.ic_calendar_solar));
        } else {
            mBinding.imgTypeDay.setImageDrawable(getResources().getDrawable(R.drawable.ic_calendar_lunar));
        }

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                String[] convertDate = CalendarUltis.convertDate(myEvent.getStartDate(), myEvent.getEndDate(), myEvent.getCalendar().equals("solar"), myEvent.getYear());
                mBinding.txtSolarDay.setText(convertDate[0]);
                mBinding.txtLunarDay.setText(convertDate[1]);
                mBinding.txtDuration.setText(convertDate[2]);
            }
        });

        mBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }
}

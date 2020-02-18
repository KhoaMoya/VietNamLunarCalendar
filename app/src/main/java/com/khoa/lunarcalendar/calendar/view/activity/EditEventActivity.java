package com.khoa.lunarcalendar.calendar.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;

import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.databinding.EditEventFragmentBinding;

public class EditEventActivity extends AppCompatActivity {

    private MyEvent myEvent;
    private EditEventFragmentBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.edit_event_fragment);
        myEvent = getRecivedEvent();
        setupView();
        showDataEvent();
    }

    private MyEvent getRecivedEvent(){
        Bundle bundle = getIntent().getExtras();
        return (MyEvent) bundle.get("My_Event");
    }

    private void showDataEvent(){
        mBinding.txtTitle.setText("Sửa sự kiện");
        mBinding.edtTitile.append(myEvent.getTitle());
        mBinding.edtContent.append(Html.fromHtml(myEvent.getContent()));
        showToggleTypeDay();
    }

    public void selectStartTime(View view){

    }

    public void selectEndTime(View view){

    }

    public void selectRepeat(View view){

    }

    public void selectRemind(View view){

    }

    public void saveEvent(View view){

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    private void setupView(){
        mBinding.txtSolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEvent.setCalendar("solar");
                showToggleTypeDay();
            }
        });

        mBinding.txtLunar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myEvent.setCalendar("lunar");
                showToggleTypeDay();
            }
        });

        mBinding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showToggleTypeDay(){
        if(myEvent.getCalendar().equals("solar")){
            mBinding.txtSolar.setTextColor(getResources().getColor(R.color.colorWhite));
            mBinding.txtSolar.setBackground(getResources().getDrawable(R.drawable.background_black_conner6));

            mBinding.txtLunar.setTextColor(getResources().getColor(R.color.colorBlack));
            mBinding.txtLunar.setBackground(getResources().getDrawable(R.drawable.background_white_conner8));
        }else{
            mBinding.txtLunar.setTextColor(getResources().getColor(R.color.colorWhite));
            mBinding.txtLunar.setBackground(getResources().getDrawable(R.drawable.background_black_conner6));

            mBinding.txtSolar.setTextColor(getResources().getColor(R.color.colorBlack));
            mBinding.txtSolar.setBackground(getResources().getDrawable(R.drawable.background_white_conner8));
        }
    }

}

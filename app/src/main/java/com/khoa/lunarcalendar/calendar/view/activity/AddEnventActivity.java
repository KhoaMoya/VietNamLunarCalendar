package com.khoa.lunarcalendar.calendar.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.databinding.EditEventFragmentBinding;

public class AddEnventActivity extends AppCompatActivity {
    private EditEventFragmentBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.edit_event_fragment);

        animationFields();
    }

    private void animationFields(){
        mBinding.imgClose.setVisibility(View.INVISIBLE);
        mBinding.txtTitle.setVisibility(View.INVISIBLE);
        mBinding.txtSave.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.imgClose.startAnimation(AnimationUtils.loadAnimation(AddEnventActivity.this, R.anim.slide_to_left));
                mBinding.imgClose.setVisibility(View.VISIBLE);
            }
        }, 200);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.txtTitle.startAnimation(AnimationUtils.loadAnimation(AddEnventActivity.this, R.anim.slide_to_left));
                mBinding.txtTitle.setVisibility(View.VISIBLE);
            }
        }, 400);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.txtSave.startAnimation(AnimationUtils.loadAnimation(AddEnventActivity.this, R.anim.slide_up_appear));
                mBinding.txtSave.setVisibility(View.VISIBLE);
            }
        }, 600);

    }

}

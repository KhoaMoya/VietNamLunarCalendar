package com.khoa.lunarcalendar.calendar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.khoa.lunarcalendar.R;


public class DialogUltis extends Dialog {

    public DialogUltis(@NonNull Context context) {
        super(context);
        Window window = getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.getAttributes().windowAnimations = R.style.Calendar_Dialog;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public void showDialogMessage(String content){
        setContentView(R.layout.notification_dialog);
        setCancelable(true);

        TextView txtContent = findViewById(R.id.txt_message);
        txtContent.setText(content);

        findViewById(R.id.txt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        show();
    }

    public void showDialogConfirm(final int id, final View.OnClickListener callback){
        setContentView(R.layout.confirm_dialog);
        setCancelable(false);

        final TextView txtContent = findViewById(R.id.txt_message);
        final LinearLayout layoutButton = findViewById(R.id.layout_button);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);
        final ImageView imgDone = findViewById(R.id.img_done);
        final ImageView imgError = findViewById(R.id.img_error);
        final TextView txtClose = findViewById(R.id.txt_close);

        findViewById(R.id.txt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                layoutButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                txtContent.setText("Đang xóa");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        boolean deleted = new CalendarReponsitory(getContext()).deleteEvent(id);
                        boolean deleted = true;
                        if(deleted) {
                            progressBar.setVisibility(View.GONE);
                            txtContent.setText("Đã xóa");

                            imgDone.setVisibility(View.VISIBLE);
                            imgDone.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in_appear));

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dismiss();
                                    callback.onClick(view);
                                }
                            }, 400);
                        } else {
                            progressBar.setVisibility(View.GONE);
                            txtContent.setText("Xóa thất bại");

                            imgError.setVisibility(View.VISIBLE);
                            imgError.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.zoom_in_appear));

                            txtClose.setVisibility(View.VISIBLE);
                        }
                    }
                }, 400);
            }
        });

        findViewById(R.id.txt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        txtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        show();
    }

}

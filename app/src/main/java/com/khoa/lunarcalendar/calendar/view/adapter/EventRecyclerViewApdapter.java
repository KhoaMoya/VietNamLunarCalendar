package com.khoa.lunarcalendar.calendar.view.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.khoa.lunarcalendar.R;
import com.khoa.lunarcalendar.calendar.model.MessageEvent;
import com.khoa.lunarcalendar.calendar.model.MyEvent;
import com.khoa.lunarcalendar.calendar.reponsitory.CalendarReponsitory;
import com.khoa.lunarcalendar.calendar.ultis.CalendarUltis;
import com.khoa.lunarcalendar.calendar.ultis.GlideApp;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class EventRecyclerViewApdapter extends RecyclerView.Adapter<EventRecyclerViewApdapter.ViewHolder> {

    private ArrayList<MyEvent> eventList;
    public ArrayList<MyEvent> eventFullList;
    private Context context;
    private int MAX_LOAD = 10;
    private int currentSize = 0;
    private Handler handler;

    public EventRecyclerViewApdapter(Context context) {
        this.eventList = new ArrayList<>();
        this.eventFullList = new ArrayList<>();
        this.context = context;
        this.handler = new Handler();
    }

    public ArrayList<MyEvent> getEventList() {
        return eventList;
    }

    public void setEventList(final ArrayList<MyEvent> list) {
        this.eventFullList = list;
        // dừng handler nếu đang chạy
        handler.removeMessages(0);
        // xóa dữ liệu cũ
        clear();
        // add dữ liệu mới
        for (int i = 0; i < list.size(); i++) {
            final int j = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    eventList.add(list.get(j));
                    notifyItemInserted(eventList.size() - 1);
                }
            }, 200 * (j + 1));
            currentSize++;
            if (i >= MAX_LOAD) break;
        }

    }

    private void loadMore() {
        if (currentSize == eventFullList.size()) return;
        int limit = currentSize + MAX_LOAD - 1;
        int start = currentSize;
        for (int i = start; i < eventFullList.size(); i++) {
            final int j = i;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    eventList.add(eventFullList.get(j));
                    notifyItemInserted(eventList.size() - 1);
                }
            }, 200 * (j - start));
            currentSize++;
            if (i >= limit) break;
        }
    }

    private void clear() {
        int size = this.eventList.size();
        this.eventList.clear();
        notifyItemRangeRemoved(0, size);
        currentSize = 0;
    }

    public void notifyEventDeleted(int index){
        eventList.remove(index);
        currentSize--;
        notifyDataSetChanged();
    }

    public void notifyEventEdited(int index){
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final int position = holder.getAdapterPosition();
        holder.bind(eventList.get(position));
        if (position == currentSize - 1) {
            loadMore();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new MessageEvent(MessageEvent.Action.SHOW_DETAIL_EVENT_FRAGMNET, eventList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList != null ? eventList.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgEvent;
        private TextView txtTitle;
        public TextView txtSolarTime;
        public TextView txtLunarTime;
        public ImageView imgAlarm;
        public LinearLayout layoutAlarm;
        public TextView txtAlarmTime;
        public ImageView imgDayType;
        public TextView txtDuration;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgEvent = itemView.findViewById(R.id.event_img);
            txtTitle = itemView.findViewById(R.id.txt_event_title);

            txtSolarTime = itemView.findViewById(R.id.txt_solar_time);
            txtLunarTime = itemView.findViewById(R.id.txt_lunar_time);
            imgAlarm = itemView.findViewById(R.id.img_alarm_clock);
            layoutAlarm = itemView.findViewById(R.id.layout_alarm);
            txtAlarmTime = itemView.findViewById(R.id.txt_time_alarm);
            imgDayType = itemView.findViewById(R.id.img_type_day);
            txtDuration = itemView.findViewById(R.id.txt_duration);

        }

        private void bind(MyEvent event) {
            GlideApp.with(context)
                    .load("http://static.appotapay.com" + event.getImageURL())
                    .override(300, 240)
                    .centerCrop()
                    .into(imgEvent);
            txtTitle.setText(event.getTitle());

            String[] convertDate = CalendarUltis.convertDate(event.getStartDate(), event.getEndDate(), event.getCalendar().equals("solar"), event.getYear());
            txtSolarTime.setText(convertDate[0]);
            txtLunarTime.setText(convertDate[1]);
            txtDuration.setText(convertDate[2]);

            if (event.getCalendar().equals("solar")) imgDayType.setImageResource(R.drawable.ic_calendar_solar);
            else imgDayType.setImageResource(R.drawable.ic_calendar_lunar);

        }
    }
}

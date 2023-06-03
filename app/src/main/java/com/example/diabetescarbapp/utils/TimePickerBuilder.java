package com.example.diabetescarbapp.utils;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerBuilder {

    private static TimePickerBuilder instance;

    public static TimePickerBuilder getInstance() {
        if (instance == null) {
            instance = new TimePickerBuilder();
        }
        return instance;
    }

    public void build(Context context, TextView timeView, OnTimeChangeListener listener) {
        new TimePicker(context, timeView).build(listener);
    }

    public interface OnTimeChangeListener {
        void onTimeChange(String time);
    }

    public class TimePicker {

        private Context context;
        private TextView timeView;
        private int hour, minute;

        private TimePickerDialog timePickerDialog;

        private OnTimeChangeListener listener;

        public TimePicker(Context context, TextView timeView) {
            this.context = context;
            this.timeView = timeView;

            Calendar calendar = Calendar.getInstance();
            this.hour = calendar.get(Calendar.HOUR_OF_DAY);
            this.minute = calendar.get(Calendar.MINUTE);
        }

        public TimePicker(Context context, TextView timeView, String time) {
            this.context = context;
            this.timeView = timeView;

            String[] fullTime = time.split(":");

            this.hour = Integer.parseInt(fullTime[0]);
            this.minute = Integer.parseInt(fullTime[1].split(" ")[0]);
        }

        public void build(OnTimeChangeListener listener) {
            initDatePicker();
            setViewEventListener();

            this.listener = listener;

            timeView.setText(makeTimeString(hour, minute));
        }


        private void setViewEventListener() {
            timeView.setOnClickListener(v -> {
                timePickerDialog.show();
            });
        }

        private void initDatePicker() {
            TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
                String timeStr = makeTimeString(hourOfDay, minute);
                timeView.setText(timeStr);
                listener.onTimeChange(timeStr);
            };


            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            int style = AlertDialog.THEME_HOLO_LIGHT;

            timePickerDialog = new TimePickerDialog(context, style, timeSetListener, 12, 0, false);

            timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //set previous time..
            timePickerDialog.updateTime(hour, minute);
        }

        private String makeTimeString(int hour, int minute) {
            SimpleDateFormat f24 = new SimpleDateFormat("hh:mm");

            String time = hour + ":" + minute;

            try {
                Date date = f24.parse(time);
                SimpleDateFormat f12 = new SimpleDateFormat("hh:mm aa");
                return f12.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

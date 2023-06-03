package com.example.diabetescarbapp.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerBuilder {

    private static DatePickerBuilder instance;

    public static DatePickerBuilder getInstance() {
        if (instance == null) {
            instance = new DatePickerBuilder();
        }
        return instance;
    }

    private DatePickerBuilder() {}

    public void build(Context context, TextView dateView) {
        new DatePicker(context, dateView).build();
    }

    private static class DatePicker {

        private Context context;
        private TextView dateView;
        private DatePickerDialog datePickerDialog;
        private int year, month, day;

        public DatePicker(Context context, TextView dateView) {
            this.context = context;
            this.dateView = dateView;

            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        public DatePicker(Context context, TextView dateView, String date) {
            this.context = context;
            this.dateView = dateView;

            String[] fullDate = date.split(" ");

            month = getMonthFormat(fullDate[0]);
            day = Integer.parseInt(fullDate[1]);
            year = Integer.parseInt(fullDate[2]);
        }

        public void build() {
            initDatePicker();
            setViewEventListener();

            dateView.setText(makeDateString(year, month, day));
        }

        private void setViewEventListener() {
            dateView.setOnClickListener(v -> {
                datePickerDialog.show();
            });
        }

        private void initDatePicker() {
            DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, day) -> {
                month = month + 1;
                String date = makeDateString(year, month, day);
                dateView.setText(date);
            };

            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            int style = AlertDialog.THEME_HOLO_LIGHT;

            datePickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        }

        private String makeDateString(int year, int month, int day) {
            return getMonthFormat(month) + " " + day + " " + year;
        }

        private String getMonthFormat(int month) {
            switch (month) {
                case 1:
                    return "JAN";
                case 2:
                    return "FEB";
                case 3:
                    return "MAR";
                case 4:
                    return "APR";
                case 5:
                    return "MAY";
                case 6:
                    return "JUN";
                case 7:
                    return "JUL";
                case 8:
                    return "AUG";
                case 9:
                    return "SEP";
                case 10:
                    return "OCT";
                case 11:
                    return "NOV";
                case 12:
                    return "DEC";
                default:
                    return "JAN";
            }
        }

        public static int getMonthFormat(String month) {
            switch (month) {
                case "JAN":
                    return 1;
                case "FEB":
                    return 2;
                case "MAR":
                    return 3;
                case "APR":
                    return 4;
                case "MAY":
                    return 5;
                case "JUN":
                    return 6;
                case "JUL":
                    return 7;
                case "AUG":
                    return 8;
                case "SEP":
                    return 9;
                case "OCT":
                    return 10;
                case "NOV":
                    return 11;
                case "DEC":
                    return 12;
                default:
                    return 1;
            }
        }
    }
}

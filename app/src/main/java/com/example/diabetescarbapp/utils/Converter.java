package com.example.diabetescarbapp.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

public class Converter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getStrFromDate(LocalDate date) {
        return date.getDayOfMonth() + " " + date.getMonth().getValue() + " " + date.getYear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String getStrFromTime(LocalTime time) {
        SimpleDateFormat f24 = new SimpleDateFormat("hh:mm");

        String timeStr = time.getHour() + ":" + time.getMinute();

        try {
            Date date = f24.parse(timeStr);
            SimpleDateFormat f12 = new SimpleDateFormat("hh:mm aa");
            return f12.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalTime getTimeFromStr(String time, String ampm) {
        String[] timeStr = time.split(":");
        return LocalTime.of(
                ampm.toLowerCase(Locale.ROOT).equals("am") ? Integer.parseInt(timeStr[0]) : Integer.parseInt(timeStr[0])+12,
                Integer.parseInt(timeStr[1]));
    }
}

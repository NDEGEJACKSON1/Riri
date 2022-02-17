package com.example.riri;


import android.annotation.SuppressLint;
import android.icu.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.TimeZone;

public class user_dataInfos {

    final String title, description,time,date;

    public user_dataInfos(String title,String description,String time,String date){
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
    }

    public String [] getRemainedTime(){
        String [] int_label = {"inf","inf"};

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            final long due_date_time = (Objects.requireNonNull(simpleDateFormat.parse(date + " " + time))).getTime();
            final long current_date_time =  Calendar.getInstance().getTimeInMillis();

            final long remained_time = due_date_time - current_date_time - (10792707 + 7000);

            final long seconds = remained_time / 1000;
            final long minutes = seconds / 60;
            final long hours = minutes / 60;
            final long days  = hours / 24;

            if (days >= 1) {
                int_label[0] = String.valueOf(days);
                int_label[1] = "Dys";

                return int_label;
            }

            if (hours >= 1){
                int_label[0] = String.valueOf(hours);
                int_label[1] = "Hrs";
                return int_label;
            }

            if (minutes >= 1){
                int_label[0] = String.valueOf(minutes);
                int_label[1] = "Min";
                return int_label;
            }

            if (seconds >= 1){
                int_label[0] = String.valueOf(seconds);
                int_label[1] = "Sec";
                return int_label;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return int_label;
    }

    public boolean isOutOfTime(){
        return getRemainedTime()[0].equals("inf");
    }


}

package com.example.philipp.kastl_wecker_v1;

import android.support.v7.app.AppCompatActivity;


/**
 * Created by Philipp on 07.01.2017.
 */


public class Wecker_managment extends AppCompatActivity {

    byte hour, minute, day;

    Wecker_managment(byte bla) {
        hour = bla;
        minute = bla;
        day = bla;
    }

    public void set_hour(byte hour) {
        this.hour = hour;
    }

    public void set_minute(byte minute) {
        this.minute = minute;
    }

    public void set_day(byte day) {
        this.day = day;
    }

    public byte get_hour(){
        return hour;
    }
    public byte get_minute(){
        return minute;
    }
    public byte get_day(){
        return day;
    }
}

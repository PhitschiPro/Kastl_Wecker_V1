package com.example.philipp.kastl_wecker_v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.View;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import java.util.Calendar;

public class ClockActivity extends AppCompatActivity {

    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clock);
        this.context = this;

        Wecker_managment Alarm = new Wecker_managment((byte)0);
        final Wecker_managment Alarms[] = {
            Alarm, Alarm, Alarm, Alarm, Alarm, Alarm, Alarm, Alarm
        };

        //initialize TimePicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);
        alarm_timepicker.setIs24HourView(DateFormat.is24HourFormat(this)); //TimePicker is 24h format

        //initialize StatusTextfield
        update_text = (TextView) findViewById(R.id.status_text);

        //create a instance of calender
        final Calendar calendar = Calendar.getInstance();

        //initialize Buttons
        Button set_alarm = (Button) findViewById(R.id.btn_alarm_on);
        Button unset_alarm = (Button) findViewById(R.id.btn_alarm_off);

        //creat an OnClick Listener for alarm on
        set_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //calendar instance, wich is picked on timepicker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                //get int values from calendar instance
                Alarms[0].set_hour((byte) alarm_timepicker.getHour());
                Alarms[0].set_minute((byte) alarm_timepicker.getMinute());

                //convert int to string
                String hour_S = String.valueOf(Alarms[0].get_hour());
                String minute_S = String.valueOf(Alarms[0].get_minute());

                if(Alarms[0].get_minute() < 10) minute_S = "0" + String.valueOf(Alarms[0].get_minute());

                show_toasttime(Alarms[0].get_hour(), Alarms[0].get_minute());

                set_alarm_text("Alarm set to " + hour_S + ":" + minute_S);

            }
        });

        //creat an OnClick Listener for alarm off
        unset_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                set_alarm_text("Alarm deleted!");
                Wecker_löschen(Alarms[0].get_hour(), Alarms[0].get_minute());
            }
        });
    }

    private void show_toasttime(byte hour ,byte minute) {   //Calculetes how long alarm needs to ring

        Calendar cal = Calendar.getInstance();
        int sys_minute = cal.get(Calendar.MINUTE);
        int sys_hour = cal.get(Calendar.HOUR_OF_DAY);
        String toast_time_h = "";
        String toast_time_min = "";
        String toast_time = "";

        int toast_h = hour - sys_hour;
        int toast_min = minute - sys_minute;

        if(minute < sys_minute) {
            toast_min = 60 - sys_minute + minute;
            toast_h--;
        }
        if(hour == 0){
            toast_h = (sys_hour - 24)*(-1) ;
        }
        if( hour < sys_hour && sys_minute > minute){
            toast_h = hour - sys_hour +23;
        }

        if (hour < sys_hour) {
            toast_h = 24 - (hour - sys_hour);
        }

        toast_time_h = Integer.toString(toast_h);
        toast_time_min = Integer.toString(toast_min);


        if(toast_min < 10){
            toast_time_min = "0" + String.valueOf(toast_min);
        }

        toast_time = toast_time_h + ":" + toast_time_min;
        //Toast for how long it takes to ring
        Toast.makeText(getApplicationContext(),  "Der Wecker läutet in " + toast_time , Toast.LENGTH_SHORT).show();
    }

    private void set_alarm_text(String output) {
        update_text.setText(output);
    }

    private void Wecker_editieren(byte hour, byte minute) { //nicht funktionsfähig

        byte Toene, Tag;
        Toene = 0b000000;
        Tag = 0b000;

        byte Status;
        Status = 0b01010000;
        Status |= (Toene & 0b00111100) >> 2;

        short Data;
        Data = 0;
        Data |= (Toene & 0b00000011) << 6;
        Data |= Tag << 3;
        Data |= hour << 6;
        Data |= minute;

    }

    private void Wecker_löschen(byte hour, byte minute) {

        byte Toene, Tag;
        Toene = 0b000000;
        Tag = 0b000;

        byte Status;
        Status = 0b01100000;
        Status |= (Toene & 0b00111100) >> 2;

        short Data;
        Data = 0;
        Data |= (Toene & 0b00000011) << 6;
        Data |= Tag << 3;
        Data |= hour << 6;
        Data |= minute;

    }


    private void Weckersteuerung(byte hour, byte minute){ //int auf short oder byte ändern

        byte Toene, Tag;
        Toene = 0;
        Tag = 0;

        byte Status;
        Status = 0b01000000;
        Status |= (Toene & 0b00111100) >> 2;

        short Data; //unsigned
        Data = 0;
        Data |= (Toene & 0b00000011) << 6;
        Data |= hour << 6;
        Data |= minute;
    }

    private void Lichtsteuerung(byte Strip, byte Effektauswahl, byte Red, byte Green, byte Blue, byte alpha){ //int bei Farben und alpha auf byte ändern

        Effektauswahl = 0b00;

        byte Status;
        Status = 0b00000000;
        Status |= Strip<<4;
        Status |= Effektauswahl;
    }
}

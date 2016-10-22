package com.gtmchaudhary.g_reminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gtmchaudhary on 10/20/2016.
 */
public class EditReminder extends Activity{

    public static int TIME_PICKER_ID = 1;
    public static int DATE_PICKER_ID = 2;

    public int hour_x, minute_x;
    public int day_x, month_x, year_x;

    Button date_picker_button, time_picker_button;
    TextView date_display_textView, time_display_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_reminder);

        // Referencing UI/UX widgets
        date_picker_button = (Button)findViewById(R.id.date_picker_button);
        time_picker_button = (Button)findViewById(R.id.time_picker_button);
        date_display_textView = (TextView)findViewById(R.id.date_display_textView);
        time_display_textView = (TextView)findViewById(R.id.time_display_textView);

        // TimePickerButtonListener
        time_picker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_PICKER_ID);
            }
        });

        // DatePickerButtonListener
        date_picker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DATE_PICKER_ID);
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == TIME_PICKER_ID)
            return new TimePickerDialog(this, timePickerListener, hour_x, minute_x, false);
        else if(id == DATE_PICKER_ID)
            return new DatePickerDialog(this, datePickerListener, year_x, month_x, day_x);
        return null;
    }

    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            /*hour_x = hourOfDay;
            minute_x = minute;

            String amPM = "AM";
            if(hour_x>12){
                hour_x = hour_x - 12;
                amPM = "PM";
            }
            time_display_textView.setText(hour_x + " : " + minute + amPM );
            */
            Date date = new Date();
            date.setHours(hourOfDay);
            date.setMinutes(minute);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:ss aa");
            time_display_textView.setText(sdf.format(date));

        }
    };

    protected DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
            date_display_textView.setText(sdf.format(calendar.getTime()));
        }
    };
}

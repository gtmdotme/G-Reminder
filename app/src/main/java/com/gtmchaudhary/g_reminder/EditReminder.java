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

    Calendar calendar;

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

        // Get current date
        calendar = Calendar.getInstance();

        time_display_textView.setText(new SimpleDateFormat("hh:mm aa").format(calendar.getTime()));
        date_display_textView.setText(new SimpleDateFormat("dd MMM, yyyy").format(calendar.getTime()));
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == TIME_PICKER_ID)
            return new TimePickerDialog(this, timePickerListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        else if(id == DATE_PICKER_ID)
            return new DatePickerDialog(this, datePickerListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        return null;
    }


    protected TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            time_display_textView.setText(sdf.format(calendar.getTime()));

        }
    };

    protected DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
            date_display_textView.setText(sdf.format(calendar.getTime()));
        }
    };
}

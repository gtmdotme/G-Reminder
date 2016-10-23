package com.gtmchaudhary.g_reminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by gtmchaudhary on 10/20/2016.
 */
public class EditReminder extends Activity{

    public static int TIME_PICKER_ID = 1;
    public static int DATE_PICKER_ID = 2;
    public static String TAG = "myReminderApp";

    Calendar calendar;

    Button date_picker_button, time_picker_button;
    Button save_button, discardButton;
    TextView date_display_textView, time_display_textView, id_display_textView;
    EditText label_editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_reminder);



        // Referencing UI/UX widgets
        date_picker_button = (Button)findViewById(R.id.date_picker_button);
        time_picker_button = (Button)findViewById(R.id.time_picker_button);

        save_button = (Button)findViewById(R.id.save_button);
        discardButton = (Button)findViewById(R.id.discard_button);

        date_display_textView = (TextView)findViewById(R.id.date_display_textView);
        time_display_textView = (TextView)findViewById(R.id.time_display_textView);
        id_display_textView = (TextView)findViewById(R.id.id_reminder_edit);
        label_editText = (EditText)findViewById(R.id.editText_reminder_label);

        // Get the intent
        Intent intent = getIntent();
        final Reminder recievedReminderViaIntent = (Reminder)intent.getSerializableExtra("object");// Contains either LABEL and ID or null


        // SAVE button
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Reminder reminder = new Reminder(
                        label_editText.getText().toString().trim(),
                        date_display_textView.getText().toString(),
                        time_display_textView.getText().toString()
                );
                reminder.setCalenderMillis(calendar.getTimeInMillis());
                Log.d(TAG,""+reminder.getCalenderMillis());
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());

                if(reminder.getLabel().isEmpty()){
                    label_editText.setError("Label Should not be blank");
                }
                else {
                    if (recievedReminderViaIntent.getId() > 0){//Card Was CLICKED
                        //***************UPDATE Reminder
                        reminder.setId(recievedReminderViaIntent.getId());
                        Log.d(TAG, "Reminder sent for updation");
                        Snackbar.make(v, "Updated !..", Snackbar.LENGTH_SHORT)
                                //.setAction("Undo", mOnClickListener)
                                .setActionTextColor(Color.RED)
                                .show();
                        db.updateReminderOfDB(reminder);
                    }
                    else {//FAB Was Clicked
                        //***************SAVE Reminder
                        Log.d(TAG, "Reminder sent for insertion");
                        Snackbar.make(v, "Successfully saved !..", Snackbar.LENGTH_SHORT)
                                //.setAction("Undo", mOnClickListener)
                                .setActionTextColor(Color.YELLOW)
                                .show();
                        db.insertReminderToDB(reminder);
                    }
                    db.close();

                    //Now go Back to Home-Screen
                    onBackPressed();
                }
            }
        });

        // DISCARD button
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recievedReminderViaIntent.getId() > 0){//Card Was CLICKED
                    //************DELETE Operation
                    recievedReminderViaIntent.setCalenderMillis(calendar.getTimeInMillis());
                    //  delete from database
                    DatabaseHelper db = new DatabaseHelper(EditReminder.this);
                    Log.d(TAG, "Reminder sent for deletion");
                    Snackbar.make(v, "Deleted", Snackbar.LENGTH_SHORT)
                            //.setAction("Undo", mOnClickListener)
                            .setActionTextColor(Color.WHITE)
                            .show();
                    db.deleteReminderFromDB(recievedReminderViaIntent);
                    db.close();
                }
                else {//FAB Was CLICKED
                    //Do Nothing
                }

                //Now go back to Home-Screen
                onBackPressed();
            }
        });



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

        // Initializing the layout values of UI/UX widget
        if(recievedReminderViaIntent.getId() > 0){//Intent from CardView

            time_display_textView.setText(recievedReminderViaIntent.getStartTime());
            date_display_textView.setText(recievedReminderViaIntent.getDate());
            id_display_textView.setText(recievedReminderViaIntent.getId()+"");
            label_editText.setText(recievedReminderViaIntent.getLabel());
        }
        else {//Intent from MainActivity
            time_display_textView.setText(new SimpleDateFormat("hh:mm aa").format(calendar.getTime()));
            date_display_textView.setText(new SimpleDateFormat("dd MMM, yyyy").format(calendar.getTime()));
            id_display_textView.setText("_tempID");
            label_editText.setText("");
        }
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

    @Override
    public void onBackPressed() {
        Log.i(TAG, "onBackPressed");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        //super.onBackPressed();
    }

}

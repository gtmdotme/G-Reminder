package com.gtmchaudhary.g_reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by gtmchaudhary on 10/19/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    Context context;
    public String TAG = "myReminderApp";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "reminderDatabase";
    private static final String TABLE_NAME = "reminderTable";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_START_TIME = "startTime";
    public static final String COLUMN_CALENDER_MILLIS = "calenderMillis";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_QUERY = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_LABEL + " TEXT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_START_TIME + " TEXT,"
                + COLUMN_CALENDER_MILLIS + " INTEGER)";

        db.execSQL(CREATE_TABLE_QUERY);
        Log.d(TAG, "DB_Helper -> onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //DROP TABLE IF ALREADY EXISTS
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.d(TAG, "DB_Helper -> onUpgrade");
    }

    //Insert Reminder to DB
    void insertReminderToDB(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, reminder.getLabel());
        values.put(COLUMN_DATE, reminder.getDate());
        values.put(COLUMN_START_TIME, reminder.getStartTime());
        values.put(COLUMN_CALENDER_MILLIS, reminder.getCalenderMillis());
            // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection

        /***********************ONLY FOR LOGCAT*********************/
        //Reading the inserted reminder so as to DOUBLE-Check
        Reminder tempx = readReminderFromDB(searchID(reminder));
        String logReminder = "Reminder INSERTED to DB"
                +"\n    ID- "+searchID(reminder)
                +"\n    Label- "+tempx.getLabel()
                +"\n    Date- "+tempx.getDate()
                +"\n    StartTime- "+tempx.getStartTime()
                +"\n    CalenderMillis- "+tempx.getCalenderMillis();
        Log.d(TAG,logReminder);

        //Set Alarm
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                searchID(reminder),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getCalenderMillis(), pendingIntent);
        Log.d(TAG, "Alarm Set for " + reminder.getDate() + " " + reminder.getStartTime()); //new SimpleDateFormat("dd MMM, yyyy - hh:mm aa").format(reminder.getCalendar().getTime()));
    }

    //Delete Reminder from DB
    void deleteReminderFromDB(Reminder deletedReminder){
        //'deletedReminder' object only contains ID, LABEL and CALENDER values

        /***********************ONLY FOR LOGCAT*********************/
        //Reading the deleted reminder so as to DOUBLE-Check
        Reminder tempReminder = readReminderFromDB(deletedReminder.getId());
        String logReminder = "Reminder DELETED from DB"
                +"\n    ID- "+tempReminder.getId()
                +"\n    Label- "+tempReminder.getLabel()
                +"\n    Date- "+tempReminder.getDate()
                +"\n    StartTime- "+tempReminder.getStartTime()
                +"\n    CalenderMillis- "+tempReminder.getCalenderMillis();
        Log.d(TAG, logReminder);


        SQLiteDatabase db = this.getWritableDatabase();
            // Deleting Row
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(deletedReminder.getId()) });
        db.close();


        // Cancel the Alarm
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, deletedReminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
        Log.d(TAG, "Alarm Cancelled for " + tempReminder.getDate() + " " + tempReminder.getStartTime());//+ new SimpleDateFormat("dd MMM, yyyy - hh:mm aa").format(deletedReminder.getCalendar().getTime()));
    }

    //Update Reminder of DB
    void updateReminderOfDB(Reminder reminder){
        /***********************ONLY FOR LOGCAT*********************/
        //Reading the updated reminder so as to DOUBLE-Check
        Reminder oldReminder = readReminderFromDB(reminder.getId());
        String logReminder = "Reminder UPDATED to DB"
                +"\n    ID- "+oldReminder.getId()+"-->"+reminder.getId()
                +"\n    Label- "+oldReminder.getLabel()+"-->"+reminder.getLabel()
                +"\n    Date- "+oldReminder.getDate()+"-->"+reminder.getDate()
                +"\n    StartTime- "+oldReminder.getStartTime()+"-->"+reminder.getStartTime()
                +"\n    CalenderMillis- "+oldReminder.getCalenderMillis()+"-->"+reminder.getCalenderMillis();;
        Log.d(TAG,logReminder);

        //this object 'reminder' also contains ID
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, reminder.getLabel());
        values.put(COLUMN_DATE, reminder.getDate().toString());
        values.put(COLUMN_START_TIME, reminder.getStartTime().toString());
        values.put(COLUMN_CALENDER_MILLIS, reminder.getCalenderMillis());
            // Updating Row
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getId())});
        db.close();


        // Cancel and then create New alarm
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        intent = new Intent(context, NotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(
                context,
                reminder.getId(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | Intent.FILL_IN_DATA
        );
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminder.getCalenderMillis(), pendingIntent);
        Log.d(TAG, "Alarm Updated to " + reminder.getDate() + " " + reminder.getStartTime());//+ new SimpleDateFormat("dd MMM, yyyy - hh:mm aa").format(reminder.getCalendar().getTime()));
    }

    //Search ID of reminder
    int searchID(Reminder reminder){
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;

        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+
                    COLUMN_LABEL +" =? AND " +
                    COLUMN_DATE + " =? AND " +
                    COLUMN_START_TIME + " =?",
                    new String[] {reminder.getLabel(), reminder.getDate(), reminder.getStartTime()  });

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            }
        }finally {
            db.close();
        }
        // return id
        return id;
    }

    //Read Reminder from DB
    Reminder readReminderFromDB(int id){
        Reminder reminder = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_ID +" =?", new String[] {id + ""});

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                reminder = new Reminder(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LABEL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))
                );
                reminder.setCalenderMillis(cursor.getLong(cursor.getColumnIndex(COLUMN_CALENDER_MILLIS)));
            }
        }finally {
            db.close();
        }
        // return reminder
        return reminder;
    }

    //Read all Reminders from DB
    ArrayList<Reminder> readAllRemindersFromDB(){
        ArrayList<Reminder> reminders = new ArrayList<Reminder>();
        Reminder reminder;
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                reminder = new Reminder(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LABEL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME))
                );
                reminder.setCalenderMillis(cursor.getLong(cursor.getColumnIndex(COLUMN_CALENDER_MILLIS)));
                // Adding contact to list
                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        // return reminder list
        return reminders;
    }

}

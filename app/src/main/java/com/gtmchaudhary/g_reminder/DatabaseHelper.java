package com.gtmchaudhary.g_reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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
                + COLUMN_START_TIME + " TEXT";

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
        values.put(COLUMN_DATE, reminder.getDate().toString());
        values.put(COLUMN_START_TIME, reminder.getStartTime().toString());
            // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    //Delete Reminder from DB
    void deleteReminderFromDB(int id){
        SQLiteDatabase db = this.getWritableDatabase();
            // Deleting Row
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    //Update Reminder of DB
    void updateReminderOfDB(Reminder reminder){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LABEL, reminder.getLabel());
        values.put(COLUMN_DATE, reminder.getDate().toString());
        values.put(COLUMN_START_TIME, reminder.getStartTime().toString());
            // Updating Row
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(reminder.getId())});
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
                // Adding contact to list
                reminders.add(reminder);
            } while (cursor.moveToNext());
        }
        // return reminder list
        return reminders;
    }

}

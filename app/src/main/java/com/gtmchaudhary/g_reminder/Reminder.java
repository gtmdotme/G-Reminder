package com.gtmchaudhary.g_reminder;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by gtmchaudhary on 10/19/2016.
 */
public class Reminder implements Serializable {
    private int id;
    private String label;
    private String date;
    private String startTime;
    private long calenderMillis;


    public Reminder() {
    }

    public Reminder(String label, String date, String startTime) {
        this.label = label;
        this.date = date;
        this.startTime = startTime;
    }

    public Reminder(int id, String label, String date, String startTime) {
        this.id = id;
        this.label = label;
        this.date = date;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


    public long getCalenderMillis() {
        return calenderMillis;
    }

    public void setCalenderMillis(long calenderMillis) {
        this.calenderMillis = calenderMillis;
    }
}

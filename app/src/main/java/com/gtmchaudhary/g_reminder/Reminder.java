package com.gtmchaudhary.g_reminder;

import java.util.Date;

/**
 * Created by gtmchaudhary on 10/19/2016.
 */
public class Reminder {
    private int id;
    private String label;
    private String date;
    private String fromTime;
    private String toTime;

    public Reminder() {
    }

    public Reminder(int id, String label, String date, String fromTime, String toTime) {
        this.id = id;
        this.label = label;
        this.date = date;
        this.fromTime = fromTime;
        this.toTime = toTime;
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

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }
}

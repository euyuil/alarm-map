package com.euyuil.alarmmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.util.Date;
import java.util.List;

import com.euyuil.alarmmap.AlarmContract.AlarmEntry;

/**
 * Created by Yue on 13-9-27.
 */

public class Alarm {

    private Long id;
    private String title;
    private Date alarmTime;
    private Location alarmLocation;
    private Integer alarmDayOfWeek;

    public enum Weekday {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    public static Alarm findById(Long id) {
        // TODO
        return null;
    }

    public static List<Alarm> findAll() {
        // TODO
        return null;
    }

    public boolean insert(Context context) {

        SQLiteDatabase db = new AlarmDbHelper(context).getWritableDatabase();

        if (db == null)
            return false;

        ContentValues values = new ContentValues();

        values.put(AlarmEntry.COLUMN_NAME_ENTRY_TITLE, getTitle());

        if (getAlarmTime() != null)
            values.put(AlarmEntry.COLUMN_NAME_ENTRY_ALARM_TIME, getAlarmTime().getTime());

        if (getAlarmLocation() != null) {
            values.put(AlarmEntry.COLUMN_NAME_ENTRY_ALARM_LOCATION_LATITUDE, getAlarmLocation().getLatitude());
            values.put(AlarmEntry.COLUMN_NAME_ENTRY_ALARM_LOCATION_LONGITUDE, getAlarmLocation().getLongitude());
        }

        values.put(AlarmEntry.COLUMN_NAME_ENTRY_ALARM_DAY_OF_WEEK, getAlarmDayOfWeek());

        id = db.insert(AlarmEntry.TABLE_NAME, AlarmEntry.COLUMN_NAME_NULLABLE, values);

        return true;
    }

    public boolean delete(Context context) {
        // TODO
        return false;
    }

    public boolean update(Context context) {
        // TODO
        return false;
    }

    public boolean getAlarmDayOfWeek(Weekday weekday) {
        return alarmDayOfWeek != null && (alarmDayOfWeek & (1 << weekday.ordinal())) != 0;
    }

    public void setAlarmDayOfWeek(Weekday weekday, boolean flag) {
        if (alarmDayOfWeek == null)
            alarmDayOfWeek = 0;
        if (flag)
            alarmDayOfWeek = alarmDayOfWeek | (1 << weekday.ordinal());
        else
            alarmDayOfWeek = alarmDayOfWeek & ~(1 << weekday.ordinal());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAlarmDayOfWeek() {
        return alarmDayOfWeek;
    }

    public void setAlarmDayOfWeek(Integer alarmDayOfWeek) {
        this.alarmDayOfWeek = alarmDayOfWeek;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Location getAlarmLocation() {
        return alarmLocation;
    }

    public void setAlarmLocation(Location alarmLocation) {
        this.alarmLocation = alarmLocation;
    }
}

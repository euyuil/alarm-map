package com.euyuil.alarmmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;

import com.euyuil.alarmmap.AlarmContract.AlarmEntry;
import com.euyuil.alarmmap.utility.AlarmUtility;

import java.util.Date;

/**
 * The model for Alarm object.
 * @author EUYUIL
 * @version 0.0.20130927
 */

public class Alarm {

    private Long id; // TODO Maybe long?
    private String title;
    private boolean available = true;
    private Date timeOfDay;
    private Location location;
    private Double locationRadius; // TODO Integrate this.
    private String locationAddress;
    private Integer dayOfWeek;
    private boolean repeat = false;
    private String ringtone;

    public enum Weekday {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }

    public static Alarm findById(Context context, long id) {

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, AlarmEntry._ID + " = ?",
                new String[]{String.valueOf(id)}, null);

        if (cursor != null && cursor.moveToFirst())
            return fromCursor(cursor);

        return null;
    }

    public static Alarm findByUri(Context context, Uri uri) {

        Cursor cursor = context.getContentResolver().query(
                uri, AlarmEntry.PROJECTION_ALARM_DETAILS, null, null, null);

        if (cursor != null && cursor.moveToFirst())
            return fromCursor(cursor);

        return null;
    }

    public static Cursor findAll(Context context) {
        return context.getContentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, null, null, null);
    }

    public boolean insert(Context context) {

        Uri uri = context.getContentResolver().insert(getClassUri(), getContentValues());

        if (uri == null)
            return false;

        try {
            id = Long.parseLong(uri.getLastPathSegment());
        } catch (NumberFormatException nfe) {
            id = null;
        }

        if (id != null) {
            AlarmUtility.register(context, this);
            return true;
        }

        return false;
    }

    public boolean delete(Context context) {
        AlarmUtility.unregister(context, this); // TODO Move those to content provider?
        return context.getContentResolver().delete(getUri(), null, null) > 0;
    }

    public boolean update(Context context) {
        AlarmUtility.register(context, this);
        return context.getContentResolver().update(getUri(), getContentValues(), null, null) > 0;
    }

    public static Alarm fromCursor(Cursor cursor) {

        Alarm alarm = new Alarm();

        alarm.setId(cursor.getLong(cursor.getColumnIndex(AlarmEntry._ID)));
        alarm.setTitle(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TITLE)));
        alarm.setAvailable(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_AVAILABLE)) != 0);
        alarm.setTimeOfDay(new Date(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TIME_OF_DAY))));

        Location alarmLocation = new Location("content://com.euyuil.alarmmap.provider/alarm");
        alarmLocation.setLatitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE)));
        alarmLocation.setLongitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE)));
        alarm.setLocation(alarmLocation);

        alarm.setLocationRadius(cursor.getDouble(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_RADIUS)));
        alarm.setLocationAddress(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_ADDRESS)));
        alarm.setDayOfWeek(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK)));
        alarm.setRepeat(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_REPEAT)) != 0);
        alarm.setRingtone(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_RINGTONE)));

        return alarm;
    }

    public ContentValues getContentValues() {

        ContentValues values = new ContentValues();

        values.put(AlarmEntry.COLUMN_NAME_ALARM_TITLE, getTitle());
        values.put(AlarmEntry.COLUMN_NAME_ALARM_AVAILABLE, getAvailable());

        if (getTimeOfDay() != null)
            values.put(AlarmEntry.COLUMN_NAME_ALARM_TIME_OF_DAY, getTimeOfDay().getTime());

        if (getLocation() != null) {
            values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE, getLocation().getLatitude());
            values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE, getLocation().getLongitude());
        }

        values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_RADIUS, getLocationRadius());
        values.put(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_ADDRESS, getLocationAddress());
        values.put(AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK, getDayOfWeek());
        values.put(AlarmEntry.COLUMN_NAME_ALARM_REPEAT, getRepeat());
        values.put(AlarmEntry.COLUMN_NAME_ALARM_RINGTONE, getRingtone());

        return values;
    }

    public static Uri getClassUri() {
        return Uri.parse("content://com.euyuil.alarmmap.provider/alarm");
    }

    public Uri getUri() {
        return Uri.parse("content://com.euyuil.alarmmap.provider/alarm/" + getId().toString());
    }

    public boolean getDayOfWeek(Weekday weekday) {
        return dayOfWeek != null && (dayOfWeek & (1 << weekday.ordinal())) != 0;
    }

    public void setDayOfWeek(Weekday weekday, boolean flag) {
        if (dayOfWeek == null)
            dayOfWeek = 0;
        if (flag)
            dayOfWeek = dayOfWeek | (1 << weekday.ordinal());
        else
            dayOfWeek = dayOfWeek & ~(1 << weekday.ordinal());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(Date timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getLocationRadius() {
        return locationRadius;
    }

    public void setLocationRadius(Double locationRadius) {
        this.locationRadius = locationRadius;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }
}

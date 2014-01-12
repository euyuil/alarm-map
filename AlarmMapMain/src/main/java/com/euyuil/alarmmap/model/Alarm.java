package com.euyuil.alarmmap.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;

import com.euyuil.alarmmap.AlarmApplication;
import com.euyuil.alarmmap.model.AlarmContract.AlarmEntry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.IllegalFormatConversionException;

/**
 * The model for Alarm object.
 * @author EUYUIL
 * @version 0.0.20130927
 */
public class Alarm {

    public static final Uri CONTENT_URI = Uri.parse("content://com.euyuil.alarmmap.provider/alarm");

    private Long id; // TODO Maybe long?
    private String title;
    private boolean available = true;
    private TimeOfDay timeOfDay; // TODO Timezone change.
    private Location location;
    private Double locationRadius; // TODO Integrate this.
    private String locationAddress;
    private Integer dayOfWeek;
    private boolean repeat = false;
    private String ringtone;

    public static Alarm findById(long id) {

        Cursor cursor = AlarmApplication.contentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, AlarmEntry._ID + " = ?",
                new String[]{String.valueOf(id)}, null);

        if (cursor != null && cursor.moveToFirst())
            return new Alarm(cursor);

        return null;
    }

    public static Alarm findByUri(@NotNull Uri uri) {

        Cursor cursor = AlarmApplication.contentResolver().query(
                uri, AlarmEntry.PROJECTION_ALARM_DETAILS, null, null, null);

        if (cursor != null && cursor.moveToFirst())
            return new Alarm(cursor);

        return null;
    }

    public static Cursor findAll() {
        return AlarmApplication.contentResolver().query(
                Uri.parse("content://com.euyuil.alarmmap.provider/alarm"),
                AlarmEntry.PROJECTION_ALARM_DETAILS, null, null, null);
    }

    public Alarm() {
    }

    public Alarm(@NotNull ContentValues values) {
        // TODO
    }

    public Alarm(@NotNull Cursor cursor) {

        setId(cursor.getLong(cursor.getColumnIndex(AlarmEntry._ID)));
        setTitle(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TITLE)));
        setAvailable(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_AVAILABLE)) != 0);
        setTimeOfDay(new TimeOfDay(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_TIME_OF_DAY))));

        Location alarmLocation = new Location("content://com.euyuil.alarmmap.provider/alarm");
        alarmLocation.setLatitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LATITUDE)));
        alarmLocation.setLongitude(cursor.getLong(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_LONGITUDE)));
        setLocation(alarmLocation);

        setLocationRadius(cursor.getDouble(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_RADIUS)));
        setLocationAddress(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_LOCATION_ADDRESS)));
        setDayOfWeek(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_DAY_OF_WEEK)));
        setRepeat(cursor.getInt(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_REPEAT)) != 0);
        setRingtone(cursor.getString(cursor.getColumnIndex(AlarmEntry.COLUMN_NAME_ALARM_RINGTONE)));
    }

    public boolean insert() {

        Uri uri = AlarmApplication.contentResolver()
                .insert(CONTENT_URI, toContentValues());

        if (uri == null)
            return false;

        try {
            id = Long.parseLong(uri.getLastPathSegment());
        } catch (NumberFormatException nfe) {
            id = null;
        }

        return id != null;
    }

    public boolean delete() {
        return AlarmApplication.contentResolver()
                .delete(toUri(), null, null) > 0;
    }

    public boolean update() {
        return AlarmApplication.contentResolver()
                .update(toUri(), toContentValues(), null, null) > 0;
    }

    public ContentValues toContentValues() {

        ContentValues values = new ContentValues();

        values.put(AlarmEntry.COLUMN_NAME_ALARM_TITLE, getTitle());
        values.put(AlarmEntry.COLUMN_NAME_ALARM_AVAILABLE, getAvailable());

        if (getTimeOfDay() != null)
            values.put(AlarmEntry.COLUMN_NAME_ALARM_TIME_OF_DAY, getTimeOfDay().toInteger());

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

    public Uri toUri() {
        return Uri.parse(String.format("%s/%d", CONTENT_URI, getId()));
    }

    public boolean getDayOfWeek(Weekday weekday) {
        return dayOfWeek != null && (dayOfWeek & (1 << weekday.ordinal())) != 0;
    }

    public void setDayOfWeek(Weekday weekday, boolean flag) {
        if (dayOfWeek == null)
            dayOfWeek = 0;
        if (flag)
            setDayOfWeek(dayOfWeek | (1 << weekday.ordinal()));
        else
            setDayOfWeek(dayOfWeek & ~(1 << weekday.ordinal()));
    }

    @Nullable
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

    @Nullable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TimeOfDay getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(TimeOfDay timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    @Nullable
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Nullable
    public Double getLocationRadius() {
        return locationRadius;
    }

    public void setLocationRadius(Double locationRadius) {
        this.locationRadius = locationRadius;
    }

    @Nullable
    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    @Nullable
    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        if (dayOfWeek != null && dayOfWeek != 0)
            this.setRepeat(true);
        this.dayOfWeek = dayOfWeek;
    }

    public boolean getRepeat() {
        return repeat && getDayOfWeek() != null && getDayOfWeek() != 0;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    @Nullable
    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    /**
     * This is the data type for the alarm time of day.
     * @author EUYUIL
     * @version 0.0.20131028
     */
    public static class TimeOfDay {

        private int hour;
        private int minute;

        public TimeOfDay() {
        }

        public TimeOfDay(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }

        public TimeOfDay(String timeOfDay) {
            String[] splitted = timeOfDay.split(":");
            if (splitted.length != 2)
                throw new IllegalFormatConversionException(':', TimeOfDay.class);
            this.hour = Integer.valueOf(splitted[0]);
            this.minute = Integer.valueOf(splitted[1]);
        }

        public TimeOfDay(Date timeOfDay) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(timeOfDay);
            this.hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
            this.minute = calendar.get(GregorianCalendar.MINUTE);
        }

        public TimeOfDay(int integer) {
            this.hour = integer / 100;
            this.minute = integer % 100;
        }

        @Override
        public String toString() {
            return String.format("%02d:%02d", this.hour, this.minute);
        }

        public int toInteger() {
            return this.hour * 100 + this.minute;
        }

        public Date toDate() {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.set(GregorianCalendar.HOUR_OF_DAY, this.hour);
            calendar.set(GregorianCalendar.MINUTE, this.minute);
            return calendar.getTime();
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }
    }

    /**
    * Created by Yue on 13-10-28.
    */
    public static enum Weekday {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
    }
}

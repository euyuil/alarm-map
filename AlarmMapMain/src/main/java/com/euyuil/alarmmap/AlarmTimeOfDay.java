package com.euyuil.alarmmap;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.IllegalFormatConversionException;

/**
 * This is the data type for the alarm time of day.
 * @author EUYUIL
 * @version 0.0.20131028
 */

public class AlarmTimeOfDay {

    private int hour;
    private int minute;

    public AlarmTimeOfDay() {
    }

    public AlarmTimeOfDay(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public AlarmTimeOfDay(String timeOfDay) {
        String[] splitted = timeOfDay.split(":");
        if (splitted.length != 2)
            throw new IllegalFormatConversionException(':', AlarmTimeOfDay.class);
        this.hour = Integer.valueOf(splitted[0]);
        this.minute = Integer.valueOf(splitted[1]);
    }

    public AlarmTimeOfDay(Date timeOfDay) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(timeOfDay);
        this.hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
        this.minute = calendar.get(GregorianCalendar.MINUTE);
    }

    public AlarmTimeOfDay(int integer) {
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

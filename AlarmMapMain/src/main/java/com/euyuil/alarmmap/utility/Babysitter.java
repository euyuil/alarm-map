package com.euyuil.alarmmap.utility;

import android.net.Uri;

import com.euyuil.alarmmap.model.Alarm;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author Yue
 * @version 0.0.2014.01.12
 */
public class Babysitter {

    /**
     * Invoke this method internally in the provider, each time after the
     * alarm being inserted, updated, or deleted.
     * And if the system time settings are changed, this method should be
     * invoked, too. Including time and date changes, and time-zone changes.
     * Every time the system boots up, it should be invoked, too.
     * And if the Daylight Saving Time changes, you should call this method.
     * You can pass in the root URI of alarms, which means all the alarms
     * should be taken care of.
     * @param alarm The URI of the alarm, which should be taken care of.
     */
    public static void takeCareOf(Uri alarm) {
        // TODO Finish this method.
    }

    private static void takeCareOf(Alarm alarm) {

        // TODO Is it enabled?

        Date now = new Date();
        GregorianCalendar nowCalendar = new GregorianCalendar();
        nowCalendar.setTime(now);

        Date startTime;
        GregorianCalendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(now);
        startCalendar.set(GregorianCalendar.SECOND, 0);
        startCalendar.set(GregorianCalendar.MILLISECOND, 0);

        // Let's see if today's HH:mm has been reached.
        int nowHour = nowCalendar.get(GregorianCalendar.HOUR_OF_DAY);
        int nowMinute = nowCalendar.get(GregorianCalendar.MINUTE);
        int alarmHour = alarm.getTimeOfDay().getHour();
        int alarmMinute = alarm.getTimeOfDay().getMinute();
        if ((nowHour < alarmHour) || (nowHour == alarmHour && nowMinute < alarmMinute)) {
            // HH:mm of today hasn't been reached.
        } else {
            // HH:mm of today has been reached, so start from tomorrow.
            startCalendar.add(GregorianCalendar.DATE, 1);
        }
        startCalendar.set(GregorianCalendar.HOUR_OF_DAY, alarmHour);
        startCalendar.set(GregorianCalendar.MINUTE, alarmMinute);
        startTime = startCalendar.getTime();

        // Calculate the time for next ringing.
        if (!alarm.getRepeat()) {
            // startTime is fairly enough.
            // Will ring at startTime.
            // TODO Set timer
            return;
        }

        // alarm.getRepeat() == true
        for (int i = 0; i < 7; ++i) {
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(startTime);
            calendar.add(GregorianCalendar.DATE, i);
            int calendarWeekday = calendar.get(GregorianCalendar.DAY_OF_WEEK);
            Alarm.Weekday weekday = AlarmDateTimeUtility.calendarWeekdayToAlarmWeekday(calendarWeekday);
            if (alarm.getDayOfWeek(weekday)) {
                Date time = calendar.getTime();
                // TODO Set timer
                return;
            }
        }
    }
}

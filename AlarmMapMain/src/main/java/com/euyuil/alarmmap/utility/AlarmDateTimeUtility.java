package com.euyuil.alarmmap.utility;

import com.euyuil.alarmmap.Alarm;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utilities that help you register alarms in system.
 * TODO When timezone changes, will the alarms work?
 * @author EUYUIL
 * @version 0.0.20131023
 */

public class AlarmDateTimeUtility {

    public static final String TAG = "AlarmDateTimeUtility";

    public static int alarmWeekdayToCalendarWeekday(Alarm.Weekday weekday) {
        return weekday.ordinal() - Alarm.Weekday.SUNDAY.ordinal() + GregorianCalendar.SUNDAY;
    }

    public static Alarm.Weekday calendarWeekdayToAlarmWeekday(int weekday) {
        return Alarm.Weekday.values()[weekday - GregorianCalendar.SUNDAY + Alarm.Weekday.SUNDAY.ordinal()];
    }

    public static Date clearDateButPreserveTime(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(2000, GregorianCalendar.JANUARY, 1);
        return calendar.getTime();
    }

    public static Date getThisManyDaysAfterThatTime(int days, Date date) {
        GregorianCalendar nowCalendar = new GregorianCalendar();
        nowCalendar.setTime(date);
        nowCalendar.add(GregorianCalendar.DAY_OF_MONTH, days);
        return nowCalendar.getTime();
    }

    public static Date getTomorrowNowTime(Date now) {
        return getThisManyDaysAfterThatTime(1, now);
    }

    @SuppressWarnings("MagicConstant")
    public static Date getThatTimeOnThisDay(Date thisDay, Date thatTime) {
        GregorianCalendar thisDayCalendar = new GregorianCalendar();
        GregorianCalendar thatTimeCalendar = new GregorianCalendar();
        thisDayCalendar.setTime(thisDay);
        thatTimeCalendar.setTime(thatTime);
        thatTimeCalendar.set(
                thisDayCalendar.get(GregorianCalendar.YEAR),
                thisDayCalendar.get(GregorianCalendar.MONTH),
                thisDayCalendar.get(GregorianCalendar.DAY_OF_MONTH));
        return thatTimeCalendar.getTime();
    }

    public static boolean hasCorrespondingTimeTodayPassed(Date now, Date date) {
        // TODO Maybe we should set one NOW.
        return clearDateButPreserveTime(now).getTime() >=
                clearDateButPreserveTime(date).getTime();
    }

    public static Date getFirstOccurrenceOfThisTimeInTheFuture(Date now, Date date) {
        if (hasCorrespondingTimeTodayPassed(now, date))
            return getTomorrowNowTime(getThatTimeOnThisDay(now, date));
        return getThatTimeOnThisDay(now, date);
    }

    @SuppressWarnings("MagicConstant")
    public static Date getCorrespondingTimeOnThatFutureWeekday(Date now, Date date, Alarm.Weekday weekday) {
        Alarm.Weekday nowWeekday = getNowWeekday(now);
        if (nowWeekday == weekday) // TODO Future?
            return getThatTimeOnThisDay(now, date);
        int dayDiffFromThatDayToToday = weekday.ordinal() - nowWeekday.ordinal();
        if (dayDiffFromThatDayToToday < 0)
            dayDiffFromThatDayToToday += 7;
        return getThisManyDaysAfterThatTime(dayDiffFromThatDayToToday,
                getThatTimeOnThisDay(now, date));
    }

    public static Alarm.Weekday getNowWeekday(Date now) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(now);
        return calendarWeekdayToAlarmWeekday(calendar.get(GregorianCalendar.DAY_OF_WEEK));
    }

    public static Date getNextRingingDateTimeFromRepeatAlarm(Date now, Alarm alarm) {
        Alarm.Weekday nowWeekday = getNowWeekday(now);
        if (alarm.getDayOfWeek(nowWeekday) && !hasCorrespondingTimeTodayPassed(now, alarm.getTimeOfDay()))
            return getThatTimeOnThisDay(now, alarm.getTimeOfDay());
        Alarm.Weekday nextRingingWeekday;
        for (int i = 1; i <= 7; ++i) {
            nextRingingWeekday = Alarm.Weekday.values()[(nowWeekday.ordinal() + i) % 7];
            if (alarm.getDayOfWeek(nextRingingWeekday))
                return getThisManyDaysAfterThatTime(i,
                        getThatTimeOnThisDay(now, alarm.getTimeOfDay()));
        }
        throw new IllegalArgumentException("There's no any weekday set on the alarm, it's not repeat.");
    }
}

package com.euyuil.alarmmap.test.utility;

import com.euyuil.alarmmap.Alarm;
import com.euyuil.alarmmap.test.RobolectricGradleTestRunner;
import com.euyuil.alarmmap.utility.AlarmDateTimeUtility;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.GregorianCalendar;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Yue on 13-10-24.
 */

@RunWith(RobolectricGradleTestRunner.class)
public class AlarmDateTimeUtilityTest {

    @Test
    public void testAlarmWeekdayToCalendarWeekday() {

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(Alarm.Weekday.SUNDAY))
                .isEqualTo(GregorianCalendar.SUNDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(Alarm.Weekday.MONDAY))
                .isEqualTo(GregorianCalendar.MONDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(Alarm.Weekday.TUESDAY))
                .isEqualTo(GregorianCalendar.TUESDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(Alarm.Weekday.FRIDAY))
                .isEqualTo(GregorianCalendar.FRIDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(Alarm.Weekday.SATURDAY))
                .isEqualTo(GregorianCalendar.SATURDAY);
    }

    @Test
    public void testCalendarWeekdayToAlarmWeekday() {

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.SUNDAY))
                .isEqualTo(Alarm.Weekday.SUNDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.MONDAY))
                .isEqualTo(Alarm.Weekday.MONDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.TUESDAY))
                .isEqualTo(Alarm.Weekday.TUESDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.FRIDAY))
                .isEqualTo(Alarm.Weekday.FRIDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.SATURDAY))
                .isEqualTo(Alarm.Weekday.SATURDAY);


    }

    @Test
    public void testClearDateButPreserveTime() {

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());

        calendar.set(1999, GregorianCalendar.MARCH, 20);
        Date foo = calendar.getTime();

        calendar.set(2008, GregorianCalendar.AUGUST, 27);
        Date bar = calendar.getTime();

        assertThat(foo).isNotEqualTo(bar);

        assertThat(AlarmDateTimeUtility.clearDateButPreserveTime(foo))
                .isEqualTo(AlarmDateTimeUtility.clearDateButPreserveTime(bar));
    }

    @Test
    public void testGetThisManyDaysAfterThatTime() {

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(2000, GregorianCalendar.JANUARY, 1);
        Date foo = calendar.getTime();

        calendar.set(2000, GregorianCalendar.FEBRUARY, 1);
        Date bar = calendar.getTime();

        calendar.set(2001, GregorianCalendar.JANUARY, 1);
        Date baz = calendar.getTime();

        assertThat(AlarmDateTimeUtility
                .getThisManyDaysAfterThatTime(31, foo)).isEqualTo(bar);

        assertThat(AlarmDateTimeUtility
                .getThisManyDaysAfterThatTime(366, foo)).isEqualTo(baz);
    }

    @Test
    public void testGetTomorrowNowTime() {
        // TODO
    }

    @Test
    public void testGetThatTimeOnThisDay() {
        // TODO
    }

    @Test
    public void testHasCorrespondingTimeTodayPassed() {
        // TODO
    }

    @Test
    public void testGetFirstOccurrenceOfThisTimeInTheFuture() {
        // TODO
    }

    @Test
    public void testGetCorrespondingTimeOnThatFutureWeekend() {
        // TODO
    }

    @Test
    public void testGetNowWeekday() {

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(2013, GregorianCalendar.OCTOBER, 24);
        assertThat(AlarmDateTimeUtility
                .getNowWeekday(calendar.getTime())).isEqualTo(Alarm.Weekday.THURSDAY);

        calendar.set(2000, GregorianCalendar.FEBRUARY, 29);
        assertThat(AlarmDateTimeUtility
                .getNowWeekday(calendar.getTime())).isEqualTo(Alarm.Weekday.TUESDAY);
    }

    @Test
    public void testGetNextRingingDateTimeFromRepeatAlarm() {

        boolean except = false;
        Alarm alarm = new Alarm();
        GregorianCalendar calendar = new GregorianCalendar();

        // 2013-04-01 is a Monday.
        calendar.set(2013, GregorianCalendar.APRIL, 1, 12, 0, 0);

        alarm.setTimeOfDay(calendar.getTime());

        // Alarm is not available, expect exception.

        alarm.setAvailable(false);
        except = false;
        try {
            AlarmDateTimeUtility.getNextRingingDateTimeFromRepeatAlarm(new Date(), alarm);
        } catch (IllegalArgumentException iae) {
            except = true;
        }
        assertThat(except).isEqualTo(true);

        // Alarm is available, but is not repeat, expect exception.

        alarm.setAvailable(true);
        alarm.setRepeat(false);
        except = false;
        try {
            AlarmDateTimeUtility.getNextRingingDateTimeFromRepeatAlarm(new Date(), alarm);
        } catch (IllegalArgumentException iae) {
            except = true;
        }
        assertThat(except).isEqualTo(true);

        // Alarm is repeat, but no weekday is set, expect exception.

        alarm.setAvailable(true);
        alarm.setRepeat(true);
        alarm.setDayOfWeek(0);
        except = false;
        try {
            AlarmDateTimeUtility.getNextRingingDateTimeFromRepeatAlarm(new Date(), alarm);
        } catch (IllegalArgumentException iae) {
            except = true;
        }
        assertThat(except).isEqualTo(true);

        // TODO Repeat weekday includes today's, no other repeat weekday, now time is before alarm time, expect ringing today.

        // TODO Repeat weekday includes today's, no other repeat weekday, now time is after alarm time, expect ringing next week.

        // TODO Repeat weekday includes today's, now time is before alarm time, expect ringing today.

        // TODO Repeat weekday includes today's, now time is after alarm time, expect ringing next set weekday.

        // TODO Repeat weekday doesn't include today's, now time is before alarm time, expect ringing next set weekday.

        // TODO Repeat weekday doesn't include today's, now time is after alarm time, expect ringing next set weekday.
    }
}

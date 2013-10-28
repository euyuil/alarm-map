package com.euyuil.alarmmap.test.utility;

import com.euyuil.alarmmap.Alarm;
import com.euyuil.alarmmap.AlarmTimeOfDay;
import com.euyuil.alarmmap.AlarmWeekday;
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
                .alarmWeekdayToCalendarWeekday(AlarmWeekday.SUNDAY))
                .isEqualTo(GregorianCalendar.SUNDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(AlarmWeekday.MONDAY))
                .isEqualTo(GregorianCalendar.MONDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(AlarmWeekday.TUESDAY))
                .isEqualTo(GregorianCalendar.TUESDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(AlarmWeekday.FRIDAY))
                .isEqualTo(GregorianCalendar.FRIDAY);

        assertThat(AlarmDateTimeUtility
                .alarmWeekdayToCalendarWeekday(AlarmWeekday.SATURDAY))
                .isEqualTo(GregorianCalendar.SATURDAY);
    }

    @Test
    public void testCalendarWeekdayToAlarmWeekday() {

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.SUNDAY))
                .isEqualTo(AlarmWeekday.SUNDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.MONDAY))
                .isEqualTo(AlarmWeekday.MONDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.TUESDAY))
                .isEqualTo(AlarmWeekday.TUESDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.FRIDAY))
                .isEqualTo(AlarmWeekday.FRIDAY);

        assertThat(AlarmDateTimeUtility
                .calendarWeekdayToAlarmWeekday(GregorianCalendar.SATURDAY))
                .isEqualTo(AlarmWeekday.SATURDAY);
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

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(2013, GregorianCalendar.APRIL, 1, 12, 0, 0);

        // Now is 12:00 .
        Date now = calendar.getTime();

        // Corresponding time is 10:00 .
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 10);
        assertThat(AlarmDateTimeUtility
                .hasCorrespondingTimeTodayPassed(now, calendar.getTime())).isEqualTo(true);

        // Corresponding time is 14:00 .
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 14);
        assertThat(AlarmDateTimeUtility
                .hasCorrespondingTimeTodayPassed(now, calendar.getTime())).isEqualTo(false);

        // Corresponding time is 10:00 .
        calendar.set(2014, GregorianCalendar.APRIL, 1, 10, 0, 0);
        assertThat(AlarmDateTimeUtility
                .hasCorrespondingTimeTodayPassed(now, calendar.getTime())).isEqualTo(true);

        // Corresponding time is 14:00 .
        calendar.set(2012, GregorianCalendar.APRIL, 1, 14, 0, 0);
        assertThat(AlarmDateTimeUtility
                .hasCorrespondingTimeTodayPassed(now, calendar.getTime())).isEqualTo(false);

        // Corresponding time is 10:00 .
        calendar.set(2012, GregorianCalendar.APRIL, 1, 10, 0, 0);
        assertThat(AlarmDateTimeUtility
                .hasCorrespondingTimeTodayPassed(now, calendar.getTime())).isEqualTo(true);

        // Corresponding time is 14:00 .
        calendar.set(2014, GregorianCalendar.APRIL, 1, 14, 0, 0);
        assertThat(AlarmDateTimeUtility
                .hasCorrespondingTimeTodayPassed(now, calendar.getTime())).isEqualTo(false);
    }

    @Test
    public void testGetFirstOccurrenceOfThisTimeInTheFuture() {
        // TODO
    }

    @Test
    public void testGetCorrespondingTimeOnThatWeekdayAfterToday() {
        // TODO Test this first.
    }

    @Test
    public void testGetNowWeekday() {

        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(2013, GregorianCalendar.OCTOBER, 24);
        assertThat(AlarmDateTimeUtility
                .getNowWeekday(calendar.getTime())).isEqualTo(AlarmWeekday.THURSDAY);

        calendar.set(2000, GregorianCalendar.FEBRUARY, 29);
        assertThat(AlarmDateTimeUtility
                .getNowWeekday(calendar.getTime())).isEqualTo(AlarmWeekday.TUESDAY);
    }

    @Test
    public void testGetNextRingingDateTimeFromRepeatAlarm_ExceptionTests() {

        Alarm alarm = new Alarm();
        boolean except;

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
    }

    @Test
    public void testGetNextRingingDateTimeFromRepeatAlarm_ReturnValueTests() {

        Date now, expected;
        Alarm alarm = new Alarm();
        GregorianCalendar calendar = new GregorianCalendar();

        calendar.set(2013, GregorianCalendar.APRIL, 1, 12, 0, 0); // 2013-04-01 is a Monday.

        AlarmTimeOfDay alarmTime = new AlarmTimeOfDay(12, 0);
        alarm.setTimeOfDay(alarmTime); // Alarm time is 12:00 .

        // Repeat weekday includes today's, no other repeat weekday, now time is before alarm time, expect ringing today.
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 10);
        now = calendar.getTime(); // Now is 2013-04-01 10:00 .

        alarm.setAvailable(true);
        alarm.setRepeat(true);
        alarm.setDayOfWeek(0);
        alarm.setDayOfWeek(AlarmDateTimeUtility.getNowWeekday(now), true);
        expected = AlarmDateTimeUtility
                .getThatTimeOnThisDay(now, alarmTime.toDate()); // Today 12:00 .

        assertThat(AlarmDateTimeUtility
                .getNextRingingDateTimeFromRepeatAlarm(now, alarm))
                .isEqualTo(expected);

        // Repeat weekday includes today's, no other repeat weekday, now time is after alarm time, expect ringing next week.
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 14);
        now = calendar.getTime(); // Now is 2013-04-01 14:00 .

        alarm.setAvailable(true);
        alarm.setRepeat(true);
        alarm.setDayOfWeek(0);
        alarm.setDayOfWeek(AlarmDateTimeUtility.getNowWeekday(now), true);
        expected = AlarmDateTimeUtility
                .getThisManyDaysAfterThatTime(7, AlarmDateTimeUtility
                .getThatTimeOnThisDay(now, alarmTime.toDate())); // Next week 12:00 .

        assertThat(AlarmDateTimeUtility
                .getNextRingingDateTimeFromRepeatAlarm(now, alarm))
                .isEqualTo(expected);

        // Repeat weekday includes today's, now time is before alarm time, expect ringing today.
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 10);
        now = calendar.getTime(); // Now is 2013-04-01 10:00 .

        alarm.setAvailable(true);
        alarm.setRepeat(true);
        alarm.setDayOfWeek(0);
        alarm.setDayOfWeek(AlarmDateTimeUtility.getNowWeekday(now), true);
        alarm.setDayOfWeek(AlarmWeekday.WEDNESDAY, true);
        alarm.setDayOfWeek(AlarmWeekday.FRIDAY, true);
        expected = AlarmDateTimeUtility
                .getThatTimeOnThisDay(now, alarmTime.toDate()); // Today 12:00 .

        assertThat(AlarmDateTimeUtility
                .getNextRingingDateTimeFromRepeatAlarm(now, alarm))
                .isEqualTo(expected);

        // Repeat weekday includes today's, now time is after alarm time, expect ringing next set weekday.
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 14);
        now = calendar.getTime(); // Now is 2013-04-01 14:00 .

        alarm.setAvailable(true);
        alarm.setRepeat(true);
        alarm.setDayOfWeek(0);
        alarm.setDayOfWeek(AlarmDateTimeUtility.getNowWeekday(now), true);
        alarm.setDayOfWeek(AlarmWeekday.THURSDAY, true);
        alarm.setDayOfWeek(AlarmWeekday.FRIDAY, true);
        expected = AlarmDateTimeUtility // Next Thursday 12:00 .
                .getCorrespondingTimeOnThatWeekdayAfterToday(
                        now, alarmTime.toDate(), AlarmWeekday.THURSDAY);

        assertThat(AlarmDateTimeUtility
                .getNextRingingDateTimeFromRepeatAlarm(now, alarm))
                .isEqualTo(expected);

        // Repeat weekday doesn't include today's, now time is before alarm time, expect ringing next set weekday.
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 10);
        now = calendar.getTime(); // Now is 2013-04-01 10:00 .

        alarm.setAvailable(true);
        alarm.setRepeat(true);
        alarm.setDayOfWeek(0);
        alarm.setDayOfWeek(AlarmDateTimeUtility.getNowWeekday(now), false);
        alarm.setDayOfWeek(AlarmWeekday.WEDNESDAY, true);
        alarm.setDayOfWeek(AlarmWeekday.FRIDAY, true);
        expected = AlarmDateTimeUtility // Next Wednesday 12:00 .
                .getCorrespondingTimeOnThatWeekdayAfterToday(
                        now, alarmTime.toDate(), AlarmWeekday.WEDNESDAY);

        assertThat(AlarmDateTimeUtility
                .getNextRingingDateTimeFromRepeatAlarm(now, alarm))
                .isEqualTo(expected);

        // Repeat weekday doesn't include today's, now time is after alarm time, expect ringing next set weekday.
        calendar.set(GregorianCalendar.HOUR_OF_DAY, 14);
        now = calendar.getTime(); // Now is 2013-04-01 14:00 .

        alarm.setAvailable(true);
        alarm.setRepeat(true);
        alarm.setDayOfWeek(0);
        alarm.setDayOfWeek(AlarmDateTimeUtility.getNowWeekday(calendar.getTime()), false);
        alarm.setDayOfWeek(AlarmWeekday.THURSDAY, true);
        alarm.setDayOfWeek(AlarmWeekday.FRIDAY, true);
        expected = AlarmDateTimeUtility // Next Thursday 12:00 .
                .getCorrespondingTimeOnThatWeekdayAfterToday(
                        now, alarmTime.toDate(), AlarmWeekday.THURSDAY);

        assertThat(AlarmDateTimeUtility
                .getNextRingingDateTimeFromRepeatAlarm(now, alarm))
                .isEqualTo(expected);
    }

}

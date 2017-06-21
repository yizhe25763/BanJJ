package com.shzy.bjj.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	private static volatile int index = 0;

	public DateUtil() {
	}

	public static Date getDate() {
		return new Date();
	}

	public static String getStringDate(String pattern) {
		return format(new Date(), pattern);
	}

	public static String getStringDateTime() {
		return format(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	public static String getStringDateTime(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getStringTime() {
		return format(new Date(), "HH:mm:ss");
	}

	public static String getStringDate() {
		return format(new Date(), "yyyy-MM-dd");
	}

	public static String getStringDateUnique() {
		return getStringDate("yyyyMMddHHmmss" + index++);
	}

	public static Date getDate(Object value, String pattern) {
		if (StringTool.isNoBlankAndNoNull(pattern)) {
			return DataUtil.getDate(value, new SimpleDateFormat(pattern));
		} else {
			return DataUtil.getDate(value);
		}
	}

	public static boolean isLeapYear(int year) {
		return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
	}

	public static long getBeginOfDate(long time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		setBegin(calendar);
		time = calendar.getTime().getTime();
		return time;
	}

	public static Date getTodayBegin() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(12, 0);
		calendar.set(11, 0);
		calendar.set(13, 0);
		return calendar.getTime();
	}

	public static Date getTodayEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(12, 59);
		calendar.set(11, 23);
		calendar.set(13, 59);
		return calendar.getTime();
	}

	public static long getNextDay(long endTime) {
		return endTime;
	}

	public static Calendar getBeginOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, year);
		calendar.set(2, 0);
		calendar.set(5, 1);
		setBegin(calendar);
		return calendar;
	}

	public static Calendar getEndOfYear(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(1, year);
		calendar.set(2, 11);
		calendar.set(5, 31);
		setEnd(calendar);
		return calendar;
	}

	public static void setBegin(Calendar calendar) {
		calendar.set(11, 0);
		calendar.set(12, 0);
		calendar.set(13, 0);
		calendar.set(14, 0);
	}

	public static void setEnd(Calendar calendar) {
		calendar.set(11, 23);
		calendar.set(12, 59);
		calendar.set(13, 59);
		calendar.set(14, 999);
	}

	public static Date getDateAdd(Date date, int add, int type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(type, add);
		return calendar.getTime();
	}

	public static int getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(7);
	}

	public static Date toDate(String date) {
		String format = "yyyy-MM-dd HH:mm:ss";
		return toDate(date, format);
	}

	public static Date toDate(String date, String parrern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(parrern);

		try {
			return dateFormat.parse(date);
		} catch (ParseException var4) {
			var4.printStackTrace();
			return null;
		}
	}

	public static String format(Date date, String parrern) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(parrern);
		return dateFormat.format(date);
	}

	public static String toString(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		return dateFormat.format(date);
	}

	public static Date parseDate(String value, String style, Locale locale,
			TimeZone timeZone) {
		SimpleDateFormat format = new SimpleDateFormat(style, locale);
		if (timeZone != null) {
			format.setTimeZone(timeZone);
		}

		try {
			return format.parse(value);
		} catch (ParseException var6) {
			throw new IllegalArgumentException(
					"Date format error.The value is " + value);
		}
	}

	public static String formatDate(Date date, String style, Locale locale,
			TimeZone zone) {
		SimpleDateFormat format = new SimpleDateFormat(style, locale);
		format.setTimeZone(zone);
		return format.format(date);
	}

	public static Date toDate(long date) {
		return new Date(date);
	}

	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(6, day);
		return cal.getTime();
	}

	public static Date addMinute(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(12, minute);
		return cal.getTime();
	}

	public static Date addHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(10, hour);
		return cal.getTime();
	}

	public static Date addWeek(Date date, int week) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}

		cal.add(4, week);
		return cal.getTime();
	}

	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(2, month);
		return cal.getTime();
	}

	public static double getHourBetween(Date date1, Date date2) {
		return date1 != null && date2 != null ? (double) (date1.getTime() - date2
				.getTime()) / 3600000.0D : 0.0D;
	}

	public static long getSecondBetween(Date date1, Date date2) {
		return date1 != null && date2 != null ? (date1.getTime() - date2
				.getTime()) / 1000L : 0L;
	}

	public static int[] getTimeInSecond(int second) {
		int iHour = second / 3600;
		int iMin = (second - iHour * 3600) / 60;
		int iSen = second - iHour * 3600 - iMin * 60;
		return new int[] { iHour, iMin, iSen };
	}

	public static double getDayBetween(Date date1, Date date2) {
		return date1 != null && date2 != null ? (double) (date1.getTime() - date2
				.getTime()) / 8.64E7D : 0.0D;
	}

	public static double getWeekBetween(Date date1, Date date2) {
		return date1 != null && date2 != null ? (double) (date1.getTime() - date2
				.getTime()) / 8.64E7D / 7.0D : 0.0D;
	}

	public static double getMinuteBetween(Date date1, Date date2) {
		return date1 != null && date2 != null ? (double) (date1.getTime() - date2
				.getTime()) / 60000.0D : 0.0D;
	}

	public static double getYearBetween(Date date1, Date date2) {
		return date1 != null && date2 != null ? (double) (date1.getTime() - date2
				.getTime()) / 8.64E7D / 365.0D : 0.0D;
	}

	public static int getAge(Date birthDay) {
		Calendar cal = Calendar.getInstance();
		if (birthDay != null && !cal.before(birthDay)) {
			int yearNow = cal.get(1);
			int monthNow = cal.get(2) + 1;
			int dayOfMonthNow = cal.get(5);
			cal.setTime(birthDay);
			int yearBirth = cal.get(1);
			int monthBirth = cal.get(2);
			int dayOfMonthBirth = cal.get(5);
			int age = yearNow - yearBirth;
			if (monthNow <= monthBirth) {
				if (monthNow == monthBirth) {
					if (dayOfMonthNow < dayOfMonthBirth) {
						--age;
					}
				} else {
					--age;
				}
			}

			return age;
		} else {
			return 0;
		}
	}
}

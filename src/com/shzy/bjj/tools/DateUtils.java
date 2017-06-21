package com.shzy.bjj.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class DateUtils extends DateUtil {
	private static long dayMilliseconds = 1000 * 3600 * 24;
	private static DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static DateFormat dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd");
	private static DateFormat dateFormatHM = new SimpleDateFormat("HH:mm");

	public static String format(Date date) {
		if (null == date) {
			return "";
		}
		return dateFormat.format(date);
	}

	public static String formatYMD(Date date) {
		if (null == date) {
			return "";
		}
		return dateFormatYMD.format(date);
	}

	public static String formatHM(Date date) {
		if (null == date) {
			return "";
		}
		return dateFormatHM.format(date);
	}

	public static Date getDate(String value) {
		return DataUtil.getDate(value, dateFormatYMD);
	}

	public static long getDays(Date date) {
		long days = (System.currentTimeMillis() - date.getTime())
				/ dayMilliseconds;
		if (days < 1) {
			days = 1;
		}
		return days;
	}

	public static long getDays(String value) {
		Date date = getDate(value);
		if (null == date) {
			return 1;
		}
		return getDays(date);
	}

	public static int getMonths(String date) {
		return getMonths(getDate(date));
	}

	public static int getMonths(Date date) {
		return (int) Math.ceil(getDays(date) / 30.0f);
	}

	public static int getTypeMonthDefine(String date) {
		return getTypeMonthDefine(getMonths(date));
	}

	public static int getTypeMonthDefine(Date date) {
		return getTypeMonthDefine(getMonths(date));
	}

	public static int getTypeMonthDefine(int months) {
		if (months >= 25 && months <= 27) {
			months = 25;
		} else if (months > 27 && months <= 30) {
			months = 26;
		} else if (months > 30 && months <= 33) {
			months = 27;
		} else if (months > 33) {
			months = 28;
		}
		return months;
	}

}

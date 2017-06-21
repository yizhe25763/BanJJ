package com.shzy.bjj.activity.home;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.util.Log;

import com.shzy.bjj.bean.TeacherConditionBean;
import com.shzy.bjj.tools.StringTool;

public class DateChannel {

	public static String channelYear(String content) {
		if (!StringTool.isNoBlankAndNoNull(content)) {
			return "";
		} else {
			return content.substring(0, 4).toString().trim();

		}
	}

	public static String channelMonth(String content) {
		if (!StringTool.isNoBlankAndNoNull(content)) {
			return "";
		} else {
			return content.substring(5, 7).toString().trim();

		}
	}

	public static String channelDay(String content) {
		if (!StringTool.isNoBlankAndNoNull(content)) {
			return "";
		} else {
			return content.substring(8, 10).toString().trim();
		}
	}

	public static String channelStartTime(String content) {
		if (!StringTool.isNoBlankAndNoNull(content)) {
			return "";
		} else {
			return content.substring(11, 13).toString().trim();

		}
	}

	public static String channelEndTime(String content) {
		if (!StringTool.isNoBlankAndNoNull(content)) {
			return "";
		} else {
			return content.substring(31, 33).toString().trim();

		}
	}

	public static int channelTotal(String content) {
		return Integer.parseInt(channelEndTime(content))
				- Integer.parseInt(channelStartTime(content));

	}

	public static String dayFormate(String content) {
		return content.substring(0, 4).toString().trim() + "-"
				+ content.substring(5, 7).toString().trim() + "-"
				+ content.substring(8, 10).toString().trim();
	}

	public static String getWeekday(String date) {// 必须yyyy-MM-dd
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdw = new SimpleDateFormat("E");
		Date d = null;
		try {
			d = sd.parse(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return sdw.format(d);
	}

	public static String chooseTeacherDate(TeacherConditionBean request) {
		int sizes = request.getCondition_time().toString().trim().length();
		int startTime = Integer.valueOf(request.getCondition_time()
				.substring(11, 13).toString().trim());
		int endTime = Integer.valueOf(request.getCondition_time()
				.substring(31, 33).toString().trim());
		if (endTime - startTime == 2) {
			String startTimes = request.getCondition_time().substring(0, 19);
			String str = request.getCondition_time().substring(0, 11)
					+ (startTime + 1) + "-00-00";
			String endTimes = request.getCondition_time().substring(20, 39);
			String times = "(" + startTimes + "_" + str + " AND " + str + "_"
					+ endTimes + ")";
			return times;
		} else if (endTime - startTime == 3) {
			String startTimes = request.getCondition_time().substring(0, 19);
			String str = request.getCondition_time().substring(0, 11)
					+ (startTime + 1) + "-00-00";
			String strs = request.getCondition_time().substring(0, 11)
					+ (startTime + 2) + "-00-00";
			String endTimes = request.getCondition_time().substring(20, 39);
			String times = "((" + startTimes + "_" + str + " AND " + str + "_"
					+ strs + " AND " + strs + "_" + endTimes + "))";
			return times;
		} else if (endTime - startTime == 4) {
			String startTimes = request.getCondition_time().substring(0, 19);
			String str = request.getCondition_time().substring(0, 11)
					+ (startTime + 1) + "-00-00";
			String strs = request.getCondition_time().substring(0, 11)
					+ (startTime + 2) + "-00-00";
			String strss = request.getCondition_time().substring(0, 11)
					+ (startTime + 3) + "-00-00";
			String endTimes = request.getCondition_time().substring(20, 39);
			String times = "((" + startTimes + "_" + str + " AND " + str + "_"
					+ strs + " AND " + strs + "_" + strss + " AND " + strss
					+ "_" + endTimes + "))";
			return times;
		} else if (endTime - startTime == 1) {
			return request.getCondition_time();
		}
		return "";
	}

	

}

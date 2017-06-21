package com.shzy.bjj.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.util.Log;

import com.shzy.bjj.constant.DataConst;

/**
 * 
 * @brief 时间工具类
 * @author Fanhao.Yi
 * @date 2015年4月23日下午6:56:53
 * @version V1.0
 */
public class DateTimeTool {

	/** 日期格式：yyyy-MM-dd HH:mm:ss **/
	public static final String DF_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	/** 日期格式：yyyy-MM-dd HH:mm **/
	public static final String DF_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

	/** 日期格式：yyyy-MM-dd **/
	public static final String DF_YYYY_MM_DD = "yyyy-MM-dd";

	/** 日期格式：HH:mm:ss **/
	public static final String DF_HH_MM_SS = "HH:mm:ss";

	/** 日期格式：HH:mm **/
	public static final String DF_HH_MM = "HH:mm";

	/** 1分钟 **/
	private final static long minute = 60 * 1000;

	/** 1小时 **/
	private final static long hour = 60 * minute;

	/** 1天 **/
	private final static long day = 24 * hour;

	/** 月 **/
	private final static long month = 31 * day;

	/** 年 **/
	private final static long year = 12 * month;

	/** Log输出标识 **/
	private static final String TAG = DateTimeTool.class.getSimpleName();

	/**
	 * 将日期格式化成友好的字符串：几分钟前、几小时前、几天前、几月前、几年前、刚刚
	 * 
	 * @param date
	 * @return
	 */
	public static String formatFriendly(Date date) {
		if (date == null) {
			return null;
		}
		long diff = new Date().getTime() - date.getTime();
		long r = 0;
		if (diff > year) {
			r = (diff / year);
			return r + "年前";
		}
		if (diff > month) {
			r = (diff / month);
			return r + "个月前";
		}
		if (diff > day) {
			r = (diff / day);
			return r + "天前";
		}
		if (diff > hour) {
			r = (diff / hour);
			return r + "个小时前";
		}
		if (diff > minute) {
			r = (diff / minute);
			return r + "分钟前";
		}
		return "刚刚";
	}

	/**
	 * 将日期以yyyy-MM-dd HH:mm:ss格式化
	 * 
	 * @param dateL
	 *            日期
	 * @return
	 */
	public static String formatDateTime(long dateL) {
		SimpleDateFormat sdf = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
		Date date = new Date(dateL);
		return sdf.format(date);
	}

	/**
	 * 将日期以yyyy-MM-dd HH:mm:ss格式化
	 * 
	 * @param dateL
	 *            日期
	 * @return
	 */
	public static String formatDateTime(long dateL, String formater) {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(new Date(dateL));
	}

	/**
	 * 将日期以yyyy-MM-dd HH:mm:ss格式化
	 * 
	 * @param dateL
	 *            日期
	 * @return
	 */
	public static String formatDateTime(Date date, String formater) {
		SimpleDateFormat sdf = new SimpleDateFormat(formater);
		return sdf.format(date);
	}

	/**
	 * 将日期字符串转成日期
	 * 
	 * @param strDate
	 *            字符串日期
	 * @return java.util.date日期类型
	 */
	public static Date parseDate(String strDate) {
		DateFormat dateFormat = new SimpleDateFormat(DF_YYYY_MM_DD_HH_MM_SS);
		Date returnDate = null;
		try {
			returnDate = dateFormat.parse(strDate);
		} catch (ParseException e) {
			Log.v(TAG, "parseDate failed !");

		}
		return returnDate;

	}

	/**
	 * 获取系统当前日期
	 * 
	 * @return
	 */
	public static Date gainCurrentDate() {
		return new Date();
	}

	/**
	 * 获取系统当前日期
	 * 
	 * @return
	 */
	public static String gainCurrentDate(String formater) {
		return formatDateTime(new Date(), formater);
	}

	/**
	 * 验证日期是否比当前日期早
	 * 
	 * @param target1
	 *            比较时间1
	 * @param target2
	 *            比较时间2
	 * @return true 则代表target1比target2晚，否则比target2早
	 */
	public static boolean compareDate(Date target1, Date target2) {
		boolean flag = false;
		try {
			String target1DateTime = DateTimeTool.formatDateTime(target1,
					DF_YYYY_MM_DD_HH_MM_SS);
			String target2DateTime = DateTimeTool.formatDateTime(target2,
					DF_YYYY_MM_DD_HH_MM_SS);
			if (target1DateTime.compareTo(target2DateTime) < 0) {
				flag = true;
			}
		} catch (Exception e1) {
			System.out.println("比较失败，原因：" + e1.getMessage());
		}
		return flag;
	}

	/**
	 * 对日期进行增加操作
	 * 
	 * @param target
	 *            需要进行运算的日期
	 * @param hour
	 *            小时
	 * @return
	 */
	public static Date addDateTime(Date target, double hour) {
		if (null == target || hour < 0) {
			return target;
		}

		return new Date(target.getTime() + (long) (hour * 60 * 60 * 1000));
	}

	/**
	 * 对日期进行相减操作
	 * 
	 * @param target
	 *            需要进行运算的日期
	 * @param hour
	 *            小时
	 * @return
	 */
	public static Date subDateTime(Date target, double hour) {
		if (null == target || hour < 0) {
			return target;
		}

		return new Date(target.getTime() - (long) (hour * 60 * 60 * 1000));
	}

	/**
	 * 日期转换为当天 明天 后天 周几
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateDetail(String date) {
		Calendar today = Calendar.getInstance();
		Calendar target = Calendar.getInstance();

		DateFormat df = new SimpleDateFormat(DF_YYYY_MM_DD);
		try {
			today.setTime(df.parse(getNowDateToStr()));
			today.set(Calendar.HOUR, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);
			target.setTime(df.parse(date));
			target.set(Calendar.HOUR, 0);
			target.set(Calendar.MINUTE, 0);
			target.set(Calendar.SECOND, 0);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		long intervalMilli = target.getTimeInMillis() - today.getTimeInMillis();
		int xcts = (int) (intervalMilli / (24 * 60 * 60 * 1000));
		return showDateDetail(xcts, target);

	}

	private static String getNowDateToStr() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * 将日期差显示为日期或者星期
	 * 
	 * @param xcts
	 * @param target
	 * @return
	 */
	private static String showDateDetail(int xcts, Calendar target) {
		switch (xcts) {
		case 0:
			return DataConst.TODAY;
		case 1:
			return DataConst.TOMORROW;
		case 2:
			return DataConst.AFTER_TOMORROW;
		case -1:
			return DataConst.YESTERDAY;
		case -2:
			return DataConst.BEFORE_YESTERDAY;
		default:
			int dayForWeek = 0;
			dayForWeek = target.get(Calendar.DAY_OF_WEEK);
			switch (dayForWeek) {
			case 1:
				return DataConst.SUNDAY;
			case 2:
				return DataConst.MONDAY;
			case 3:
				return DataConst.TUESDAY;
			case 4:
				return DataConst.WEDNESDAY;
			case 5:
				return DataConst.THURSDAY;
			case 6:
				return DataConst.FRIDAY;
			case 7:
				return DataConst.SATURDAY;
			default:
				return null;
			}

		}
	}

	/**
	 * 换算生日 工作年限
	 * 
	 * @param birth
	 *            宝宝时间
	 * @return 对应的宝宝生日时间的 年月
	 */
	public static int getWork(String birth) {
		if (StringTool.isNoBlankAndNoNull(birth)) {
			Date nowDate = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date birthDate;
			try {
				birthDate = format.parse(birth);
			} catch (ParseException e) {
				birthDate = new Date();
				e.printStackTrace();
			}
			Calendar flightCal = Calendar.getInstance();
			flightCal.setTime(nowDate);
			Calendar birthCal = Calendar.getInstance();
			birthCal.setTime(birthDate);
			int y = flightCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
			int m = flightCal.get(Calendar.MONTH)
					- birthCal.get(Calendar.MONTH);
			int d = flightCal.get(Calendar.DATE) - birthCal.get(Calendar.DATE);
			if (y < 0) {
				y = 0;
			} else {
				if (d < 0) {
					m--;
				}
				if (m < 0) {
					y--;
				}
			}
			if (m > 0) {
				if (y > 0) {
					return y;

				} else {
					return 0;

				}
			} else {
				return y;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 换算宝宝生日时间
	 * 
	 * @param birth
	 *            宝宝时间
	 * @return 对应的宝宝生日时间的 年月
	 */
//	public static String getAgeByBirthDay(String birth) {
//		if (StringTool.isNoBlankAndNoNull(birth)) {
//			Date nowDate = new Date();
//			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//			Date birthDate;
//			try {
//				birthDate = format.parse(birth);
//			} catch (ParseException e) {
//				birthDate = new Date();
//				e.printStackTrace();
//			}
//			Calendar flightCal = Calendar.getInstance();
//			flightCal.setTime(nowDate);
//			Calendar birthCal = Calendar.getInstance();
//			birthCal.setTime(birthDate);
//			int y = flightCal.get(Calendar.YEAR) - birthCal.get(Calendar.YEAR);
//			int m = flightCal.get(Calendar.MONTH)
//					- birthCal.get(Calendar.MONTH);
//			int d = flightCal.get(Calendar.DATE) - birthCal.get(Calendar.DATE);
//			Log.e("y", y + "1");
//			Log.e("m", m + "-3");
//			Log.e("d", d + "0");
//
//			if (y < 0) {
//				y = 0;
//			} else {
//				if (d < 0) {
//					m--;
//				}
//				if (m < 0) {
//					y--;
//				}
//			}
//			if (m > 0) {
//				if (y > 0) {
//					return y + "岁" + m + "个月";
//
//				} else {
//					return m + "个月";
//
//				}
//			} else {
//				return y + "岁";
//			}
//		} else {
//			return "0岁";
//		}
//	}


	/**
	 * 换算宝宝生日时间
	 * 
	 * @param birth
	 *            宝宝时间
	 * @return 对应的宝宝生日时间的 年月
	 */
	public static String channelBrithday(String birth) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String brithday = new String();
		try {
			Date birthDay = sdf.parse(birth);
			Date now = new Date();
			Long betweenDay = getBetweenDay(birthDay, now); // 孩子出生到现在的天数
			// 对孩子的年份求值
			int year = (int) (betweenDay / 365);
			betweenDay = betweenDay - year * 365; // 此时间隔日期是一年以内的天数
			int month = (int) (betweenDay / 30);
			// betweenDay = betweenDay - month * 30; // 此时间隔日期是一月以内的天数
			// int week = (int) (betweenDay / 7);
			if (year > 0) {
				brithday = year + "岁" + month + "个月";
			} else {
				brithday = month + "个月";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return brithday;
	}

	public static Long getBetweenDay(Date date1, Date date2) {
		Long days = Long.valueOf(0L);
		if (date1.getTime() > date2.getTime())
			days = Long.valueOf(date1.getTime() - date2.getTime());
		else
			days = Long.valueOf(date2.getTime() - date1.getTime());
		return Long.valueOf(days.longValue() / 60L / 60L / 1000L / 24L);
	}
}

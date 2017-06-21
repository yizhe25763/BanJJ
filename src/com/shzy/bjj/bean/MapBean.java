package com.shzy.bjj.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.shzy.bjj.MyApplication;
import com.shzy.bjj.tools.StringTool;
import com.shzy.bjj.tools.ToastTool;

public class MapBean implements Serializable {
	private boolean isEnable;
	private String title;
	private String name;
	private int id;
	private boolean flag;
	private boolean isTime;

	public boolean isTime() {
		return isTime;
	}

	public void setTime(boolean isTime) {
		this.isTime = isTime;
	}

	static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	static Calendar calendar = Calendar.getInstance();

	public MapBean() {
	}

	public MapBean(boolean flag, boolean isEnable) {
		this.flag = flag;
		this.isEnable = isEnable;
	}

	public MapBean(boolean flag) {
		this.flag = flag;
	}

	public MapBean(String name, boolean flag) {
		this.name = name;
		this.flag = flag;
	}

	public MapBean(String name, boolean flag, String title) {
		this.name = name;
		this.flag = flag;
		this.title = title;
	}

	public MapBean(String name) {
		this.name = name;
	}

	public MapBean(String name, String title) {
		this.name = name;
		this.title = title;
	}

	public MapBean(String name, String title, int id) {
		this.name = name;
		this.title = title;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * 老师查询（排序）
	 * 
	 * @return
	 */
	public static List<MapBean> parseSort() {
		List<MapBean> list = new ArrayList<MapBean>();
		list.add(new MapBean("人气(接单数)", "2"));
		list.add(new MapBean("评价(综合评分)", "3"));
		list.add(new MapBean("服务质量", "4"));
		list.add(new MapBean("价格", "5"));
		return list;
	}

	/**
	 * 老师查询（技能）
	 * 
	 * @return
	 */
	public static List<MapBean> parseSkill() {
		List<MapBean> list = new ArrayList<MapBean>();
		list.add(new MapBean("手工", false, "1"));
		list.add(new MapBean("美术", false, "2"));
		list.add(new MapBean("舞蹈", false, "3"));
		list.add(new MapBean("唱歌", false, "4"));
		list.add(new MapBean("讲故事", false, "5"));
		list.add(new MapBean("乐器", false, "6"));
		list.add(new MapBean("拼音", false, "7"));
		list.add(new MapBean("数学", false, "8"));
		list.add(new MapBean("右脑", false, "9"));
		list.add(new MapBean("建构", false, "10"));
		list.add(new MapBean("识字", false, "11"));
		return list;
	}

	/**
	 * 教师详情日历 时间标头
	 * 
	 * @return
	 */
	public static List<MapBean> parseTeacherTime() {
		List<MapBean> list = new ArrayList<MapBean>();
		list.add(new MapBean("09-10", "0"));
		list.add(new MapBean("10-11", "1"));
		list.add(new MapBean("11-12", "2"));
		list.add(new MapBean("14-15", "3"));
		list.add(new MapBean("15-16", "4"));
		list.add(new MapBean("16-17", "5"));
		list.add(new MapBean("17-18", "6"));
		list.add(new MapBean("19-20", "7"));
		list.add(new MapBean("20-21", "8"));
		return list;
	}

	/**
	 * 教师详情日历
	 * 
	 * @return
	 */
	public static List<MapBean> parseTeacherToday() {
		List<MapBean> list = new ArrayList<MapBean>();
		for (int i = 0; i < 9; i++) {
			list.add(new MapBean(false));
		}
		return list;
	}

	/**
	 * 日历数据(老师列表/老师预约日历)
	 * 
	 * @param teacherBean
	 * @param titleList
	 * @return
	 */
	public static List<List<MapBean>> getData(
			List<TeacherScheduleData> scheduleList, List<MapBean> titleList,
			Context context) {
		List<List<MapBean>> list = new ArrayList<List<MapBean>>();
		list.add(getCalendarTitle());
		for (int i = 0; i < 7; i++) {
			list.add(getDay(scheduleList));
		}
		if (scheduleList != null) {
			List<ServiceTimeBean> serviceTimeLists = new ArrayList<ServiceTimeBean>();
			List<ServiceTimeIdBean> serviceTimeIdLists = new ArrayList<ServiceTimeIdBean>();

			for (int i = 0, len = scheduleList.size(); i < len; i++) {
				serviceTimeLists.addAll(scheduleList.get(i)
						.getService_time_list());
				serviceTimeIdLists.addAll(scheduleList.get(i)
						.getService_time_id_list());
			}
			int len = serviceTimeLists.size();
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					ServiceTimeBean bean = serviceTimeLists.get(i);
					String day = bean.getService_time().substring(0, 10);
					int time = Integer.parseInt(bean.getService_time()
							.substring(11, 13));
					for (int j = 0, length = titleList.size(); j < length; j++) {
						if (day.equalsIgnoreCase(titleList.get(j).getName())) {
							int pose = j;
							int position = getTimePosition(time);
							// 允许选
							list.get(pose).get(position).setEnable(true);
							// timeID
							list.get(pose)
									.get(position)
									.setId(serviceTimeIdLists.get(i)
											.getService_time_id());
							break;
						}
					}
				}
			}

		}
		return list;
	}

	/**
	 * 老师排班表
	 * 
	 * @param schedule
	 * @param titleList
	 * @param context
	 * @return
	 */
	public static List<List<MapBean>> getScheduleData(
			List<TeacherScheduleData> scheduleList, List<MapBean> titleList,
			Context context) {
		List<List<MapBean>> list = new ArrayList<List<MapBean>>();
		list.add(getCalendarTitle());
		for (int i = 0; i < 7; i++) {
			list.add(getDay(scheduleList));
		}
		if (scheduleList != null) {
			List<ServiceTimeBean> serviceTimeLists = new ArrayList<ServiceTimeBean>();
			List<ServiceTimeIdBean> serviceTimeIdLists = new ArrayList<ServiceTimeIdBean>();

			for (int i = 0, len = scheduleList.size(); i < len; i++) {
				serviceTimeLists.addAll(scheduleList.get(i)
						.getService_time_list());
				serviceTimeIdLists.addAll(scheduleList.get(i)
						.getService_time_id_list());
			}
			int len = serviceTimeLists.size();
			if (len > 0) {
				for (int i = 0; i < len; i++) {
					ServiceTimeBean bean = serviceTimeLists.get(i);
					String day = bean.getService_time().substring(0, 10);
					int time = Integer.parseInt(bean.getService_time()
							.substring(11, 13));
					for (int j = 0, length = titleList.size(); j < length; j++) {
						if (day.equalsIgnoreCase(titleList.get(j).getName())) {
							int pose = j;
							int position = getTimePosition(time);
							// 允许选
							list.get(pose).get(position).setEnable(true);
							// timeID
							list.get(pose)
									.get(position)
									.setId(serviceTimeIdLists.get(i)
											.getService_time_id());
							break;
						}
					}
				}
			}

		}
		return list;
	}

	public static String getDate(String time) {
		String result = time;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		if (StringTool.isNoBlankAndNoNull(time)) {
			Date date;
			try {
				date = dateFormat.parse(time);
				result = simpleDateFormat.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 根据服务时间，确定服务ID
	 * 
	 * @param time
	 * @return
	 */
	public static int getTimePosition(int time) {
		int position = 0;
		switch (time) {
		case 9:
			position = 0;
			break;

		case 10:
			position = 1;
			break;

		case 11:
			position = 2;
			break;

		case 14:
			position = 3;
			break;

		case 15:
			position = 4;
			break;

		case 16:
			position = 5;
			break;

		case 17:
			position = 6;
			break;

		case 19:
			position = 7;
			break;

		case 20:
			position = 8;
			break;

		default:
			break;
		}
		return position;
	}

	/**
	 * 日历的每天数据
	 * 
	 * @param teacherBean
	 * @return
	 */
	public static List<MapBean> getDay(List<TeacherScheduleData> scheduleList) {
		List<MapBean> list = new ArrayList<MapBean>();
		MapBean bean = null;
		for (int i = 0; i < 9; i++) {
			if (scheduleList == null) {
				bean = new MapBean(false, true);
			} else {
				bean = new MapBean(false, false);
			}
			list.add(bean);
		}
		return list;
	}

	/**
	 * 日历时间标头
	 * 
	 * @return
	 */
	public static List<MapBean> getCalendarTitle() {
		List<MapBean> list = new ArrayList<MapBean>();
		list.add(new MapBean("09至", "10点"));
		list.add(new MapBean("10至", "11点"));
		list.add(new MapBean("11至", "12点"));
		list.add(new MapBean("14至", "15点"));
		list.add(new MapBean("15至", "16点"));
		list.add(new MapBean("16至", "17点"));
		list.add(new MapBean("17至", "18点"));
		list.add(new MapBean("19至", "20点"));
		list.add(new MapBean("20至", "21点"));
		return list;
	}

	/**
	 * 日历 天 标头
	 * 
	 * @return
	 */
	public static List<MapBean> getCalendarHead(Date date) {

		List<MapBean> list = new ArrayList<MapBean>();
		list.add(new MapBean("时间", "选择"));
		calendar.setTime(date);
		list.add(getDate());
		list.add(getDate());
		list.add(getDate());
		list.add(getDate());
		list.add(getDate());
		list.add(getDate());
		list.add(getDate());
		return list;
	}

	public static MapBean getDate() {
		MapBean bean = new MapBean();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		String date = format.format(calendar.getTime());
		int week = calendar.get(Calendar.DAY_OF_WEEK);

		bean.setTitle(String.valueOf(day));
		bean.setName(date);
		bean.setId(week);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return bean;
	}

	/**
	 * 转换周
	 * 
	 * @param week
	 * @return
	 */
	public static String getWeek(int week) {
		switch (week) {
		case 7:
			return "六";
		case 1:
			return "日";
		case 2:
			return "一";
		case 3:
			return "二";
		case 4:
			return "三";
		case 5:
			return "四";
		case 6:
			return "五";
		default:
			return "";
		}
	}

	/**
	 * 老师列表，提交订单，获取时间和timeID
	 * 
	 * @param dataList
	 * @param titleList
	 * @return
	 */
	public static List<MapBean> getCalendarTimes(List<List<MapBean>> dataList,
			List<MapBean> titleList) {
		List<MapBean> result = new ArrayList<MapBean>();
		for (int i = 0, length = dataList.size(); i < length; i++) {
			if (i == 0) {
				continue;
			} else {
				List<MapBean> list = dataList.get(i);
				for (int j = 0, len = list.size(); j < len; j++) {
					if (list.get(j).isFlag()) {
						String startTime = getDateTime(i, j, titleList, true);
						String endTime = getDateTime(i, j, titleList, false);
						int timeID = dataList.get(i).get(j).getId();
						result.add(new MapBean(startTime, endTime, timeID));
					}
				}
			}
		}
		return result;
	}

	public static String getDateTime(int i, int j, List<MapBean> titleList,
			boolean flag) {
		String day = titleList.get(i).getName();
		String time = getTime(day, j, flag);
		return time;
	}

	public static String getTime(String date, int j, boolean flag) {
		String result = date;
		switch (j) {
		case 0:
			if (flag)
				result = date + " 09:00:00";
			else
				result = date + " 10:00:00";
			break;

		case 1:
			if (flag)
				result = date + " 10:00:00";
			else
				result = date + " 11:00:00";
			break;
		case 2:
			if (flag)
				result = date + " 11:00:00";
			else
				result = date + " 12:00:00";
			break;
		case 3:
			if (flag)
				result = date + " 14:00:00";
			else
				result = date + " 15:00:00";
			break;
		case 4:
			if (flag)
				result = date + " 15:00:00";
			else
				result = date + " 16:00:00";
			break;
		case 5:
			if (flag)
				result = date + " 16:00:00";
			else
				result = date + " 17:00:00";
			break;
		case 6:
			if (flag)
				result = date + " 17:00:00";
			else
				result = date + " 18:00:00";
			break;
		case 7:
			if (flag)
				result = date + " 19:00:00";
			else
				result = date + " 20:00:00";
			break;
		case 8:
			if (flag)
				result = date + " 20:00:00";
			else
				result = date + " 21:00:00";
			break;
		default:
			break;
		}
		return result;
	}

	/**
	 * 判断用户24小时内不可预约
	 * 
	 * @return
	 */
	public static int getCurrentTime(MyApplication application) {
		SimpleDateFormat format = new SimpleDateFormat("HH");
		Date nowDate = ConfigResponse.getMyServerTime(application);
		String date = format.format(nowDate);
		return Integer.parseInt(date);
	}

	/**
	 * 判断用户24小时内不可预约
	 * 
	 * @return
	 */
	public static int getTimePositionByTime(int start) {
		if (start < 9) {
			return -1;
		} else if (start > 20) {
			return 8;
		} else {
			switch (start) {
			case 9:
				return 0;
			case 10:
				return 1;
			case 11:
			case 12:
			case 13:
				return 2;
			case 14:
				return 3;
			case 15:
				return 4;
			case 16:
				return 5;
			case 17:
			case 18:
				return 6;
			case 19:
				return 7;
			case 20:
				return 8;
			}
		}
		return 0;
	}
}

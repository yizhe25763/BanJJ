package com.shzy.bjj.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shzy.bjj.tools.StringTool;

public class OrderDetailCountBean implements Serializable {
	private int service_grade_1_id;
	private int service_grade_2_id;
	private int service_time_id;
	private int service_unit_price;
	private String service_start_time;
	private String service_end_time;
	private String teacher_id;
	private String teacher_name;
	// 订单明细编号
	private String order_detail_number;
	private String memo;

	private int schedule_status;
	private int is_comment;

	public int getSchedule_status() {
		return schedule_status;
	}

	public void setSchedule_status(int schedule_status) {
		this.schedule_status = schedule_status;
	}

	public int getIs_comment() {
		return is_comment;
	}

	public void setIs_comment(int is_comment) {
		this.is_comment = is_comment;
	}

	public int getService_grade_1_id() {
		return service_grade_1_id;
	}

	public void setService_grade_1_id(int service_grade_1_id) {
		this.service_grade_1_id = service_grade_1_id;
	}

	public int getService_grade_2_id() {
		return service_grade_2_id;
	}

	public void setService_grade_2_id(int service_grade_2_id) {
		this.service_grade_2_id = service_grade_2_id;
	}

	public int getService_time_id() {
		return service_time_id;
	}

	public void setService_time_id(int service_time_id) {
		this.service_time_id = service_time_id;
	}

	public int getService_unit_price() {
		return service_unit_price;
	}

	public void setService_unit_price(int service_unit_price) {
		this.service_unit_price = service_unit_price;
	}

	public String getService_start_time() {
		return service_start_time;
	}

	public void setService_start_time(String service_start_time) {
		this.service_start_time = service_start_time;
	}

	public String getService_end_time() {
		return service_end_time;
	}

	public void setService_end_time(String service_end_time) {
		this.service_end_time = service_end_time;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	public String getOrder_detail_number() {
		return order_detail_number;
	}

	public void setOrder_detail_number(String order_detail_number) {
		this.order_detail_number = order_detail_number;
	}

	public static String getOrderTime(String startTime, String endTime) {
		StringBuilder builder = new StringBuilder();
		String date = getDate(startTime);
		builder.append(date);
		builder.append(" ");
		builder.append(startTime.substring(11, 13) + "点至"
				+ endTime.substring(11, 13) + "点");
		return builder.toString().trim();
	}

	public static String getDate(String time) {
		String result = time;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
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
}

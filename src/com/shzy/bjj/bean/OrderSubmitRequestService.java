package com.shzy.bjj.bean;

import java.io.Serializable;

public class OrderSubmitRequestService implements Serializable {
	private int service_grade_1_id;
	private int service_grade_2_id;
	private int service_time_id;
	private String service_grade_1_name;
	private String service_grade_2_name;
	private int service_unit_price;
	private String service_start_time;
	private String service_end_time;
	private String memo;

	public OrderSubmitRequestService(int service_grade_1_id,
			int service_grade_2_id, int service_time_id,
			String service_grade_1_name, String service_grade_2_name,
			int service_unit_price, String service_start_time,
			String service_end_time, String memo) {
		this.service_grade_1_id = service_grade_1_id;
		this.service_grade_2_id = service_grade_2_id;
		this.service_time_id = service_time_id;
		this.service_grade_1_name = service_grade_1_name;
		this.service_grade_2_name = service_grade_2_name;
		this.service_unit_price = service_unit_price;
		this.service_start_time = service_start_time;
		this.service_end_time = service_end_time;
		this.memo = memo;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public void setService_time_id(int service_time_id) {
		this.service_time_id = service_time_id;
	}

	public String getService_grade_1_name() {
		return service_grade_1_name;
	}

	public void setService_grade_1_name(String service_grade_1_name) {
		this.service_grade_1_name = service_grade_1_name;
	}

	public String getService_grade_2_name() {
		return service_grade_2_name;
	}

	public void setService_grade_2_name(String service_grade_2_name) {
		this.service_grade_2_name = service_grade_2_name;
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

}

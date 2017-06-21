package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class TeacherScheduleData implements Serializable {
	private String date;
	private int service_time_count;
	private List<ServiceTimeBean> service_time_list;
	private int service_time_id_count;
	private List<ServiceTimeIdBean> service_time_id_list;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getService_time_count() {
		return service_time_count;
	}

	public void setService_time_count(int service_time_count) {
		this.service_time_count = service_time_count;
	}

	public List<ServiceTimeBean> getService_time_list() {
		return service_time_list;
	}

	public void setService_time_list(List<ServiceTimeBean> service_time_list) {
		this.service_time_list = service_time_list;
	}

	public int getService_time_id_count() {
		return service_time_id_count;
	}

	public void setService_time_id_count(int service_time_id_count) {
		this.service_time_id_count = service_time_id_count;
	}

	public List<ServiceTimeIdBean> getService_time_id_list() {
		return service_time_id_list;
	}

	public void setService_time_id_list(
			List<ServiceTimeIdBean> service_time_id_list) {
		this.service_time_id_list = service_time_id_list;
	}

}

package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class TeacherScheduleResponse implements Serializable {
	private int date_count;
	private List<TeacherScheduleData> date_list;

	public int getDate_count() {
		return date_count;
	}

	public void setDate_count(int date_count) {
		this.date_count = date_count;
	}

	public List<TeacherScheduleData> getDate_list() {
		return date_list;
	}

	public void setDate_list(List<TeacherScheduleData> date_list) {
		this.date_list = date_list;
	}

}

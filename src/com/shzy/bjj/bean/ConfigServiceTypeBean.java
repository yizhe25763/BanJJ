package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class ConfigServiceTypeBean implements Serializable {
	private int id;
	private String name;
	private int service_grade_2_count;
	private List<ConfigServiceNameBean> service_grade_2_list;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getService_grade_2_count() {
		return service_grade_2_count;
	}

	public void setService_grade_2_count(int service_grade_2_count) {
		this.service_grade_2_count = service_grade_2_count;
	}

	public List<ConfigServiceNameBean> getService_grade_2_list() {
		return service_grade_2_list;
	}

	public void setService_grade_2_list(
			List<ConfigServiceNameBean> service_grade_2_list) {
		this.service_grade_2_list = service_grade_2_list;
	}

}

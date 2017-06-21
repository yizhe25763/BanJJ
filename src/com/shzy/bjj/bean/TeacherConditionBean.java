package com.shzy.bjj.bean;

import java.io.Serializable;

public class TeacherConditionBean implements Serializable {
	private String city;
	private int condition_serviec_type;
	private int condition_serviec_id;
	private String condition_time;
	private String condition_skill_id;
	private int condition_order_id;
	private String condition_order_type;
	private String condition_district;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getCondition_serviec_type() {
		return condition_serviec_type;
	}

	public void setCondition_serviec_type(int condition_serviec_type) {
		this.condition_serviec_type = condition_serviec_type;
	}

	public int getCondition_serviec_id() {
		return condition_serviec_id;
	}

	public void setCondition_serviec_id(int condition_serviec_id) {
		this.condition_serviec_id = condition_serviec_id;
	}

	public String getCondition_time() {
		return condition_time;
	}

	public void setCondition_time(String condition_time) {
		this.condition_time = condition_time;
	}

	public String getCondition_skill_id() {
		return condition_skill_id;
	}

	public void setCondition_skill_id(String condition_skill_id) {
		this.condition_skill_id = condition_skill_id;
	}

	public int getCondition_order_id() {
		return condition_order_id;
	}

	public void setCondition_order_id(int condition_order_id) {
		this.condition_order_id = condition_order_id;
	}

	public String getCondition_order_type() {
		return condition_order_type;
	}

	public void setCondition_order_type(String condition_order_type) {
		this.condition_order_type = condition_order_type;
	}

	public String getCondition_district() {
		return condition_district;
	}

	public void setCondition_district(String condition_district) {
		this.condition_district = condition_district;
	}

}

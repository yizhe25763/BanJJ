package com.shzy.bjj.bean;

import java.io.Serializable;

public class TeacherDetailResumeBean implements Serializable {
	// 时间跨度
	private String time_descb;
	// 学校/单位
	private String unit_descb;
	// 专业/职业
	private String profession_descb;

	public String getTime_descb() {
		return time_descb;
	}

	public void setTime_descb(String time_descb) {
		this.time_descb = time_descb;
	}

	public String getUnit_descb() {
		return unit_descb;
	}

	public void setUnit_descb(String unit_descb) {
		this.unit_descb = unit_descb;
	}

	public String getProfession_descb() {
		return profession_descb;
	}

	public void setProfession_descb(String profession_descb) {
		this.profession_descb = profession_descb;
	}

}

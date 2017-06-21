package com.shzy.bjj.bean;

import java.io.Serializable;

public class BusinessDistrictBean implements Serializable {
	private String district_name;

	public BusinessDistrictBean() {
	}

	public BusinessDistrictBean(String district_name) {
		this.district_name = district_name;
	}

	public String getDistrict_name() {
		return district_name;
	}

	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

}

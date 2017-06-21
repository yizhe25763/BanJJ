package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class BusinessAreaBean implements Serializable {
	private String area_name;
	private int district_count;
	private List<BusinessDistrictBean> district_list;

	public String getArea_name() {
		return area_name;
	}

	public void setArea_name(String area_name) {
		this.area_name = area_name;
	}

	public int getDistrict_count() {
		return district_count;
	}

	public void setDistrict_count(int district_count) {
		this.district_count = district_count;
	}

	public List<BusinessDistrictBean> getDistrict_list() {
		return district_list;
	}

	public void setDistrict_list(List<BusinessDistrictBean> district_list) {
		this.district_list = district_list;
	}

}

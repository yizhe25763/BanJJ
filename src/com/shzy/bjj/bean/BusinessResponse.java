package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 获取城市的商圈列表
 * 
 * @author Administrator
 * 
 */
public class BusinessResponse implements Serializable {
	private int area_count;
	private List<BusinessAreaBean> area_list;

	public int getArea_count() {
		return area_count;
	}

	public void setArea_count(int area_count) {
		this.area_count = area_count;
	}

	public List<BusinessAreaBean> getArea_list() {
		return area_list;
	}

	public void setArea_list(List<BusinessAreaBean> area_list) {
		this.area_list = area_list;
	}

}

package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class AppointmentResponse implements Serializable {
	private int count;
	private List<AppointmentBean> list;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<AppointmentBean> getList() {
		return list;
	}

	public void setList(List<AppointmentBean> list) {
		this.list = list;
	}

}

package com.shzy.bjj.bean;

import java.io.Serializable;
import java.util.List;

public class TeacherAppointmentRequest implements Serializable {

	private List<String> dataList;
	private AppointmentBean address;
	private String memo;
	private BabyBean baby;

	private String time;
	private int count;

	public TeacherAppointmentRequest(List<String> dataList,
			AppointmentBean address, String memo, BabyBean baby, String time,
			int count) {
		this.dataList = dataList;
		this.address = address;
		this.memo = memo;
		this.baby = baby;
		this.time = time;
		this.count = count;
	}

	public List<String> getDataList() {
		return dataList;
	}

	public void setDataList(List<String> dataList) {
		this.dataList = dataList;
	}

	public AppointmentBean getAddress() {
		return address;
	}

	public void setAddress(AppointmentBean address) {
		this.address = address;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public BabyBean getBaby() {
		return baby;
	}

	public void setBaby(BabyBean baby) {
		this.baby = baby;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}

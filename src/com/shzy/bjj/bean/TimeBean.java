package com.shzy.bjj.bean;

import java.io.Serializable;

public class TimeBean implements Serializable {
	private String orderTime;
	private AppointmentBean address;
	private String memo;
	private BabyBean baby;
	private String pic;

	private String time;
	private int count;

	public TimeBean() {
	}

	public TimeBean(String orderTime, AppointmentBean address, String memo,
			BabyBean baby, String time, int count) {
		super();
		this.orderTime = orderTime;
		this.address = address;
		this.memo = memo;
		this.baby = baby;
		this.time = time;
		this.count = count;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
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
